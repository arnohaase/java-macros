package com.ajjpj.macro.jdk18.tree.expr;


import com.ajjpj.macro.jdk18.tree.support.WrapperFactory;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.tree.expr.MBinaryExpressionTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class MJavacBinaryExpression implements MBinaryExpressionTree {
    private final JCTree.JCBinary inner;

    public MJavacBinaryExpression(JCTree.JCBinary inner) {
        this.inner = inner;
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }

    @Override public MExpressionTree getLeft() {
        return WrapperFactory.wrap(inner.getLeftOperand());
    }

    @Override public MExpressionTree getRight() {
        return WrapperFactory.wrap(inner.getRightOperand());
    }

    @Override public BinaryOperator getOperator() {
        return BinaryOperator.valueOf (inner.getTag().name());
    }
}
