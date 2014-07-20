package com.ajjpj.macro.jdk18.annotationmacro;

import com.ajjpj.macro.AnnotationMacro;
import com.ajjpj.macro.impl.shared.annotationmacro.AnnotationCache;
import com.ajjpj.macro.jdk18.JavacCompilerContext;
import com.ajjpj.macro.jdk18.tree.MJavacClassTree;
import com.ajjpj.macro.jdk18.util.JcTreeHelper;
import com.ajjpj.macro.jdk18.util.ListHelper;
import com.ajjpj.macro.jdk18.util.TypeHelper;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.Context;

import java.lang.reflect.Method;
import java.util.Objects;


/**
 * @author arno
 */
public class AnnotationMacroInvoker extends TreeScanner {
    private final Context context;
    private final Enter enter;
    private final Check check;
    private final JCTree.JCCompilationUnit compilationUnit;

    private final AnnotationCache annotationCache;
    private final TypeHelper typeHelper;

    public AnnotationMacroInvoker (Context context, AnnotationCache annotationCache, JCTree.JCCompilationUnit compilationUnit) {
        this.context = context;
        this.enter = Enter.instance (context);
        this.check = Check.instance (context);
        this.annotationCache = annotationCache;
        this.typeHelper = new TypeHelper (context);
        this.compilationUnit = compilationUnit;
    }

    @Override
    public void visitClassDef (final JCTree.JCClassDecl jcClassDecl) {
        final JCTree.JCModifiers mods = jcClassDecl.getModifiers ();

        if (mods != null) {
            for (JCTree.JCAnnotation annot : mods.getAnnotations ()) {
                final MClassTree tree = new MJavacClassTree (compilationUnit, jcClassDecl);
                final AnnotationMacro macro = annotationCache.getMacro (typeHelper.getAnnotationFqn (annot));

                if (macro != null) {
                    final MTree transformedRaw = macro.transformClass (new JavacCompilerContext (context, compilationUnit), tree);
                    final JCTree.JCClassDecl transformed = transformedRaw != null ? (JCTree.JCClassDecl) transformedRaw.getInternalRepresentation () : null;

                    if (transformed != jcClassDecl) {
                        final JCTree parent = (JCTree) TreePath.getPath (compilationUnit, jcClassDecl).getParentPath ().getLeaf (); //TODO is there a more efficient way to do this? jcClassDecl.sym.owner, but how to get the corresponding JCTree?!

                        removeClassDecl (parent, jcClassDecl);

                        if (transformed != null) {
                            addClassDecl (parent, transformed);
                        }

                        // cancel the loop through the original transformation annotations  TODO test this
                        break;
                    }
                }
            }
        }

        super.visitClassDef (jcClassDecl);
    }

    private void addClassDecl (JCTree parent, JCTree.JCClassDecl jcClassDecl) {
        if (parent instanceof JCTree.JCCompilationUnit) {
            final JCTree.JCCompilationUnit parentCompilationUnit = (JCTree.JCCompilationUnit) parent;
            parentCompilationUnit.defs = parentCompilationUnit.defs.prepend (jcClassDecl);

            try {
                final Method classEnter = Enter.class.getDeclaredMethod ("classEnter", JCTree.class, Env.class); //TODO extract to field
                classEnter.setAccessible (true);
                classEnter.invoke (enter, jcClassDecl, enter.getTopLevelEnv (parentCompilationUnit));
            }
            catch (Exception e) {
                throw new RuntimeException (e); //TODO error handling
            }
        }
        else if (parent instanceof JCTree.JCClassDecl) {
            final JCTree.JCClassDecl parentClass = (JCTree.JCClassDecl) parent;

            parentClass.defs = parentClass.defs.prepend (jcClassDecl);
//
//            final Scope parentScope = JcTreeHelper.getScope(jcClassDecl, enter);
//            parentScope.remove (jcClassDecl.sym);
        }
        else {
            throw new IllegalStateException ("unsupported owner of class declaration: " + parent.getClass ().getName ()); //TODO log.error
        }
    }

    private void removeClassDecl (JCTree parent, JCTree.JCClassDecl jcClassDecl) {
        check.compiled.remove (jcClassDecl.sym.flatname);

        if (parent instanceof JCTree.JCCompilationUnit) {
            final JCTree.JCCompilationUnit parentCompilationUnit = (JCTree.JCCompilationUnit) parent;
            parentCompilationUnit.defs = ListHelper.without (parentCompilationUnit.defs, jcClassDecl);

            //TODO un-enter?
        }
        else if (parent instanceof JCTree.JCClassDecl) {
            final JCTree.JCClassDecl parentClass = (JCTree.JCClassDecl) parent;

            parentClass.defs = ListHelper.without (parentClass.defs, jcClassDecl);

            final Scope parentScope = JcTreeHelper.getScope (jcClassDecl, enter);
            parentScope.remove (jcClassDecl.sym);
        }
        else {
            throw new IllegalStateException ("unsupported owner of class declaration: " + parent.getClass ().getName ()); //TODO log.error
        }
    }

    //TODO annotation macros for methods,
}


//TODO test everything when annotations are specified with their FQN
