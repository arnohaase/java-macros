package com.ajjpj.macro;

import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MTree;


/**
 * @author arno
 */
public interface AnnotationMacro { //TODO return a tree --> allow substitution
    MTree transformClass(CompilerContext context, MClassTree tree);
//    void transformClass (CompilerContext context, MMethodTree raw);
//    void transformClass (CompilerContext context, MVarDeclTree raw);
//    MExpressionTree transform (CompilerContext context, MExpressionTree tree); //TODO implement invocation; test this
}
