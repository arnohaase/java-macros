package com.ajjpj.macro.tree.support;


public class MModifiersBuilderForField {
    public MVisibility visibility = MVisibility.Public;

    public boolean isFinal = false;
    public boolean isStatic = false;

    public MModifiers build () {
        return new MModifiersForField (visibility, isFinal, isStatic);
    }

    /**
     * for <em>creating</em> new class instances
     */
    private static class MModifiersForField implements MModifiers {
        private final MVisibility visibility;
        private final boolean isFinal;
        private final boolean isStatic;

        private MModifiersForField (MVisibility visibility, boolean isFinal, boolean isStatic) {
            this.visibility = visibility;
            this.isFinal = isFinal;
            this.isStatic = isStatic;
        }

        @Override public MVisibility getVisibility () {
            return visibility;
        }

        @Override public boolean isFinal () {
            return isFinal;
        }

        @Override public boolean isStatic () {
            return isStatic;
        }

        // not applicable

        @Override public boolean isSynchronized () {
            return false;
        }

        @Override public boolean isAbstract () {
            return false;
        }

        @Override public boolean isStrictFp () {
            return false;
        }

        @Override
        public boolean isVolatile () {
            return false;
        }

        @Override
        public boolean isTransient () {
            return false;
        }

        @Override public boolean isNative () {
            return false;
        }

        @Override public boolean isAnnotation () {
            return false;
        }

        @Override public boolean isEnum () {
            return false;
        }

        @Override public boolean isInterface () {
            return false;
        }
    }
}
