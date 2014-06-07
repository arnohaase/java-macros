package com.ajjpj.macro;

import com.sun.source.tree.ExpressionTree;

/**
 * @author arno
 */
public class TypedExpressionTree<T> {
    public final ExpressionTree tree;

    public TypedExpressionTree(ExpressionTree tree) {
        this.tree = tree;
    }

    @Override public String toString() {
        return "TypedExpressionTree{" +
                "tree=" + tree +
                '}';
    }
}
