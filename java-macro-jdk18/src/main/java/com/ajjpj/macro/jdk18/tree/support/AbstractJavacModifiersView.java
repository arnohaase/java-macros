package com.ajjpj.macro.jdk18.tree.support;

import com.ajjpj.macro.tree.support.MModifiers;
import com.ajjpj.macro.tree.support.MVisibility;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class AbstractJavacModifiersView implements MModifiers {
    private final JCTree.JCModifiers inner;

    public AbstractJavacModifiersView(JCTree.JCModifiers inner) {
        this.inner = inner;
    }

    @Override public MVisibility getVisibility() {
        if((inner.flags & Flags.PUBLIC) != 0) {
            return MVisibility.Public;
        }
        if((inner.flags & Flags.PRIVATE) != 0) {
            return MVisibility.Private;
        }
        if((inner.flags & Flags.PROTECTED) != 0) {
            return MVisibility.Protected;
        }

        return MVisibility.Package;
    }

    @Override public boolean isStatic() {
        return (inner.flags & Flags.STATIC) != 0;
    }

    @Override public boolean isFinal() {
        return (inner.flags & Flags.FINAL) != 0;
    }

    @Override public boolean isSynchronized() {
        return (inner.flags & Flags.SYNCHRONIZED) != 0;
    }

    @Override public boolean isVolatile() {
        return (inner.flags & Flags.VOLATILE) != 0;
    }

    @Override public boolean isTransient() {
        return (inner.flags & Flags.TRANSIENT) != 0;
    }

    @Override public boolean isNative() {
        return (inner.flags & Flags.NATIVE) != 0;
    }

    @Override public boolean isInterface() {
        return (inner.flags & Flags.INTERFACE) != 0;
    }

    @Override public boolean isAbstract() {
        return (inner.flags & Flags.ABSTRACT) != 0;
    }

    @Override public boolean isStrictFp() {
        return (inner.flags & Flags.STRICTFP) != 0;
    }

    @Override public boolean isAnnotation() {
        return (inner.flags & Flags.ANNOTATION) != 0;
    }

    @Override public boolean isEnum() {
        return (inner.flags & Flags.ENUM) != 0;
    }
}
