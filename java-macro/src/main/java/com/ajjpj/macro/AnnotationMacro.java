package com.ajjpj.macro;

import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MTree;


/**
 * @author arno
 */
public interface AnnotationMacro {
    MTree transformClass(CompilerContext context, MClassTree tree);
//TODO    void transformClass (CompilerContext context, MMethodTree raw);
//TODO    void transformClass (CompilerContext context, MVarDeclTree raw);
//TODO    MExpressionTree transform (CompilerContext context, MExpressionTree tree);
}
