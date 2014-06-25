package com.ajjpj.macro.tree.support;


public class MModifiersBuilderForClass {
    public MVisibility visibility = MVisibility.Public;

    public boolean isFinal = false;
    public boolean isInterface = false;
    public boolean isAbstract = false;
    public boolean isStrictFp = false;

    public MModifiers build() {
        return new MModifiersForClass(visibility, isFinal, isInterface, isAbstract, isStrictFp);
    }


    /**
     * for <em>creating</em> new class instances
     */
    private static class MModifiersForClass implements MModifiers {
        private final MVisibility visibility;
        private final boolean isFinal;
        private final boolean isInterface;
        private final boolean isAbstract;
        private final boolean isStrictFp;

        private MModifiersForClass(MVisibility visibility, boolean isFinal, boolean isInterface, boolean isAbstract, boolean isStrictFp) {
            this.visibility = visibility;
            this.isFinal = isFinal;
            this.isInterface = isInterface;
            this.isAbstract = isAbstract;
            this.isStrictFp = isStrictFp;
        }

        @Override public MVisibility getVisibility() {
            return visibility;
        }

        @Override public boolean isFinal() {
            return isFinal;
        }

        @Override public boolean isInterface() {
            return isInterface;
        }

        @Override public boolean isAbstract() {
            return isAbstract;
        }

        @Override public boolean isStrictFp() {
            return isStrictFp;
        }

        // not applicable

        @Override public boolean isStatic() {
            return false;
        }

        @Override public boolean isSynchronized() {
            return false;
        }

        @Override public boolean isVolatile() {
            return false;
        }

        @Override public boolean isTransient() {
            return false;
        }

        @Override public boolean isNative() {
            return false;
        }

        @Override public boolean isAnnotation() { //TODO builders for enums, annotations
            return false;
        }

        @Override public boolean isEnum() {
            return false;
        }
    }
}
