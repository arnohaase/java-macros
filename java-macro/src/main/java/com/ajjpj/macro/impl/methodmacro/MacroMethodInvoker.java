package com.ajjpj.macro.impl.methodmacro;

import com.ajjpj.macro.impl.util.TreeDumper;
import com.ajjpj.macro.impl.util.TypeHelper;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import java.lang.reflect.Method;

/**
 * @author arno
 */
public class MacroMethodInvoker extends TreeTranslator {
    private final ClassLoader cl;

    private final Enter enter;
    private final TypeHelper typeHelper;
    private final TreeMaker make;

    private Env<AttrContext> env;
    private Scope scope;

    public MacroMethodInvoker (ClassLoader cl, Context context) {
        this.cl = cl;
        enter = Enter.instance (context);
        typeHelper = new TypeHelper (context);
        make = TreeMaker.instance (context);
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl tree) {
        final Env<AttrContext> prevEnv = env;
        final Scope prevScope = scope;

        Symbol.ClassSymbol sym = tree.sym;
        scope = sym.members();

        try {
            env = enter.getClassEnv (tree.sym);
            super.visitClassDef(tree);
        }
        finally {
            scope = prevScope;
            env = prevEnv;
        }
    }

    @Override
    public void visitApply (JCTree.JCMethodInvocation tree) {
        System.out.println("===============================================");
        System.out.println("???" + tree);
        new TreeDumper().scan(tree);

        final Method target = typeHelper.resolveInvocationOfExternalStaticMethod (env.toplevel, tree, cl);
        if (target == null || target.getAnnotation (MethodMacroBridge.class) == null) {
            super.visitApply(tree);
        }
        else {
            result = make.Literal (TypeTag.BOT, null);
        }
    }

    private String methodNameFqn (JCTree.JCExpression methodSelect) {
        if (methodSelect instanceof JCTree.JCIdent) {
            return ((JCTree.JCIdent) methodSelect).getName().toString();
        }
        if (methodSelect instanceof JCTree.JCFieldAccess) {
            final JCTree.JCFieldAccess fieldAccess = (JCTree.JCFieldAccess) methodSelect;
            return methodNameFqn (fieldAccess.getExpression()) + "." + fieldAccess.name;
        }
        return null;
    }

    private Scope.Entry lookup2 (Scope scope, JCTree.JCExpression methodSelect) {
        if (methodSelect instanceof JCTree.JCIdent) {
            return scope.lookup(((JCTree.JCIdent) methodSelect).name);
        }

        final JCTree.JCFieldAccess fieldAccess = (JCTree.JCFieldAccess) methodSelect;
        final Scope.Entry subEntry = lookup2(scope, fieldAccess.getExpression());

        System.out.println("----> " + methodSelect);

        if(subEntry == null || subEntry.scope == null) {
            return null;
        }

        System.out.println("subEntry: " + subEntry);
        System.out.println("subEntry.scope: " + subEntry.scope);


        return subEntry.scope.lookup(fieldAccess.name);
    }


}
