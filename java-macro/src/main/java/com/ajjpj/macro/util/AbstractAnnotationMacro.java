package com.ajjpj.macro.util;

import com.ajjpj.macro.AnnotationMacro;
import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MTree;

/**
 * This is a convenience superclass for an annotation macro, delegating all specific
 *
 * @author arno
 */
public class AbstractAnnotationMacro implements AnnotationMacro {
    @Override public void transform (CompilerContext context, MClassTree tree) {
        transformGeneric (context, tree);
    }

    @Override public void transform (CompilerContext context, MExpressionTree tree) {
        transformGeneric (context, tree);
    }

    public void transformGeneric (CompilerContext context, MTree tree) {
        throw new UnsupportedOperationException(); //TODO error handling
    }
}
