package com.ajjpj.macro.util;

import com.ajjpj.macro.AnnotationMacro;
import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MTree;

/**
 * This is a convenience superclass for an annotation macro, delegating all specific
 *
 * @author arno
 */
public class AbstractAnnotationMacro implements AnnotationMacro {
    @Override public MClassTree transformClass(CompilerContext context, MClassTree tree) {
        return (MClassTree) transformGeneric (context, tree);
    }

//    @Override public void transform (CompilerContext context, MExpressionTree tree) {
//        transformGeneric (context, tree);
//    }

    public MTree transformGeneric (CompilerContext context, MTree tree) {
        throw new UnsupportedOperationException(); //TODO error handling
    }
}
