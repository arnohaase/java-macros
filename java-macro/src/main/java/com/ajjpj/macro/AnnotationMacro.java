package com.ajjpj.macro;

import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MExpressionTree;


/**
 * @author arno
 */
public interface AnnotationMacro { //TODO return a tree --> allow substitution
    void transform (CompilerContext context, MClassTree tree);
//    void transform (CompilerContext context, MMethodTree raw);
//    void transform (CompilerContext context, MVarDeclTree raw);
    void transform (CompilerContext context, MExpressionTree tree); //TODO implement invocation; test this
}
