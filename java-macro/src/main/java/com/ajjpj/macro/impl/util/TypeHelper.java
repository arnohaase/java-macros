package com.ajjpj.macro.impl.util;

import com.sun.tools.javac.code.Symbol;
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

    public TypeHelper(Context context) {
        this.make = TreeMaker.instance (context);
        this.names = Names.instance (context);
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


    /**
     * This method is meant to be called after the Enter phase but before the Attr phase. It performs a tiny
     *  subset of what the Attr phase does, namely analyzing the invocation of a method that is assumed to be
     *
     *  <ul>
     *      <li>static,</li>
     *      <li>invoked in a straight-forward static way,</li>
     *      <li>member of a top-level class that is</li>
     *      <li>precompiled and</li>
     *      <li>available on the class path</li>
     *  </ul>
     *
     * The Attr phase does a far more comprehensive job of cross-referencing method invocations (and expressions in general),
     *  but macro processing is done right after the Enter phase is finished - so Attr results are not available yet.
     *
     * @return the method if it exists, <code>null</code> otherwise
     */
    public Method resolveInvocationOfExternalStaticMethod (JCTree.JCCompilationUnit compilationUnit, JCTree.JCMethodInvocation methodInvocation, ClassLoader cl) {
        final String qualifiedMethodName = methodNameFqn (methodInvocation.getMethodSelect());
        if (qualifiedMethodName == null) {
            return null;
        }

        final int idxDot = qualifiedMethodName.lastIndexOf('.');
        if(idxDot == -1) {
            return null; //TODO static imports
        }
        final String typeName = qualifiedMethodName.substring(0, idxDot);
        final String methodName = qualifiedMethodName.substring (idxDot+1);

        final Class<?> cls = resolveClassFromImports (typeName, compilationUnit, cl);
        if (cls == null) {
            return null;
        }

        try {
            for (Method candidate: cls.getMethods()) {
                if (! methodName.equals(candidate.getName())) {
                    continue;
                }
                if (! Modifier.isStatic (candidate.getModifiers())) {
                    continue;
                }
                if (candidate.getParameterCount() == methodInvocation.getArguments().size()) { //TODO refine this
                    return candidate;
                }
            }
        }
        catch (Exception exc) {
            exc.printStackTrace(); //TODO error handling
        }
        return null;
    }

    private Class<?> resolveClassFromImports (String className, JCTree.JCCompilationUnit compilationUnit, ClassLoader cl) {
        try {
            return cl.loadClass(className);
        }
        catch (Exception exc) {/**/}

        if (className.indexOf('.') != -1) {
            return null;
        }

        final Name name = names.fromString (className);
        Symbol symbol = compilationUnit.namedImportScope.lookup (name).sym;

        final String pkg = compilationUnit.packge.fullname.toString();
        try {
            return cl.loadClass(pkg + "." + className);
        }
        catch (Exception exc) {/**/}

        if (symbol == null) {
            symbol = compilationUnit.starImportScope.lookup (name).sym;
        }

        if (symbol != null) {
            final String fqn = ((Symbol.ClassSymbol) symbol).fullname.toString();
            try {
                return cl.loadClass (fqn);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //TODO error handling
            }
        }

        return null;
    }

    private String methodNameFqn (JCTree.JCExpression methodSelect) {
        if (methodSelect instanceof JCTree.JCIdent) {
            return ((JCTree.JCIdent) methodSelect).getName().toString();
        }
        if (methodSelect instanceof JCTree.JCFieldAccess) {
            final JCTree.JCFieldAccess fieldAccess = (JCTree.JCFieldAccess) methodSelect;
            return methodNameFqn (fieldAccess.getExpression()) + "." + fieldAccess.name;
        }
        return null;
    }

}
