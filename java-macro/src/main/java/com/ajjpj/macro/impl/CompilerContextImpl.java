package com.ajjpj.macro.impl;

import com.ajjpj.macro.CompilerContext;

/**
 * @author arno
 */
public class CompilerContextImpl implements CompilerContext {
    private final ClassLoader classLoader;

    public CompilerContextImpl(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override public void log(String msg) {
        System.out.println("LOG: " + msg);
    }
}
