package com.ajjpj.macro.impl.javac.tree.support;

import com.ajjpj.macro.tree.support.MType;
import com.sun.tools.javac.code.Type;
/**
 * @author arno
 */
public class MJavacType implements MType {
    private final Type type;

    public MJavacType(Type type) {
        this.type = type;
    }

    @Override public Object getInternalRepresentation() {
        return type;
    }
}
