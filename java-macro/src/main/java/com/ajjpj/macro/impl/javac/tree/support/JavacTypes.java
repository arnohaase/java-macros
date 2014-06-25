package com.ajjpj.macro.impl.javac.tree.support;

import com.ajjpj.macro.tree.support.MType;
import com.ajjpj.macro.util.MTypes;
import com.sun.tools.javac.code.Symtab;

/**
 * @author arno
 */
public class JavacTypes implements MTypes {
    private final Symtab syms;

    public JavacTypes(Symtab syms) {
        this.syms = syms;
    }

    @Override public MType stringType() {
        return new MJavacType (syms.stringType);
    }
}
