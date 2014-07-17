package com.ajjpj.macro.jdk18.methodmacro;

import com.ajjpj.macro.impl.shared.methodmacro.MethodMacroPlaceholder;
import com.ajjpj.macro.jdk18.util.MethodBuilder;
import com.ajjpj.macro.jdk18.util.TypeHelper;
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
public class SyntheticMethodMacroPlaceholderInserter extends TreeTranslator {
    private final Context context;
    private final TreeMaker make;
    private final Names names;
    private final TypeHelper typeHelper;

    private JCTree.JCClassDecl classDecl;
    private List<JCTree.JCMethodDecl> macroMethods = List.nil();

    public SyntheticMethodMacroPlaceholderInserter(Context context) {
        this.context = context;
        make = TreeMaker.instance (context);
        names = Names.instance (context);
        typeHelper = new TypeHelper (context);
    }

    @Override public void visitClassDef(JCTree.JCClassDecl tree) {
        final List<JCTree.JCMethodDecl> prevList = macroMethods;
        final JCTree.JCClassDecl prevClass = classDecl;

        try {
            classDecl = tree;
            macroMethods = List.nil();
            super.visitClassDef(tree);

            for(JCTree.JCMethodDecl mtd: macroMethods) {
                createSyntheticBridge (mtd);
            }
        }
        finally {
            classDecl = prevClass;
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

//        new TreeDumper().scan(macroMethod);

        final JCTree.JCStatement stmt = make.Throw( //TODO add explaining text along the lines of 'compiled without macro support'
                make.NewClass(
                        null,
                        null,
                        make.Ident (names.fromString (UnsupportedOperationException.class.getSimpleName())),
                        List.<JCTree.JCExpression> nil(),
                        null));

        final JCTree.JCBlock impl =
                make.Block (
                        0,
                        List.of(stmt));

        final MethodBuilder methodBuilder = new MethodBuilder (context, macroMethod.name, returnType, impl);
        methodBuilder.setFlags(Flags.PUBLIC | Flags.STATIC);
        methodBuilder.addAnnotation (MethodMacroPlaceholder.class);

        for(JCTree.JCVariableDecl origParam: macroMethod.getParameters().tail) {
            //TODO error handling; make this more robust
            methodBuilder.addParam (origParam.name, origParam.getType().type.getTypeArguments().head);
        }

        methodBuilder.buildIntoClass (classDecl, macroMethod.pos);
    }



    boolean isMethodMacro(JCTree.JCMethodDecl mtd) {
        if (! typeHelper.hasAnnotation (mtd, "com.ajjpj.macro.MethodMacro")) {
            return false;
        }

        //TODO verify signature, static, visibility, top-level class

        return true;
    }
}
