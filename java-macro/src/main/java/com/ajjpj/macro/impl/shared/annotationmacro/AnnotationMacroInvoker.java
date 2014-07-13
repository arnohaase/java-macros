package com.ajjpj.macro.impl.shared.annotationmacro;

import com.ajjpj.macro.AnnotationMacro;
import com.ajjpj.macro.impl.javac.JavacCompilerContext;
import com.ajjpj.macro.impl.tree.ClassTreeImpl;
import com.ajjpj.macro.impl.util.TypeHelper;
import com.ajjpj.macro.tree.MClassTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class AnnotationMacroInvoker extends TreeTranslator {
    private final ClassLoader cl;
    private final Context context;
    private final JCTree.JCCompilationUnit compilationUnit;

    private final AnnotationCache annotationCache;
    private final TypeHelper typeHelper;

    public AnnotationMacroInvoker(ClassLoader cl, Context context, AnnotationCache annotationCache, JCTree.JCCompilationUnit compilationUnit) {
        this.cl = cl;
        this.context = context;
        this.annotationCache = annotationCache;
        this.typeHelper = new TypeHelper (context);
        this.compilationUnit = compilationUnit;
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        final JCTree.JCModifiers mods = jcClassDecl.getModifiers();
        if (mods != null) {

            for(JCTree.JCAnnotation annot: mods.getAnnotations()) {
                final MClassTree tree = new ClassTreeImpl (jcClassDecl);
                final AnnotationMacro macro = annotationCache.getMacro (typeHelper.getAnnotationFqn (annot));

                if (macro != null) {
                    //TODO allow replacing the class decl? How to do that with annotation processor API?
//                    final JCTree.JCClassDecl transformed = (JCTree.JCClassDecl) macro.transformClass (new JavacCompilerContext(context, compilationUnit), tree).getInternalRepresentation();
//                    if (transformed )
                }
            }
        }

        super.visitClassDef (jcClassDecl);
    }
}


//TODO test everything when annotations are specified with their FQN
