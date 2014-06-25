package com.ajjpj.macro.tree.support;


public class MModifiersBuilderForParameter {
    public boolean isFinal = false;

    public MModifiers build() {
        return new MModifiersForParameter(isFinal);
    }

    /**
     * for <em>creating</em> new class instances
     */
    private static class MModifiersForParameter implements MModifiers {
        private final boolean isFinal;

        private MModifiersForParameter(boolean isFinal) {
            this.isFinal = isFinal;
        }

        @Override public boolean isFinal() {
            return isFinal;
        }

        // not applicable


        @Override public MVisibility getVisibility() {
            return MVisibility.Package;
        }

        @Override public boolean isStatic() {
            return false;
        }

        @Override public boolean isVolatile() {
            return false;
        }

        @Override public boolean isTransient() {
            return false;
        }

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
