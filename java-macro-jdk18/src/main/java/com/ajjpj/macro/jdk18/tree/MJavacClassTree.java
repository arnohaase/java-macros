package com.ajjpj.macro.jdk18.tree;

import com.ajjpj.macro.jdk18.tree.stmt.MJavacVariableDeclStatement;
import com.ajjpj.macro.jdk18.tree.support.JavacModifiersView;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiers;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author arno
 */
public class MJavacClassTree implements MClassTree {
    private JCTree.JCClassDecl inner;

    public MJavacClassTree (JCTree.JCClassDecl inner) {
        this.inner = inner;
    }

    @Override public String getName () {
        return inner.name.toString ();
    }

    @Override public MModifiers getModifiers () {
        return new JavacModifiersView (inner.getModifiers ());
    }

    @Override public JCTree.JCClassDecl getInternalRepresentation () {
        return inner;
    }

    @Override public Collection<MMethodTree> getMethods () {
        final java.util.List<MMethodTree> result = new ArrayList<> ();

        for (JCTree def : inner.defs) {
            if (def instanceof JCTree.JCMethodDecl) {
                final JCTree.JCMethodDecl mtd = (JCTree.JCMethodDecl) def;
                result.add (new JavacMethodTree (mtd));
            }
        }

        return result;
    }

    @Override public Collection<MMethodTree> getMethods (String name) {
        final java.util.List<MMethodTree> result = new ArrayList<> ();

        for (JCTree def : inner.defs) {
            if (def instanceof JCTree.JCMethodDecl) {
                final JCTree.JCMethodDecl mtd = (JCTree.JCMethodDecl) def;
                if (name.equals (mtd.getName ().toString ())) {
                    result.add (new JavacMethodTree (mtd));
                }
            }
        }

        return result;
    }

    @Override public Collection<MVariableDeclTree> getVariables () {
        final java.util.List<MVariableDeclTree> result = new ArrayList<> ();

        for (JCTree def : inner.defs) {
            if (def instanceof JCTree.JCVariableDecl) {
                final JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) def;
                result.add (new MJavacVariableDeclStatement (var));
            }
        }

        return result;
    }

    @Override public MVariableDeclTree getVariable (String name) {
        for (JCTree def : inner.defs) {
            if (def instanceof JCTree.JCVariableDecl) {
                final JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) def;
                if (name.equals (var.getName ().toString ())) {
                    return new MJavacVariableDeclStatement (var);
                }
            }
        }

        return null;
    }
}
