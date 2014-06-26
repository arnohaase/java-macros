package com.ajjpj.macro.impl.javac.tree.support;

import com.ajjpj.macro.util.MTypes;
import com.sun.tools.javac.code.Symtab;

/**
 * @author arno
 */
public class MJavacTypes implements MTypes {
    private final Symtab syms;

    public MJavacTypes (Symtab syms) {
        this.syms = syms;
    }

    @Override public MJavacType stringType() {
        return new MJavacType (syms.stringType);
    }
}
