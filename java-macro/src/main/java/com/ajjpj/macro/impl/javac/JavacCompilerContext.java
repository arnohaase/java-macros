package com.ajjpj.macro.impl.javac;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.impl.javac.tree.MJavacExpression;
import com.ajjpj.macro.impl.javac.tree.MJavacLiteralExpression;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class JavacCompilerContext implements CompilerContext {
    private final Context context;
    private final MTreeMaker treeMaker;

    public JavacCompilerContext(Context context) {
        this.context = context;
        treeMaker = new JavacTreeMaker (context);
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

    @Override public MTreeMaker treeMaker() {
        return treeMaker;
    }
}
