package com.ajjpj.macro.impl.javac;

import com.ajjpj.macro.impl.javac.tree.*;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.tree.expr.MBinaryExpressionTree;
import com.ajjpj.macro.tree.expr.MLiteralTree;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
class JavacTreeMaker implements MTreeMaker {
    private final TreeMaker make;
    private final ParserFactory parserFactory;

    JavacTreeMaker(Context context) {
        make = TreeMaker.instance (context);
        parserFactory = ParserFactory.instance (context);
    }

    @Override public MExpressionTree parseExpression (String expr) {
        return WrapperFactory.wrap(parserFactory.newParser(expr, false, false, false).parseExpression());
    }

    @Override public MStatementTree parseStatement (String stmt) {
        return WrapperFactory.wrap(parserFactory.newParser(stmt, false, false, false).parseStatement());
    }

    @Override public MBinaryExpressionTree BinaryExpression(MExpressionTree left, MExpressionTree right, BinaryOperator op) {
        final JCTree.Tag tag = JCTree.Tag.valueOf (op.name());
        final JCTree.JCBinary inner = make.Binary (tag,
                (JCTree.JCExpression) left.getInternalRepresentation(),
                (JCTree.JCExpression) right.getInternalRepresentation());

        return new MJavacBinaryExpression (inner);
    }

    @Override public MLiteralTree Literal (Object value) {
        final JCTree.JCLiteral inner = value != null ? make.Literal (value) : make.Literal (TypeTag.BOT, null);
        return new MJavacLiteralExpression (inner);
    }
}
