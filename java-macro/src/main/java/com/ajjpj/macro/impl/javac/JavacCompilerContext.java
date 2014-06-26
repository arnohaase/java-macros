package com.ajjpj.macro.impl.javac;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.impl.javac.tree.support.MJavacTypes;
import com.ajjpj.macro.tree.MTree;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.AbstractLog;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

/**
 * @author arno
 */
public class JavacCompilerContext implements CompilerContext {
    private final Context context;
    private final MTreeMaker treeMaker;
    private final Log log;
    private final Enter enter;
    private final JCTree.JCCompilationUnit compilationUnit;

    public JavacCompilerContext (Context context, JCTree.JCCompilationUnit compilationUnit) {
        this.context = context;
        treeMaker = new JavacTreeMaker (context);
        log = Log.instance (context);
        enter = Enter.instance (context);
        this.compilationUnit = compilationUnit;
    }

    public Context getContext() {
        return context;
    }

    @Override public void msg (MTree codePos, String msg) {
        log.useSource (compilationUnit.sourcefile);
        log.note ((JCTree) codePos.getInternalRepresentation(), "proc.messager", msg);
    }

    @Override public void warn(MTree codePos, String msg) {
        log.useSource (compilationUnit.sourcefile);
        log.warning ((JCTree) codePos.getInternalRepresentation(), "proc.messager", msg);
    }

    @Override public void error(MTree codePos, String msg) {
        log.useSource (compilationUnit.sourcefile);
        log.error((JCTree) codePos.getInternalRepresentation(), "proc.messager", msg);
    }

    @Override public MJavacTypes types() {
        return new MJavacTypes (context);
    }

    @Override public MTreeMaker treeMaker() {
        return treeMaker;
    }
}
