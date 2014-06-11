package com.ajjpj.macro.impl.tree;

import com.ajjpj.macro.tree.ExpressionTree;

/**
 * @author arno
 */
public class ExpressionTreeImpl<T> implements ExpressionTree<T> {
    private final Object internalRepresentation;

    public ExpressionTreeImpl(Object internalRepresentation) {
        this.internalRepresentation = internalRepresentation;
    }

    @Override public Object getInternalRepresentation() {
        return internalRepresentation;
    }
}
