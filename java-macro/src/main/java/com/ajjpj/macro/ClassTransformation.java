package com.ajjpj.macro;

import com.ajjpj.macro.tree.ClassTree;


/**
 * @author arno
 */
public interface ClassTransformation {
    void transform (CompilerContext context, ClassTree raw);
}
