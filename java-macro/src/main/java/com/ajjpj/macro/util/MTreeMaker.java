package com.ajjpj.macro.util;

import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.tree.expr.MBinaryExpressionTree;
import com.ajjpj.macro.tree.expr.MLiteralTree;

/**
 * @author arno
 */
public interface MTreeMaker {
    MExpressionTree parseExpression (String expr);
    MStatementTree parseStatement (String stmt);

    MBinaryExpressionTree BinaryExpression (MExpressionTree left, MExpressionTree right, BinaryOperator op);
    MLiteralTree Literal (Object value);
}
