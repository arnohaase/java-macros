package com.ajjpj.macro.impl.javac.tree.stmt;

import com.ajjpj.macro.impl.javac.tree.support.WrappedStatementList;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;

/**
 * @author arno
 */
public class MJavacBlockStatement implements MBlockTree {
    private final JCTree.JCBlock inner;

    public MJavacBlockStatement(JCTree.JCBlock inner) {
        this.inner = inner;
    }

    @Override public List<MStatementTree> getStatements() {
        return new WrappedStatementList (inner.getStatements()); //TODO deal with changes / additions
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }
}
