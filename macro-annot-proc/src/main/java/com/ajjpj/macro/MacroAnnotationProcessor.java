package com.ajjpj.macro;

import com.sun.source.util.Trees;
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

    @Override public synchronized void init (ProcessingEnvironment env) {
        super.init (env);

        trees = Trees.instance(env);

        final Context context = ((JavacProcessingEnvironment) env).getContext();
        make = TreeMaker.instance (context);
        names = Names.instance (context);
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(roundEnv.processingOver()) {
            return false;
        }

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Hi!!!");

        for(Element rootEl: roundEnv.getRootElements()) {
            if(rootEl.getKind() != ElementKind.CLASS) {
                continue;
            }

            final JCTree tree = (JCTree) trees.getTree(rootEl);
             
            tree.accept(new TreeTranslator() {
                @Override public void visitMethodDef (JCTree.JCMethodDecl mtd) {
                    final JCTree.JCStatement stmt = make.Throw(
                            make.NewClass(
                                    null,
                                    null,
                                    make.Ident(names.fromString(RuntimeException.class.getName())),
                                    List.nil(),
                                    null));

                    final JCTree.JCBlock impl =
                            make.Block (
                                    0,
                                    List.of(stmt));

                    mtd.body.stats = mtd.body.stats.prepend (stmt);

                    result = mtd;
                }
            });
        }

        return false;
    }
}