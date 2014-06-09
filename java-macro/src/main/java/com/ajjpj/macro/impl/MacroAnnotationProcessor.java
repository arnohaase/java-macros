package com.ajjpj.macro.impl;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author arno
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MacroAnnotationProcessor extends AbstractProcessor {
    private Trees trees;
    private TreeMaker make;
    private Names names;
    private Context context;
    private Enter enter;
    private MemberEnter memberEnter;

    @Override public synchronized void init (ProcessingEnvironment env) {
        super.init (env);

        trees = Trees.instance(env);

        context = ((JavacProcessingEnvironment) env).getContext();
        make = TreeMaker.instance(context);
        names = Names.instance (context);
        enter = Enter.instance(context);
        memberEnter = MemberEnter.instance(context);
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(roundEnv.processingOver()) {
            return false;
        }

        for(Element rootEl: roundEnv.getRootElements()) {
            if(rootEl.getKind() != ElementKind.CLASS) {
                continue;
            }

            final JCTree.JCClassDecl tree = (JCTree.JCClassDecl) trees.getTree(rootEl);

 


//            final JCTree.JCStatement stmt = make.Throw(
//                    make.NewClass(
//                            null,
//                            null,
//                            make.Ident (names.fromString (UnsupportedOperationException.class.getSimpleName())),
//                            List.nil(),
//                            null));
//
//            final JCTree.JCBlock impl =
//                    make.Block (
//                            0,
//                            List.of(stmt));
//
//            final Symtab syms = Symtab.instance(context);
//
//            final JCTree.JCMethodDecl synthetic = make.
//                    MethodDef(make.Modifiers(Flags.PUBLIC | Flags.STATIC),
//                            names.fromString("mySynthetic"),
//                            make.Type(syms.intType),
//                            List.<JCTree.JCTypeParameter>nil(),
//                            List.of(make.VarDef(make.Modifiers(Flags.PARAMETER | Flags.MANDATED),
//                                    names.fromString("name"),
//                                    make.Type(syms.stringType),
//                                    null)),
//                            List.<JCTree.JCExpression>nil(), // thrown
//                            impl,
//                            null);
//
//            tree.defs = tree.defs.prepend (synthetic);
//            memberEnter (synthetic, enter.getEnv (tree.sym)); //TODO optimization: set flag in 'enter'?
//
//            System.out.println(tree);



            tree.accept(new SyntheticMethodMacroBridgeInserter(context, tree));
        }

        return false;
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

}
