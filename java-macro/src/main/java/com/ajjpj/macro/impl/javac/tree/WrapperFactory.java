package com.ajjpj.macro.impl.javac.tree;

import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class WrapperFactory {
    public static MExpressionTree wrap (JCTree.JCExpression inner) {
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
        return new MJavacStatement(inner); //TODO refine for different kinds of statements
    }
}
