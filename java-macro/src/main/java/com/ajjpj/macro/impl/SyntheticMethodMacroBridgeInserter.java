package com.ajjpj.macro.impl;

import com.ajjpj.macro.MethodMacro;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;


/**
 * @author arno
 */
class SyntheticMethodMacroBridgeInserter extends TreeTranslator {
    @Override public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
        result = jcMethodDecl; // shortcut - no need for further recursive descent

        if(! isMethodMacro (jcMethodDecl)) {
            return;
        }

        System.out.println("method macro: " + jcMethodDecl);
    }

    boolean isMethodMacro(JCTree.JCMethodDecl mtd) {
        if (! hasMacroMethodAnnotation (mtd)) {
            return false;
        }

        //TODO verify signature, static, visibility

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

                if(MethodMacro.class.getName().equals(annotName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
