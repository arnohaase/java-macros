package com.ajjpj.macro.impl.javac.tree.support;

import com.ajjpj.macro.tree.support.MType;
import com.ajjpj.macro.util.MTypes;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

/**
 * @author arno
 */
public class MJavacTypes implements MTypes {
    private final Symtab syms;
    private final ClassReader classes;
    private final Names names;

    public MJavacTypes (Context context) {
        this.syms = Symtab.instance (context);
        this.classes = ClassReader.instance (context);
        this.names = Names.instance (context);
    }

    @Override public MJavacType stringType() {
        return new MJavacType (syms.stringType);
    }

    @Override public MType fromFqn (String fqn) {
        return new MJavacType(classes.enterClass (names.fromString (fqn)).type);
    }
}
