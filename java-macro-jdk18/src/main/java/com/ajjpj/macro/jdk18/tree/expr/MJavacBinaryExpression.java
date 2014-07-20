package com.ajjpj.macro.jdk18.tree.expr;


import com.ajjpj.macro.jdk18.tree.support.JavacSourcePosition;
import com.ajjpj.macro.jdk18.tree.support.WrapperFactory;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.tree.expr.MBinaryExpressionTree;
import com.ajjpj.macro.tree.support.MSourcePosition;
import com.sun.tools.javac.tree.JCTree;


/**
 * @author arno
 */
public class MJavacBinaryExpression implements MBinaryExpressionTree {
    private final JCTree.JCCompilationUnit compilationUnit;
    private final JCTree.JCBinary inner;

    public MJavacBinaryExpression (JCTree.JCCompilationUnit compilationUnit, JCTree.JCBinary inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    @Override public Object getInternalRepresentation () {
        return inner;
    }

    @Override public MExpressionTree getLeft () {
        return WrapperFactory.wrap (compilationUnit, inner.getLeftOperand ());
    }

    @Override public MExpressionTree getRight () {
        return WrapperFactory.wrap (compilationUnit, inner.getRightOperand ());
    }

    @Override public BinaryOperator getOperator () {
        return BinaryOperator.valueOf (inner.getTag ().name ());
    }

    @Override public MSourcePosition getSourcePosition () {
        return new JavacSourcePosition (compilationUnit, inner.pos);
    }
}
