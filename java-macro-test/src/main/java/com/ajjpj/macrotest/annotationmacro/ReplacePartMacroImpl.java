package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.util.AbstractAnnotationMacro;


/**
 * @author arno
 */
public class ReplacePartMacroImpl extends AbstractAnnotationMacro {
    @Override public MClassTree transformClass (CompilerContext context, MClassTree tree) {
        return context.treeMaker ().Class (tree.getName(), tree.getModifiers()); //TODO add field 'boolean original = false'
    }
}
