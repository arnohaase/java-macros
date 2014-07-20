package com.ajjpj.macro.jdk18.tree.expr;

import com.ajjpj.macro.jdk18.tree.support.JavacSourcePosition;
import com.ajjpj.macro.tree.expr.MLiteralTree;
import com.ajjpj.macro.tree.support.MSourcePosition;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class MJavacLiteralExpression implements MLiteralTree {
    private final JCTree.JCCompilationUnit compilationUnit;
    private final JCTree.JCLiteral inner;

    public MJavacLiteralExpression (JCTree.JCCompilationUnit compilationUnit, JCTree.JCLiteral inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    @Override public Object getValue() { //TODO special handling for boolean and character
        return inner.getValue();
    }

    //TODO setValue()?

    @Override public Object getInternalRepresentation() {
        return inner;
    }

    @Override public MSourcePosition getSourcePosition () {
        return new JavacSourcePosition (compilationUnit, inner.pos);
    }
}
