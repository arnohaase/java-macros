package com.ajjpj.macro.tree.expr;

import com.ajjpj.macro.tree.MExpressionTree;

/**
 * @author arno
 */
public interface MBinaryExpressionTree extends MExpressionTree {
    MExpressionTree getLeft();
    MExpressionTree getRight();
    BinaryOperator getOperator();
}
