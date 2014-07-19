package com.ajjpj.macro.jdk18.tree;

import com.ajjpj.macro.jdk18.tree.stmt.MJavacBlockStatement;
import com.ajjpj.macro.jdk18.tree.stmt.MJavacVariableDeclStatement;
import com.ajjpj.macro.jdk18.tree.support.JavacModifiersView;
import com.ajjpj.macro.jdk18.tree.support.MJavacType;
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

    public JavacMethodTree (JCTree.JCMethodDecl inner) {
        this.inner = inner;
    }

    @Override public String getName () {
        return inner.name.toString ();
    }

    @Override public MType getReturnType () {
        return new MJavacType (inner.getReturnType ().type);
    }

    @Override public MModifiers getModifiers () {
        return new JavacModifiersView (inner.getModifiers ());
    }

    @Override public List<MVariableDeclTree> getParameters () {
        final List<MVariableDeclTree> result = new ArrayList<> ();
        for (JCTree.JCVariableDecl v : inner.getParameters ()) {
            result.add (new MJavacVariableDeclStatement (v));
        }

        return result;
    }

    @Override public MBlockTree getBody () {
        return new MJavacBlockStatement (inner.getBody ());
    }

    @Override public JCTree.JCMethodDecl getInternalRepresentation () {
        return inner;
    }
}
