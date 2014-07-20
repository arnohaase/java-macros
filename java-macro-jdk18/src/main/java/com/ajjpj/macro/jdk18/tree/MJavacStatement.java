package com.ajjpj.macro.jdk18.tree;

import com.ajjpj.macro.jdk18.tree.support.JavacSourcePosition;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.support.MSourcePosition;
import com.sun.tools.javac.tree.JCTree;


/**
 * @author arno
 */
@Deprecated
public class MJavacStatement implements MStatementTree { //TODO delete this once concrete classes for all specific kinds of statements are in place
    private final JCTree.JCCompilationUnit compilationUnit;
    private final JCTree.JCStatement inner;

    public MJavacStatement (JCTree.JCCompilationUnit compilationUnit, JCTree.JCStatement inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }

    @Override public MSourcePosition getSourcePosition () {
        return new JavacSourcePosition (compilationUnit, inner.pos);
    }
}
