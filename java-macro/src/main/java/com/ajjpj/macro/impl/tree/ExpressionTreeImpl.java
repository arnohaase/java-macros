package com.ajjpj.macro.impl.tree;

import com.ajjpj.macro.tree.MExpressionTree;

/**
 * @author arno
 */
public class ExpressionTreeImpl<T> implements MExpressionTree<T> {
    private final Object internalRepresentation;

    public ExpressionTreeImpl(Object internalRepresentation) {
        this.internalRepresentation = internalRepresentation;
    }

    @Override public Object getInternalRepresentation() {
        return internalRepresentation;
    }
}
