package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.util.AbstractAnnotationMacro;


/**
 * @author arno
 */
public class RemoveVariableMacroImpl extends AbstractAnnotationMacro {
    @Override public MClassTree transformClass (CompilerContext context, MClassTree tree) {
        context.treeMaker ().removeVariable (tree, tree.getVariable ("removed"));
        return tree;
    }
}
