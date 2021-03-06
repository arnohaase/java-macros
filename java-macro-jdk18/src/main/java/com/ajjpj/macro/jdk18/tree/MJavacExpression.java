package com.ajjpj.macro.jdk18.tree;

import com.ajjpj.macro.jdk18.tree.support.JavacSourcePosition;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.support.MSourcePosition;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
@Deprecated
public class MJavacExpression implements MExpressionTree {
    //TODO remove this - it is only a temporary fixture until all concrete expression kinds are wrapped specifically

    private final JCTree.JCCompilationUnit compilationUnit;
    private final JCTree.JCExpression inner;

    public MJavacExpression (JCTree.JCCompilationUnit compilationUnit, JCTree.JCExpression inner) {
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
