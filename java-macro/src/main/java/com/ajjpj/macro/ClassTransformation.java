package com.ajjpj.macro;

import com.ajjpj.macro.tree.MClassTree;


/**
 * @author arno
 */
public interface ClassTransformation {
    void transform (CompilerContext context, MClassTree raw);
}
