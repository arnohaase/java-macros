package com.ajjpj.macro.jdk18.tree.support;

import com.ajjpj.macro.tree.support.MSourcePosition;
import com.sun.tools.javac.tree.JCTree;

import javax.tools.JavaFileObject;


/**
 * @author arno
 */
public class JavacSourcePosition implements MSourcePosition {
    private final JCTree.JCCompilationUnit compilationUnit;
    private final int offset;

    public JavacSourcePosition (JCTree.JCCompilationUnit compilationUnit, int offset) {
        this.compilationUnit = compilationUnit;
        this.offset = offset;
    }
    @Override public JavaFileObject getSourceFile () {
        return compilationUnit.getSourceFile ();
    }

    @Override public int getLine () {
        return compilationUnit.getLineMap ().getLineNumber (offset);
    }

    @Override public int getColumn () {
        return compilationUnit.getLineMap ().getColumnNumber (offset);
    }
}
