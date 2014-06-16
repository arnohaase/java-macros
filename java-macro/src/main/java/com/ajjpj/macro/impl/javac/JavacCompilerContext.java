package com.ajjpj.macro.impl.javac;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.impl.javac.tree.MJavacExpression;
import com.ajjpj.macro.impl.javac.tree.MJavacLiteralExpression;
import com.ajjpj.macro.tree.MExpressionTree;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class JavacCompilerContext implements CompilerContext {
    private final Context context;

    public JavacCompilerContext(Context context) {
        this.context = context;
    }

    @Override public Context getContext() {
        return context;
    }

    @Override
    public void msg(String msg) {

    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public MExpressionTree parseExpression (String exprString) {
        final JCTree.JCExpression expr = ParserFactory.instance (context).newParser (exprString, false, false, false).parseExpression();
        return wrap (expr);
    }

    private MExpressionTree wrap (JCTree.JCExpression inner) {
        switch (inner.getKind()) {
            case BOOLEAN_LITERAL:
            case CHAR_LITERAL:
            case DOUBLE_LITERAL:
            case FLOAT_LITERAL:
            case INT_LITERAL:
            case LONG_LITERAL:
            case NULL_LITERAL:
            case STRING_LITERAL:
                return new MJavacLiteralExpression ((JCTree.JCLiteral) inner);
            default:
                //TODO remove this once all kinds of expression are accounted for
                return new MJavacExpression(inner);
        }
    }
}
