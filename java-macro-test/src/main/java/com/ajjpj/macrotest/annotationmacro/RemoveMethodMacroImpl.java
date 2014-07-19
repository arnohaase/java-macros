package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.support.MModifiersBuilderForClass;
import com.ajjpj.macro.util.AbstractAnnotationMacro;

import java.util.Collections;


/**
 * @author arno
 */
public class RemoveMethodMacroImpl extends AbstractAnnotationMacro {
    @Override public MClassTree transformClass (CompilerContext context, MClassTree tree) {
        for (MMethodTree mtd: tree.getMethods ("removed")) {
            context.treeMaker ().removeMethod (tree, mtd);
        }
        return tree;
    }
}
