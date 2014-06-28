package com.ajjpj.macro.impl.shared.methodmacro;

import com.ajjpj.macro.MethodMacro;
import com.ajjpj.macro.impl.javac.JavacCompilerContext;
import com.ajjpj.macro.impl.javac.tree.support.WrapperFactory;
import com.ajjpj.macro.impl.util.TypeHelper;
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
        staticMethodResolver = new StaticMethodResolver (context);
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
        visitApply1(tree);
    }

    public void visitApply1 (JCTree.JCMethodInvocation tree) {
        final Symbol.MethodSymbol placeholder = staticMethodResolver.getUniqueInvokedMacroPlaceholder(env.toplevel, tree);

        System.out.println("111111111");

        if (placeholder == null) {
            super.visitApply (tree);
            return;
        }

        System.out.println("11111111111111111");

        try {
            final Method macroMethod = findCorrespondingMacroMethod (placeholder);
            System.out.println("macro method: " + macroMethod);
            final Object[] args = new Object[macroMethod.getParameterCount()];

            args[0] = new JavacCompilerContext (context, compilationUnit);

            int idx = 1;
            List<JCTree.JCExpression> argList = tree.getArguments();
            while(argList.nonEmpty()) {
                args[idx] = WrapperFactory.wrap (argList.head);
                argList = argList.tail;
            }

            System.out.println("111111111111111111111111111");
            final MExpressionTree transformed = (MExpressionTree) macroMethod.invoke (null, args);
            result = (JCTree) transformed.getInternalRepresentation(); //make.Literal (TypeTag.BOT, null); //TODO
            System.out.println (result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e); // TODO error handling
        }

    }

    public void visitApply2 (JCTree.JCMethodInvocation tree) {
        final Method target = typeHelper.resolveInvocationOfExternalStaticMethod (env.toplevel, tree, cl);
        if (target == null || target.getAnnotation (MethodMacroPlaceholder.class) == null) {
            super.visitApply(tree);
        }
        else {
            final Method methodMacro = findCorrespondingMacroMethod (target);
            final Object[] args = new Object[methodMacro.getParameterCount()];

            args[0] = new JavacCompilerContext (context, compilationUnit);

            int idx = 1;
            List<JCTree.JCExpression> argList = tree.getArguments();
            while(argList.nonEmpty()) {
                args[idx] = WrapperFactory.wrap (argList.head);
                argList = argList.tail;
            }

            try {
                System.out.println("22222222222222222222222222222222222222");
                final MExpressionTree transformed = (MExpressionTree) methodMacro.invoke (null, args);
                result = (JCTree) transformed.getInternalRepresentation(); //make.Literal (TypeTag.BOT, null); //TODO
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e); // TODO error handling
            }
        }
    }

    private Method findCorrespondingMacroMethod (Method placeholder) {
        for (Method candidate: placeholder.getDeclaringClass().getMethods()) {
            if (candidate.getName().equals(placeholder.getName()) &&
                    candidate.getAnnotation (MethodMacro.class) != null &&
                    candidate.getParameterCount() == placeholder.getParameterCount() + 1) { //TODO more refined checks
                return candidate;
            }
        }
        throw new RuntimeException ("no macro method for placeholder " + placeholder); //TODO error handling
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
