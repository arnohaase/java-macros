package com.ajjpj.macro.jdk18.tree.stmt;

import com.ajjpj.macro.jdk18.tree.support.JavacSourcePosition;
import com.ajjpj.macro.jdk18.tree.support.WrappedStatementList;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.support.MSourcePosition;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;


/**
 * @author arno
 */
public class MJavacBlockStatement implements MBlockTree {
    private final JCTree.JCCompilationUnit compilationUnit;
    private final JCTree.JCBlock inner;

    public MJavacBlockStatement (JCTree.JCCompilationUnit compilationUnit, JCTree.JCBlock inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    @Override public List<MStatementTree> getStatements () {
        return new WrappedStatementList (compilationUnit, inner.getStatements ()); //TODO deal with changes / additions
    }

    @Override public JCTree.JCBlock getInternalRepresentation () {
        return inner;
    }

    @Override public MSourcePosition getSourcePosition () {
        return new JavacSourcePosition (compilationUnit, inner.pos);
    }
}
