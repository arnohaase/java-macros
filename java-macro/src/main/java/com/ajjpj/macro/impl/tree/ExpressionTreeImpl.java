package com.ajjpj.macro.impl.tree;

import com.ajjpj.macro.tree.MExpressionTree;

/**
 * @author arno
 */
@Deprecated //TODO remove this once all kinds of expression are supported
public class ExpressionTreeImpl<T> implements MExpressionTree<T> {
    private final Object internalRepresentation;

    public ExpressionTreeImpl(Object internalRepresentation) {
        this.internalRepresentation = internalRepresentation;
    }

    @Override public Object getInternalRepresentation() {
        return internalRepresentation;
    }
}
