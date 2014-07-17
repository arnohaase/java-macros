package com.ajjpj.macro.jdk18.tree.support;

import com.ajjpj.macro.jdk18.tree.MJavacExpression;
import com.ajjpj.macro.jdk18.tree.MJavacStatement;
import com.ajjpj.macro.jdk18.tree.expr.MJavacLiteralExpression;
import com.ajjpj.macro.jdk18.tree.stmt.MJavacBlockStatement;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class WrapperFactory {
    public static MExpressionTree wrap (JCTree.JCExpression inner) {
        if(inner == null) {
            return null;
        }

        switch (inner.getKind()) {
            case BOOLEAN_LITERAL:
            case CHAR_LITERAL:
            case DOUBLE_LITERAL:
            case FLOAT_LITERAL:
            case INT_LITERAL:
            case LONG_LITERAL:
            case NULL_LITERAL:
            case STRING_LITERAL:
                return new MJavacLiteralExpression((JCTree.JCLiteral) inner);
            default:
                //TODO remove this once all kinds of expression are accounted for
                return new MJavacExpression(inner);
        }
    }

    public static MStatementTree wrap (JCTree.JCStatement inner) {
        if(inner == null) {
            return null;
        }

        switch (inner.getKind()) {
            case BLOCK: return new MJavacBlockStatement((JCTree.JCBlock) inner);
            case VARIABLE: //TODO
        }

        return new MJavacStatement(inner); //TODO refine for different kinds of statements
    }
}
