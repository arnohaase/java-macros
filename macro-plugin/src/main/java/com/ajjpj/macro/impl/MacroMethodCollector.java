package com.ajjpj.macro.impl;

import com.ajjpj.macro.MacroMethod;
import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class scans a tree for methods annotated as com.ajjpj.macro methods, collecting a list of them
 *
 * @author arno
 */
class MacroMethodCollector extends TreeScanner {
    public final List<MethodWithOwner> macroMethods = new ArrayList<>();

    private final LinkedList<JCTree> parentHierarchy = new LinkedList<>();

    @Override public void scan(JCTree tree) {
        parentHierarchy.push(tree);
        try {
            super.scan(tree);
        }
        finally {
            parentHierarchy.pop();
        }
    }

    @Override public void visitMethodDef(JCTree.JCMethodDecl tree) {
        if (isMacroMethod (tree)) {
            final JCTree owner = parentHierarchy.get(1);
            final JCTree.JCClassDecl owningClass = (JCTree.JCClassDecl) owner; // TODO fail more gracefully in other cases

            macroMethods.add(new MethodWithOwner (owningClass, tree));
        }

        super.visitMethodDef(tree);
    }

    private boolean isMacroMethod(JCTree.JCMethodDecl tree) {
        if(! hasMacroMethodAnnotation (tree)) {
            return false;
        }
        //TODO check for static, params, return type
        return true;
    }

    private boolean hasMacroMethodAnnotation(JCTree.JCMethodDecl tree) {
        final JCTree.JCModifiers modifiers = tree.getModifiers();
        if(modifiers == null) {
            return false;
        }

        for(JCTree.JCAnnotation annotation: modifiers.getAnnotations()) {
            final JCTree tpe = annotation.annotationType;
            if(tpe instanceof JCTree.JCIdent) {
                final JCTree.JCIdent ident = (JCTree.JCIdent) tpe;
                final String annotName = ident.sym.toString();

                if(MacroMethod.class.getName().equals(annotName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
