package com.ajjpj.macro.impl;

import com.ajjpj.macro.MethodMacro;
import com.ajjpj.macro.impl.util.MethodBuilder;
import com.ajjpj.macro.impl.util.TreeDumper;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;


/**
 * @author arno
 */
class SyntheticMethodMacroBridgeInserter extends TreeTranslator {
    private final Context context;
    private final TreeMaker make;
    private final Names names;
    private final JCTree.JCClassDecl classDecl;

    private List<JCTree.JCMethodDecl> macroMethods = List.nil();

    SyntheticMethodMacroBridgeInserter (Context context, JCTree.JCClassDecl classDecl) {
        this.context = context;
        this.classDecl = classDecl;
        make = TreeMaker.instance (context);
        names = Names.instance (context);
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
        final Type returnType = macroMethod.restype.type.getTypeArguments().head; // TODO make this more robust; error handling

        new TreeDumper().scan(macroMethod);

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

        final MethodBuilder methodBuilder = new MethodBuilder (context, macroMethod.name, returnType, impl);
        methodBuilder.setFlags(Flags.PUBLIC | Flags.STATIC);
        methodBuilder.addAnnotation (MethodMacroBridge.class);

        for(JCTree.JCVariableDecl origParam: macroMethod.getParameters().tail) {
            //TODO error handling; make this more robust
            methodBuilder.addParam (origParam.name, origParam.getType().type.getTypeArguments().head);
        }

        methodBuilder.buildIntoClass(classDecl);
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
