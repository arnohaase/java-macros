package com.ajjpj.macro.impl.javac.tree;

import com.ajjpj.macro.tree.MClassTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class MJavacClassTree implements MClassTree {
    private JCTree.JCClassDecl inner;

    public MJavacClassTree(JCTree.JCClassDecl inner) {
        this.inner = inner;
    }

    @Override public JCTree.JCClassDecl getInternalRepresentation() {
        return inner;
    }
}
