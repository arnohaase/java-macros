package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MTree;
import com.ajjpj.macro.util.AbstractAnnotationMacro;

/**
 * @author arno
 */
public class RemovePartMacroImpl extends AbstractAnnotationMacro {
    @Override public MTree transformGeneric(CompilerContext context, MTree tree) {
        return null;
    }
}
