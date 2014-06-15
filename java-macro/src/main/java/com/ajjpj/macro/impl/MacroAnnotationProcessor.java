package com.ajjpj.macro.impl;

import com.ajjpj.macro.impl.classmacro.AnnotationCache;
import com.ajjpj.macro.impl.classmacro.ClassMacroInvoker;
import com.ajjpj.macro.impl.methodmacro.MacroMethodInvoker;
import com.ajjpj.macro.impl.methodmacro.SyntheticMethodMacroPlaceholderInserter;
import com.sun.source.util.Trees;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
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
    private AnnotationCache annotationCache;

    @Override public synchronized void init (ProcessingEnvironment env) {
        super.init (env);

        macroClassLoader = ((JavacProcessingEnvironment) env).getProcessorClassLoader();
        annotationCache = new AnnotationCache (macroClassLoader);

        trees = Trees.instance(env);

        context = ((JavacProcessingEnvironment) env).getContext();
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

            tree.accept (new ClassMacroInvoker (macroClassLoader, context, annotationCache));

            tree.accept (new SyntheticMethodMacroPlaceholderInserter(context));
            tree.accept (new MacroMethodInvoker (macroClassLoader, context));
        }

        return false;
    }
}
