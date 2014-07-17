package com.ajjpj.macro.jdk18.tree;

import com.ajjpj.macro.jdk18.tree.support.JavacModifiersView;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.support.MModifiers;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class MJavacClassTree implements MClassTree {
    private JCTree.JCClassDecl inner;

    public MJavacClassTree(JCTree.JCClassDecl inner) {
        this.inner = inner;
    }

    @Override public String getName () {
        return inner.name.toString ();
    }

    @Override public MModifiers getModifiers () {
        return new JavacModifiersView (inner.getModifiers ());
    }

    @Override public JCTree.JCClassDecl getInternalRepresentation() {
        return inner;
    }
}
