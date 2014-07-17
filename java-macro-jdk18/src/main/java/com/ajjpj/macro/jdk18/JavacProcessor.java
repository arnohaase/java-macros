package com.ajjpj.macro.jdk18;

import com.ajjpj.macro.impl.MacroProcessor;
import com.ajjpj.macro.impl.shared.annotationmacro.AnnotationCache;
import com.ajjpj.macro.jdk18.methodmacro.MacroMethodInvoker;
import com.ajjpj.macro.jdk18.methodmacro.SyntheticMethodMacroPlaceholderInserter;
import com.sun.source.util.Trees;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;


/**
 * @author arno
 */
public class JavacProcessor implements MacroProcessor {
    private Trees trees;
    private Context context;
    private Enter enter;

    private ClassLoader macroClassLoader;
    private AnnotationCache annotationCache;


    @Override public boolean canHandle (ProcessingEnvironment env) {
        if (! (env instanceof JavacProcessingEnvironment)) {
            return false;
        }

        return true; //TODO check javac version
    }

    @Override public void process (Element rootElement) {
        final JCTree.JCClassDecl tree = (JCTree.JCClassDecl) trees.getTree (rootElement);
        final JCTree.JCCompilationUnit compilationUnit = enter.getEnv (tree.sym).toplevel;

        tree.accept (new AnnotationMacroInvoker (context, annotationCache, compilationUnit));

        tree.accept (new SyntheticMethodMacroPlaceholderInserter (context));
        tree.accept (new MacroMethodInvoker (macroClassLoader, context, compilationUnit));
    }

    @Override public synchronized void init (ProcessingEnvironment env) {
        macroClassLoader = ((JavacProcessingEnvironment) env).getProcessorClassLoader ();
        annotationCache = new AnnotationCache (macroClassLoader);

        context = ((JavacProcessingEnvironment) env).getContext ();

        trees = Trees.instance (env);
        enter = Enter.instance (context);
    }
}
