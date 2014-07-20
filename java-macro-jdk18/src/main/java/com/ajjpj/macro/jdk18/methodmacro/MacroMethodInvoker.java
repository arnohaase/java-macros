package com.ajjpj.macro.jdk18.methodmacro;

import com.ajjpj.macro.MethodMacro;
import com.ajjpj.macro.jdk18.JavacCompilerContext;
import com.ajjpj.macro.jdk18.tree.support.WrapperFactory;
import com.ajjpj.macro.jdk18.util.TypeHelper;
import com.ajjpj.macro.tree.MExpressionTree;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import java.lang.reflect.Method;

/**
 * @author arno
 */
public class MacroMethodInvoker extends TreeTranslator {
    private final ClassLoader cl;
    private final JCTree.JCCompilationUnit compilationUnit;

    private final Context context;
    private final Enter enter;
    private final TypeHelper typeHelper;

    private Env<AttrContext> env;
    private Scope scope;

    private final StaticMethodResolver staticMethodResolver;

        public MacroMethodInvoker (ClassLoader cl, Context context, JCTree.JCCompilationUnit compilationUnit) {
        this.cl = cl;
        this.context = context;
        enter = Enter.instance (context);
        typeHelper = new TypeHelper (context);
        this.compilationUnit = compilationUnit;
        staticMethodResolver = new StaticMethodResolver(context);
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl tree) {
        final Env<AttrContext> prevEnv = env;
        final Scope prevScope = scope;

        Symbol.ClassSymbol sym = tree.sym;
        if (sym == null) {
            super.visitClassDef (tree);
            return;
        }

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

    @Override public void visitApply (JCTree.JCMethodInvocation tree) {
        final Symbol.MethodSymbol placeholder = staticMethodResolver.getUniqueInvokedMacroPlaceholder(env.toplevel, tree);

        if (placeholder == null) {
            super.visitApply (tree);
            return;
        }

        try {
            final Method macroMethod = findCorrespondingMacroMethod (placeholder);
            final Object[] args = new Object[macroMethod.getParameterCount()];

            args[0] = new JavacCompilerContext(context, compilationUnit);

            int idx = 1;
            List<JCTree.JCExpression> argList = tree.getArguments();
            while(argList.nonEmpty()) {
                args[idx] = WrapperFactory.wrap (compilationUnit, argList.head);
                argList = argList.tail;
            }

            final MExpressionTree transformed = (MExpressionTree) macroMethod.invoke (null, args);
            result = (JCTree) transformed.getInternalRepresentation(); //make.Literal (TypeTag.BOT, null); //TODO
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e); // TODO error handling
        }

    }

    private Method findCorrespondingMacroMethod (Symbol.MethodSymbol placeholder) throws ClassNotFoundException {
        final Symbol.ClassSymbol owner = (Symbol.ClassSymbol) placeholder.owner;
        final Class<?> cls = cl.loadClass(owner.fullname.toString());

        final String methodName = placeholder.name.toString();

        for (Method candidate: cls.getMethods()) {
            if (candidate.getName().equals (methodName) &&
                    candidate.getAnnotation (MethodMacro.class) != null &&
                    candidate.getParameterCount() == placeholder.params.size() + 1) { //TODO more refined checks
                return candidate;
            }
        }
        throw new RuntimeException ("no macro method for placeholder " + placeholder); //TODO error handling
    }
}
