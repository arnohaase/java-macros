package com.ajjpj.macro.impl;

import com.ajjpj.macro.impl.methodmacro.MacroMethodInvoker;
import com.ajjpj.macro.impl.methodmacro.SyntheticMethodMacroPlaceholderInserter;
import com.sun.source.util.Trees;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author arno
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MacroAnnotationProcessor extends AbstractProcessor {
    private Trees trees;
    private Context context;
    private MemberEnter memberEnter;

    private ClassLoader macroClassLoader;

    @Override public synchronized void init (ProcessingEnvironment env) {
        super.init (env);

        macroClassLoader = ((JavacProcessingEnvironment) env).getProcessorClassLoader();

        trees = Trees.instance(env);

        context = ((JavacProcessingEnvironment) env).getContext();
        memberEnter = MemberEnter.instance(context);

//        JavaFileManager jfm = context.get(JavaFileManager.class);
//        System.out.println("has annotation processor path: " + jfm.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH));
//        final ClassLoader cl = jfm.getClassLoader(StandardLocation.CLASS_PATH);

//        try {
//            System.out.println(" --> " + cl.loadClass("com.ajjpj.macrotest.SmartString").getName());
//        } catch (ClassNotFoundException e) {
//            System.out.println(" --> SmartString not on " + cl);
////            e.printStackTrace();
//        }

//        System.out.println("----------------------");
//        dumpCl("getClass()", getClass().getClassLoader());
//        dumpCl("procEnv", macroClassLoader);
//        dumpCl("class path", cl);
//
//        System.out.println("======================================================");
    }

    private void dumpCl(String name, ClassLoader cl) {
        System.out.println("class loader " + name + " " + hasSmartString(cl));

        while(cl != null) {
            System.out.println("  " + cl);
            cl = cl.getParent();
        }
    }

    private boolean hasSmartString(ClassLoader cl) {
        try {
            cl.loadClass("com.ajjpj.macrotest.SmartString");
            return true;
        }
        catch(Exception exc) {
            return false;
        }
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

            tree.accept (new SyntheticMethodMacroPlaceholderInserter(context));
            tree.accept (new MacroMethodInvoker (macroClassLoader, context));
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
