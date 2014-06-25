package com.ajjpj.macro.tree.support;


public class MModifiersBuilderForAttribute {
    public MVisibility visibility = MVisibility.Public;

    public boolean isFinal = false;
    public boolean isStatic = false;
    public boolean isVolatile = false;
    public boolean isTransient = false;

    public MModifiers build() {
        return new MModifiersForAttribute(visibility, isFinal, isStatic, isVolatile, isTransient);
    }

    /**
     * for <em>creating</em> new class instances
     */
    private static class MModifiersForAttribute implements MModifiers {
        private final MVisibility visibility;
        private final boolean isFinal;
        private final boolean isStatic;
        private final boolean isVolatile;
        private final boolean isTransient;

        private MModifiersForAttribute(MVisibility visibility, boolean isFinal, boolean isStatic, boolean isVolatile, boolean isTransient) {
            this.visibility = visibility;
            this.isFinal = isFinal;
            this.isStatic = isStatic;
            this.isVolatile = isVolatile;
            this.isTransient = isTransient;
        }

        @Override public MVisibility getVisibility() {
            return visibility;
        }

        @Override public boolean isFinal() {
            return isFinal;
        }

        @Override public boolean isStatic() {
            return isStatic;
        }

        @Override public boolean isVolatile() {
            return isVolatile;
        }

        @Override public boolean isTransient() {
            return isTransient;
        }

        // not applicable

        @Override public boolean isSynchronized() {
            return false;
        }

        @Override public boolean isNative() {
            return false;
        }

        @Override public boolean isAnnotation() {
            return false;
        }

        @Override public boolean isEnum() {
            return false;
        }

        @Override public boolean isInterface() {
            return false;
        }

        @Override public boolean isAbstract() {
            return false;
        }

        @Override public boolean isStrictFp() {
            return false;
        }
    }
}
