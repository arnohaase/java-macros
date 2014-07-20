package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.MTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiersBuilderForClass;
import com.ajjpj.macro.util.AbstractAnnotationMacro;

import java.util.Collections;


/**
 * @author arno
 */
public class AddMethodMacroImpl extends AbstractAnnotationMacro {
    @Override public MClassTree transformClass (CompilerContext context, MClassTree tree) {
        final MBlockTree body = context.treeMaker ().Block (
                context.treeMaker ().parseStatement ("return 42;")
        );

        final MMethodTree method = context.treeMaker ().ConcreteMethod (
                "addedMethod",
                context.types ().intType (),
                new MModifiersBuilderForClass ().build (),
                Collections.<MVariableDeclTree> emptyList (),
                body);

        context.treeMaker ().addMethod (tree, method);
        return tree;
    }
}
