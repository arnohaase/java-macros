package com.ajjpj.macro.impl.javac.tree;

import com.ajjpj.macro.impl.javac.tree.stmt.MJavacBlockStatement;
import com.ajjpj.macro.impl.javac.tree.stmt.MJavacVariableDeclStatement;
import com.ajjpj.macro.impl.javac.tree.support.AbstractJavacModifiersView;
import com.ajjpj.macro.impl.javac.tree.support.MJavacType;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiers;
import com.ajjpj.macro.tree.support.MType;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author arno
 */
public class JavacMethodTree implements MMethodTree {
    private final JCTree.JCMethodDecl inner;

    public JavacMethodTree(JCTree.JCMethodDecl inner) {
        this.inner = inner;
    }

    @Override public String getName() {
        return inner.name.toString();
    }

    @Override public MType getReturnType() {
        return new MJavacType (inner.getReturnType().type);
    }

    @Override public MModifiers getModifiers() {
        return new AbstractJavacModifiersView(inner.getModifiers());
    }

    @Override public List<MVariableDeclTree> getParameters() {
        final List<MVariableDeclTree> result = new ArrayList<>();
        for(JCTree.JCVariableDecl v: inner.getParameters()) {
            result.add (new MJavacVariableDeclStatement(v));
        }

        return result;
    }

    @Override public MBlockTree getBody() {
        return new MJavacBlockStatement(inner.getBody());
    }

    @Override public Object getInternalRepresentation() {
        return inner;
    }
}
