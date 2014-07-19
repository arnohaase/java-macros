package com.ajjpj.macro.jdk18.tree.stmt;

import com.ajjpj.macro.jdk18.tree.support.MJavacType;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MType;
import com.sun.tools.javac.tree.JCTree;


/**
 * @author arno
 */
public class MJavacVariableDeclStatement implements MVariableDeclTree {
    private final JCTree.JCVariableDecl inner;

    public MJavacVariableDeclStatement (JCTree.JCVariableDecl inner) {
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
}
