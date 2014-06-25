package com.ajjpj.macro.impl.shared.classmacro;

import com.ajjpj.macro.AnnotationMacro;
import com.ajjpj.macro.AnnotationMacroMarker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author arno
 */
public class AnnotationCache {
    private final ClassLoader cl;

    private final Set<String> nonMacroAnnotations = new HashSet<>();
    private final Map<String, AnnotationMacro> macroAnnotations = new HashMap<>();

    public AnnotationCache(ClassLoader cl) {
        this.cl = cl;
    }

    /**
     * null if this is not a macro annotation
     */
    public AnnotationMacro getMacro (String annotationFqn) {
        if (nonMacroAnnotations.contains (annotationFqn)) {
            return null;
        }
        final AnnotationMacro prev = macroAnnotations.get (annotationFqn);
        if (prev != null) {
            return prev;
        }

        try {
            final Class<?> cls = cl.loadClass (annotationFqn);
            final AnnotationMacroMarker clsMacro = cls.getAnnotation (AnnotationMacroMarker.class);
            if(clsMacro == null) {
                nonMacroAnnotations.add (annotationFqn);
                return null;
            }

            final Class<? extends AnnotationMacro> trafoFactoryCls = clsMacro.value();
            final AnnotationMacro result = trafoFactoryCls.newInstance();
            macroAnnotations.put (annotationFqn, result);
            return result;
        }
        catch (ClassNotFoundException exc) {
            nonMacroAnnotations.add (annotationFqn);
            return null;
        }
        catch (Exception exc) {
            //TODO error logging
            nonMacroAnnotations.add (annotationFqn);
            return null;
        }
    }
}
