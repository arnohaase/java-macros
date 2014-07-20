package com.ajjpj.macro.jdk18.tree.stmt;

import com.ajjpj.macro.jdk18.tree.support.JavacSourcePosition;
import com.ajjpj.macro.jdk18.tree.support.MJavacType;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MSourcePosition;
import com.ajjpj.macro.tree.support.MType;
import com.sun.tools.javac.tree.JCTree;


/**
 * @author arno
 */
public class MJavacVariableDeclStatement implements MVariableDeclTree {
    private final JCTree.JCCompilationUnit compilationUnit;
    private final JCTree.JCVariableDecl inner;

    public MJavacVariableDeclStatement (JCTree.JCCompilationUnit compilationUnit, JCTree.JCVariableDecl inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    @Override public String getName () {
        return inner.getName ().toString ();
    }

    @Override public MType getType () {
        return new MJavacType (inner.type);
    }

    @Override public Object getInternalRepresentation () {
        return inner;
    }

    @Override public MSourcePosition getSourcePosition () {
        return new JavacSourcePosition (compilationUnit, inner.pos);
    }
}
