package com.ajjpj.macro.impl;

import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
class MethodWithOwner {
    public final JCTree.JCClassDecl owner;
    public final JCTree.JCMethodDecl method;

    public MethodWithOwner(JCTree.JCClassDecl owner, JCTree.JCMethodDecl method) {
        this.owner = owner;
        this.method = method;
    }

    @Override
    public String toString() {
        return "MethodWithOwner{" +
                "owner=" + owner +
                ", method=" + method +
                '}';
    }
}
