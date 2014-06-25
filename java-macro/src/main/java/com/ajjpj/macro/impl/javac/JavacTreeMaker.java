package com.ajjpj.macro.impl.javac;

import com.ajjpj.macro.impl.javac.tree.expr.MJavacBinaryExpression;
import com.ajjpj.macro.impl.javac.tree.expr.MJavacLiteralExpression;
import com.ajjpj.macro.impl.javac.tree.stmt.MJavacBlockStatement;
import com.ajjpj.macro.impl.javac.tree.support.WrapperFactory;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.tree.expr.MBinaryExpressionTree;
import com.ajjpj.macro.tree.expr.MLiteralTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiers;
import com.ajjpj.macro.tree.support.MType;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import java.util.Arrays;
import java.util.List;

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
        return WrapperFactory.wrap (parserFactory.newParser(stmt, false, false, false).parseStatement()); //TODO apply 'partial Enter'
    }

    @Override public MModifiers Modifiers (int mask) {
        make.Modifiers()
    }

    @Override public MMethodTree ConcreteMethod(String name, MType returnType, MModifiers modifiers, List<MVariableDeclTree> parameters, MBlockTree body) {

    }

    @Override public MBlockTree Block (MStatementTree... statements) {
        com.sun.tools.javac.util.List<JCTree.JCStatement> stmts = com.sun.tools.javac.util.List.nil();

        for(int i=statements.length - 1; i >= 0; i--) {
            stmts.prepend((JCTree.JCStatement) statements[i].getInternalRepresentation());
        }

        return new MJavacBlockStatement (make.Block(0, stmts));
    }

    @Override public MBinaryExpressionTree BinaryExpression(MExpressionTree left, MExpressionTree right, BinaryOperator op) {
        final JCTree.Tag tag = JCTree.Tag.valueOf (op.name());
        final JCTree.JCBinary inner = make.Binary (tag,
                (JCTree.JCExpression) left.getInternalRepresentation(),
                (JCTree.JCExpression) right.getInternalRepresentation());

        return new MJavacBinaryExpression(inner);
    }

    @Override public MLiteralTree Literal (Object value) {
        final JCTree.JCLiteral inner = value != null ? make.Literal (value) : make.Literal (TypeTag.BOT, null);
        return new MJavacLiteralExpression(inner);
    }
}

//TODO what is a good time to apply 'PartialEnter' to newly created nodes?