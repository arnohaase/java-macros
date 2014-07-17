package com.ajjpj.macro.jdk18.tree;

import com.ajjpj.macro.tree.MExpressionTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class MJavacExpression implements MExpressionTree {
    //TODO remove this - it is only a temporary fixture until all concrete expression kinds are wrapped specifically

    private final JCTree.JCExpression inner;

    public MJavacExpression(JCTree.JCExpression inner) {
        this.inner = inner;
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }
}
