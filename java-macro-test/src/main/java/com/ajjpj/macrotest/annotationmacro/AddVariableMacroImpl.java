package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiersBuilderForClass;
import com.ajjpj.macro.tree.support.MModifiersBuilderForField;
import com.ajjpj.macro.util.AbstractAnnotationMacro;

import java.util.Collections;


/**
 * @author arno
 */
public class AddVariableMacroImpl extends AbstractAnnotationMacro {
    @Override public MClassTree transformClass (CompilerContext context, MClassTree tree) {
        final MModifiersBuilderForField mods = new MModifiersBuilderForField ();
        mods.isStatic = true;

        MVariableDeclTree var = context.treeMaker ().Variable (
                "addedVar",
                context.types ().intType (),
                mods.build (),
                context.treeMaker ().Literal (42)
        );

        context.treeMaker ().addVariable (tree, var);
        return tree;
    }
}
