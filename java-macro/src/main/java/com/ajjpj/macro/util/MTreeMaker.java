package com.ajjpj.macro.util;

import com.ajjpj.macro.tree.MClassTree;
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
import com.ajjpj.macro.tree.support.MVisibility;

import java.util.List;

/**
 * @author arno
 */
public interface MTreeMaker {
    //--- parsing

    MExpressionTree parseExpression (String expr);
    MStatementTree parseStatement (String stmt);

    //--- AST modification

    void addMethod (MClassTree cls, MMethodTree method);
    void removeMethod (MClassTree cls, MMethodTree method);

    //--- AST creation

    MClassTree Class (String name, MModifiers modifiers);

    MMethodTree ConcreteMethod (String name, MType returnType, MModifiers modifiers, List<MVariableDeclTree> parameters, MBlockTree body);

    //--- AST creation: expressions

    MBinaryExpressionTree BinaryExpression (MExpressionTree left, MExpressionTree right, BinaryOperator op);
    MLiteralTree Literal (Object value);

    //--- AST creation: statements

    MBlockTree Block(MStatementTree... statements);
}
