package com.ajjpj.macro.impl;

import com.ajjpj.macro.MethodMacro;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import java.lang.reflect.Method;


/**
 * @author arno
 */
class SyntheticMethodMacroBridgeInserter extends TreeTranslator {
    private final TreeMaker make;
    private final Names names;
    private final Enter enter;
    private final MemberEnter memberEnter;
    private final Symtab syms;
    private final JCTree.JCClassDecl classDecl;

    private List<JCTree.JCMethodDecl> macroMethods = List.nil();

    SyntheticMethodMacroBridgeInserter (Context context, JCTree.JCClassDecl classDecl) {
        this.classDecl = classDecl;
        make = TreeMaker.instance (context);
        names = Names.instance (context);
        enter = Enter.instance (context);
        memberEnter = MemberEnter.instance (context);
        syms = Symtab.instance (context);
    }

    @Override public void visitClassDef(JCTree.JCClassDecl tree) {
        final List<JCTree.JCMethodDecl> prevList = macroMethods;

        try {
            macroMethods = List.nil();
            super.visitClassDef(tree);

            for(JCTree.JCMethodDecl mtd: macroMethods) {
                createSyntheticBridge (mtd);
            }
        }
        finally {
            macroMethods = prevList;
        }
    }

    @Override public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
        result = jcMethodDecl; // shortcut - no need for further recursive descent

        if (isMethodMacro (jcMethodDecl)) {
            macroMethods = macroMethods.prepend (jcMethodDecl);
        }
    }

    private void createSyntheticBridge (JCTree.JCMethodDecl macroMethod) {
        final JCTree.JCStatement stmt = make.Throw(
                make.NewClass(
                        null,
                        null,
                        make.Ident (names.fromString (UnsupportedOperationException.class.getSimpleName())),
                        List.nil(),
                        null));

        final JCTree.JCBlock impl =
                make.Block (
                        0,
                        List.of(stmt));

        final JCTree.JCMethodDecl synthetic = make.
                MethodDef(make.Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.SYNTHETIC),
                        macroMethod.name,
                        make.Type(syms.intType),
                        List.<JCTree.JCTypeParameter>nil(),
                        List.of(make.VarDef(make.Modifiers(Flags.PARAMETER | Flags.MANDATED),
                                names.fromString("name"),
                                make.Type(syms.stringType),
                                null)),
                        List.<JCTree.JCExpression>nil(), // thrown
                        impl,
                        null);

        classDecl.defs = classDecl.defs.prepend (synthetic);
        memberEnter(synthetic, enter.getEnv(classDecl.sym)); //TODO optimization: set flag in 'enter'?
    }

    private void memberEnter(JCTree.JCMethodDecl synthetic, Env classEnv) {
        try {
            final Method reflectMethodForMemberEnter = memberEnter.getClass().getDeclaredMethod ("memberEnter", JCTree.class, Env.class);
            reflectMethodForMemberEnter.setAccessible (true);
            reflectMethodForMemberEnter.invoke(memberEnter, synthetic, classEnv);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean isMethodMacro(JCTree.JCMethodDecl mtd) {
        if (! hasMacroMethodAnnotation (mtd)) {
            return false;
        }

        //TODO verify signature, static, visibility, top-level class

        return true;
    }

    private boolean hasMacroMethodAnnotation(JCTree.JCMethodDecl tree) {
        final JCTree.JCModifiers modifiers = tree.getModifiers();
        if(modifiers == null) {
            return false;
        }

        for(JCTree.JCAnnotation annotation: modifiers.getAnnotations()) {
            final JCTree tpe = annotation.annotationType;
            if(tpe instanceof JCTree.JCIdent) {
                final JCTree.JCIdent ident = (JCTree.JCIdent) tpe;
                final String annotName = ident.sym.toString();

                if(MethodMacro.class.getName().equals(annotName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
