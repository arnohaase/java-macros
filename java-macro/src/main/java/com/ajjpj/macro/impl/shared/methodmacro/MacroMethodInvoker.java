package com.ajjpj.macro.impl.shared.methodmacro;

import com.ajjpj.macro.MethodMacro;
import com.ajjpj.macro.impl.CompilerContextImpl;
import com.ajjpj.macro.impl.tree.ExpressionTreeImpl;
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

    private final Context context;
    private final Enter enter;
    private final TypeHelper typeHelper;

    private Env<AttrContext> env;
    private Scope scope;

    public MacroMethodInvoker (ClassLoader cl, Context context) {
        this.cl = cl;
        this.context = context;
        enter = Enter.instance (context);
        typeHelper = new TypeHelper (context);
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
        final Method target = typeHelper.resolveInvocationOfExternalStaticMethod (env.toplevel, tree, cl);
        if (target == null || target.getAnnotation (MethodMacroPlaceholder.class) == null) {
            super.visitApply(tree);
        }
        else {
            final Method methodMacro = findCorrespondingMacroMethod (target);
            final Object[] args = new Object[methodMacro.getParameterCount()];

            args[0] = new CompilerContextImpl (cl, context);

            int idx = 1;
            List<JCTree.JCExpression> argList = tree.getArguments();
            while(argList.nonEmpty()) {
                args[idx] = new ExpressionTreeImpl (argList.head);
                argList = argList.tail;
            }

            try {
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
}
