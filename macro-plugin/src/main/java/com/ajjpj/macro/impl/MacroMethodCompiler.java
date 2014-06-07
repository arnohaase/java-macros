package com.ajjpj.macro.impl;

import com.sun.swing.internal.plaf.synth.resources.synth;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import java.lang.reflect.Method;

/**
 * @author arno
 */
class MacroMethodCompiler {
    private final Context context;
    private final TreeMaker make;

    MacroMethodCompiler(Context context) {
        this.context = context;
        make = TreeMaker.instance (context);
    }

    public void compileMacroMethods(JCTree.JCCompilationUnit compilationUnit) {

        final MacroMethodCollector mmc = new MacroMethodCollector();
        mmc.visitTopLevel(compilationUnit);

        for (MethodWithOwner mwo: mmc.macroMethods) {
            createSynthetic(mwo, compilationUnit);
        }
    }

    private void createSynthetic(MethodWithOwner mwo, JCTree.JCCompilationUnit compilationUnit) {
        final JCTree.JCMethodDecl mtd = mwo.method;

        final Type.MethodType origType = (Type.MethodType) mtd.sym.type;

//        final Type.MethodType synthType = new Type.MethodType(List.<Type>nil(), origType.restype, List.<Type>nil(), origType.tsym);
//        final Symbol.MethodSymbol synthSym = new Symbol.MethodSymbol(mtd.sym.flags(), mtd.sym.name, synthType, mtd.sym.owner);

        final Names names = Names.instance(context);
        final Symtab syms = Symtab.instance(context);

        final Enter enter = Enter.instance(context);

        final Env env = enter.getEnv(mwo.owner.sym);

        final JCTree.JCBlock impl = make.Block(0, List.<JCTree.JCStatement>of(make.Return(make.Literal(TypeTag.INT, 42)))); //  make.Ident(names.fromString("null")))));

        JCTree.JCMethodDecl synthetic = make.
                MethodDef(make.Modifiers(Flags.PUBLIC | Flags.STATIC),
                        names.fromString("mySynthetic"),
                        make.Type(syms.intType),
                        List.<JCTree.JCTypeParameter>nil(),
                        List.of(make.VarDef(make.Modifiers(Flags.PARAMETER | Flags.MANDATED),
                                names.fromString("name"),
                                make.Type(syms.stringType),
                                null)),
                        List.<JCTree.JCExpression>nil(), // thrown
                        impl,
                        null);

        mwo.owner.defs = mwo.owner.defs.prepend (synthetic);

//        Symbol.ClassSymbol c = mwo.owner.sym;
//        c.completer = MemberEnter.instance(context);

        memberEnter(synthetic, env);

        final Scope scope = mwo.owner.sym.members();
        scope.enter(synthetic.sym);

//        enclScope.enter() // --> MethodEnter.visitMethodDef, line 563
//        Scope.enter, l. 210



        System.out.println(synthetic);
        System.out.println("--");
        System.out.println(mwo.owner);
        System.out.println("--");
        System.out.println(mwo.owner.defs);
        System.out.println("--");
    }

    private void memberEnter(JCTree.JCMethodDecl synthetic, Env classEnv) {
        try {
            final MemberEnter memberEnter = MemberEnter.instance(context);

            final Method reflectMethodForMemberEnter = memberEnter.getClass().getDeclaredMethod("memberEnter", JCTree.class, Env.class);
            reflectMethodForMemberEnter.setAccessible (true);
            reflectMethodForMemberEnter.invoke(memberEnter, synthetic, classEnv);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
