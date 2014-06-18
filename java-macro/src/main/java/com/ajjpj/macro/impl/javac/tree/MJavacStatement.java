package com.ajjpj.macro.impl.javac.tree;

import com.ajjpj.macro.tree.MStatementTree;
import com.sun.tools.javac.tree.JCTree;


/**
 * @author arno
 */
public class MJavacStatement implements MStatementTree { //TODO delete this once concrete classes for all specific kinds of statements are in place
    private final JCTree.JCStatement inner;

    public MJavacStatement(JCTree.JCStatement inner) {
        this.inner = inner;
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }
}
