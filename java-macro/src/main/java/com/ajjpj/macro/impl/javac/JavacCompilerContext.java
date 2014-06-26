package com.ajjpj.macro.impl.javac;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.impl.javac.tree.support.MJavacTypes;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class JavacCompilerContext implements CompilerContext {
    private final Context context;
    private final MTreeMaker treeMaker;
    private final Symtab syms;

    public JavacCompilerContext(Context context) {
        this.context = context;
        treeMaker = new JavacTreeMaker (context);
        syms = Symtab.instance (context);
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

    @Override public MJavacTypes types() {
        return new MJavacTypes (context);
    }

    @Override public MTreeMaker treeMaker() {
        return treeMaker;
    }
}
