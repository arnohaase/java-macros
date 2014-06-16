package com.ajjpj.macro.tree.expr;

import com.ajjpj.macro.tree.MExpressionTree;

/**
 * @author arno
 */
public interface MLiteralTree<T> extends MExpressionTree<T> {
    T getValue();
    //TODO getType()?
}
