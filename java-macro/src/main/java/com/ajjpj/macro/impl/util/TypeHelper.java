package com.ajjpj.macro.impl.util;

import com.ajjpj.macro.impl.shared.methodmacro.MethodMacroPlaceholder;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author arno
 */
public class TypeHelper {
    private final TreeMaker make;
    private final Names names;
    private final ClassReader classes;

    public TypeHelper(Context context) {
        this.make = TreeMaker.instance (context);
        this.names = Names.instance (context);
        this.classes = ClassReader.instance (context);
    }

    public JCTree makeTypeTree (String typeName) {
        String remainder = typeName;
        JCTree.JCExpression result = null;
        int idxDot;

        do {
            idxDot = remainder.indexOf('.');

            final String nameString = idxDot > 0 ? remainder.substring(0, idxDot) : remainder;
            final Name name = names.fromString (nameString);

            result = result != null ? make.Select (result, name) : make.Ident(name);

            remainder = remainder.substring (idxDot + 1);
        }
        while (idxDot > 0);

        return result;
    }

    public String getAnnotationFqn (JCTree.JCAnnotation annotationTree) {
        final JCTree tpe = annotationTree.annotationType;

        if (tpe instanceof JCTree.JCIdent) {
            final JCTree.JCIdent ident = (JCTree.JCIdent) tpe;
            return ident.sym.toString();
        }
        throw new IllegalArgumentException("TODO: annotations with FQN in code"); //TODO annotations with fqn
    }

    public boolean hasAnnotation (JCTree.JCMethodDecl tree, String annotationFqn) {
        final JCTree.JCModifiers modifiers = tree.getModifiers();
        if(modifiers == null) {
            return false;
        }

        for(JCTree.JCAnnotation annot: modifiers.getAnnotations()) {
            final String annotName = getAnnotationFqn (annot);

            if(annotationFqn.equals(annotName)) {
                return true;
            }
        }
        return false;
    }
}
