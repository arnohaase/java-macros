package com.ajjpj.macro.impl.tree;

import com.ajjpj.macro.tree.ClassTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class ClassTreeImpl implements ClassTree {
    private final JCTree.JCClassDecl inner;

    public ClassTreeImpl(JCTree.JCClassDecl inner) {
        this.inner = inner;
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }
}
