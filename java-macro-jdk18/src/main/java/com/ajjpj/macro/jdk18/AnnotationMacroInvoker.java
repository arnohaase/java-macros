package com.ajjpj.macro.jdk18;

import com.ajjpj.macro.AnnotationMacro;
import com.ajjpj.macro.impl.shared.annotationmacro.AnnotationCache;
import com.ajjpj.macro.jdk18.tree.MJavacClassTree;
import com.ajjpj.macro.jdk18.util.JcTreeHelper;
import com.ajjpj.macro.jdk18.util.ListHelper;
import com.ajjpj.macro.jdk18.util.TypeHelper;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class AnnotationMacroInvoker extends TreeScanner {
    private final Context context;
    private final Enter enter;
    private final JCTree.JCCompilationUnit compilationUnit;

    private final AnnotationCache annotationCache;
    private final TypeHelper typeHelper;

    public AnnotationMacroInvoker (Context context, AnnotationCache annotationCache, JCTree.JCCompilationUnit compilationUnit) {
        this.context = context;
        this.enter = Enter.instance (context);
        this.annotationCache = annotationCache;
        this.typeHelper = new TypeHelper (context);
        this.compilationUnit = compilationUnit;
    }

    @Override
    public void visitClassDef (final JCTree.JCClassDecl jcClassDecl) {
        final JCTree.JCModifiers mods = jcClassDecl.getModifiers();

        if (mods != null) {
            for(JCTree.JCAnnotation annot: mods.getAnnotations()) {
                final MClassTree tree = new MJavacClassTree(jcClassDecl);
                final AnnotationMacro macro = annotationCache.getMacro (typeHelper.getAnnotationFqn (annot));

                if (macro != null) {
                    final MTree transformedRaw = macro.transformClass (new JavacCompilerContext(context, compilationUnit), tree);
                    if (transformedRaw == null || transformedRaw.getInternalRepresentation() == null) {
                        final JCTree parent = (JCTree) TreePath.getPath(compilationUnit, jcClassDecl).getParentPath().getLeaf(); //TODO is there a more efficient way to do this? jcClassDecl.sym.owner, but how to get the corresponding JCTree?!

                        if (parent instanceof JCTree.JCCompilationUnit) {
                            final JCTree.JCCompilationUnit parentCompilationUnit = (JCTree.JCCompilationUnit) parent;
                            parentCompilationUnit.defs = ListHelper.without(parentCompilationUnit.defs, jcClassDecl);

                            //TODO un-enter?
                        }
                        else if (parent instanceof JCTree.JCClassDecl) {
                            final JCTree.JCClassDecl parentClass = (JCTree.JCClassDecl) parent;

                            parentClass.defs = ListHelper.without (parentClass.defs, jcClassDecl);

                            final Scope parentScope = JcTreeHelper.getScope(jcClassDecl, enter);
                            parentScope.remove (jcClassDecl.sym);
                        }
                        else {
                            throw new IllegalStateException("unsupported owner of class declaration: " + parent.getClass().getName()); //TODO log.error
                        }
                    }
                    else {
                        final JCTree.JCClassDecl transformed = (JCTree.JCClassDecl) transformedRaw.getInternalRepresentation();
                        if (transformed != jcClassDecl) {
                            //TODO replace; enter; cancel loop of previous annotation macros
                        }
                    }
                }
            }
        }

        super.visitClassDef (jcClassDecl);
    }

    //TODO annotation macros for methods,
}


//TODO test everything when annotations are specified with their FQN
