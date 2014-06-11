package com.ajjpj.macro;

import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public interface CompilerContext {
    Context getContext();

    void log(String msg);
}
