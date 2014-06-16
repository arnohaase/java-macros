package com.ajjpj.macro.impl.shared.classmacro;

import com.ajjpj.macro.ClassMacro;
import com.ajjpj.macro.ClassTransformationFactory;

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
    private final Map<String, ClassTransformationFactory> macroAnnotations = new HashMap<>();

    public AnnotationCache(ClassLoader cl) {
        this.cl = cl;
    }

    /**
     * null if this is not a macro annotation
     */
    public ClassTransformationFactory getFactory (String annotationFqn) {
        if (nonMacroAnnotations.contains (annotationFqn)) {
            return null;
        }
        final ClassTransformationFactory prev = macroAnnotations.get (annotationFqn);
        if (prev != null) {
            return prev;
        }

        try {
            final Class<?> cls = cl.loadClass (annotationFqn);
            final ClassMacro clsMacro = cls.getAnnotation (ClassMacro.class);
            if(clsMacro == null) {
                nonMacroAnnotations.add (annotationFqn);
                return null;
            }

            final Class<? extends ClassTransformationFactory> trafoFactoryCls = clsMacro.value();
            final ClassTransformationFactory result = trafoFactoryCls.newInstance();
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
