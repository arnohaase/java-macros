package com.ajjpj.macro.impl;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MExpressionTree;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public class CompilerContextImpl implements CompilerContext {
    private final ClassLoader classLoader;
    private final Context context;

    /**
     * @param classLoader the processor class loader
     */
    public CompilerContextImpl(ClassLoader classLoader, Context context) {
        this.classLoader = classLoader;
        this.context = context;
    }

    @Override public Context getContext() {
        return context;
    }

    @Override
    public void msg(String msg) {

    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public MExpressionTree parseExpression(String expr) {
        return null;
    }

    //    public ClassLoader getClassLoader() {
//        return classLoader;
//    }
//
//    public Context getContext() {
//        return context;
//    }
//
//    @Override public void log(String msg) {
//        System.out.println("LOG: " + msg);
//    }
}
