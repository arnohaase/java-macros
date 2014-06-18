package com.ajjpj.macro.impl.javac.tree;

import com.ajjpj.macro.tree.expr.MLiteralTree;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class MJavacLiteralExpression implements MLiteralTree {
    private final JCTree.JCLiteral inner;

    public MJavacLiteralExpression (JCTree.JCLiteral inner) {
        this.inner = inner;
    }

    @Override public Object getValue() { //TODO special handling for boolean and character
        return inner.getValue();
    }

    //TODO setValue()?

    @Override public Object getInternalRepresentation() {
        return inner;
    }
}
