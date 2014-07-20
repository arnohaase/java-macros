package com.ajjpj.macro.tree.support;

import javax.tools.JavaFileObject;


/**
 * @author arno
 */
public interface MSourcePosition {
    JavaFileObject getSourceFile();
    int getLine();
    int getColumn();
}
