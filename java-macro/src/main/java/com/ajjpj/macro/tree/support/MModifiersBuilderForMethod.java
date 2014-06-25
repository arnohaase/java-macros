package com.ajjpj.macro.tree.support;


public class MModifiersBuilderForMethod {
    public MVisibility visibility = MVisibility.Public;

    public boolean isFinal = false;
    public boolean isStatic = false;
    public boolean isSynchronized = false;
    public boolean isAbstract = false;
    public boolean isStrictFp = false;

    public MModifiers build() {
        return new MModifiersForMethod(visibility, isFinal, isStatic, isSynchronized, isAbstract, isStrictFp);
    }

    /**
     * for <em>creating</em> new class instances
     */
    private static class MModifiersForMethod implements MModifiers {
        private final MVisibility visibility;
        private final boolean isFinal;
        private final boolean isStatic;
        private final boolean isSynchronized;
        private final boolean isAbstract;
        private final boolean isStrictFp;

        private MModifiersForMethod(MVisibility visibility, boolean isFinal, boolean isStatic, boolean isSynchronized, boolean isAbstract, boolean isStrictFp) {
            this.visibility = visibility;
            this.isFinal = isFinal;
            this.isStatic = isStatic;
            this.isSynchronized = isSynchronized;
            this.isAbstract = isAbstract;
            this.isStrictFp = isStrictFp;
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

        @Override public boolean isSynchronized() {
            return isSynchronized;
        }

        @Override public boolean isAbstract() {
            return isAbstract;
        }

        @Override public boolean isStrictFp() {
            return isStrictFp;
        }

        // not applicable

        @Override
        public boolean isVolatile() {
            return false;
        }

        @Override
        public boolean isTransient() {
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
    }
}
