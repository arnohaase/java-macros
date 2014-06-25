package com.ajjpj.macro.tree.support;


interface MModifiers {
    MVisibility getVisibility();

    boolean isStatic();
    boolean isFinal();
    boolean isSynchronized();
    boolean isVolatile();
    boolean isTransient();
    boolean isNative();
    boolean isInterface();
    boolean isAbstract();
    boolean isStrictFp();
    boolean isAnnotation();
    boolean isEnum();
}