package com.ajjpj.macro.impl.shared.classmacro;

import com.ajjpj.macro.ClassTransformationFactory;
import com.ajjpj.macro.impl.CompilerContextImpl;
import com.ajjpj.macro.impl.tree.ClassTreeImpl;
import com.ajjpj.macro.impl.util.TypeHelper;
import com.ajjpj.macro.tree.MClassTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class ClassMacroInvoker extends TreeTranslator {
    private final ClassLoader cl;
    private final Context context;

    private final AnnotationCache annotationCache;
    private final TypeHelper typeHelper;

    public ClassMacroInvoker (ClassLoader cl, Context context, AnnotationCache annotationCache) {
        this.cl = cl;
        this.context = context;
        this.annotationCache = annotationCache;
        this.typeHelper = new TypeHelper (context);
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        final JCTree.JCModifiers mods = jcClassDecl.getModifiers();
        if (mods != null) {
            final MClassTree tree = new ClassTreeImpl (jcClassDecl);

            for(JCTree.JCAnnotation annot: mods.getAnnotations()) {
                final ClassTransformationFactory factory = annotationCache.getFactory(typeHelper.getAnnotationFqn(annot));

                if (factory != null) {
                    factory.create (null).transform (new CompilerContextImpl (cl, context), tree); //TODO allow replacing the class decl? How to do that with annotation processor API?
                }
            }
        }

        super.visitClassDef (jcClassDecl);
    }
}


//TODO test everything when annotations are specified with their FQN
