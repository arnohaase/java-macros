package com.ajjpj.macrotest.annotationmacro;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;


/**
 * @author arno
 */
public class ReplaceClassTest {
    @SuppressWarnings ("unused")
    @ReplacePart
    class ReplacedNestedClass {
        public boolean original = true;
    }

    @Test public void testReplaceTopLevelClass () throws ClassNotFoundException, NoSuchFieldException {
        final Class<?> cls = Class.forName ("com.ajjpj.macrotest.annotationmacro.ReplacedTopLevelClass");

        try {
            cls.getDeclaredField ("original");
            fail ("exception expected");
        }
        catch (NoSuchFieldException e) { /**/ }

        // TODO add field 'replacement = true;'
    }

    @Test public void testReplaceNestedClass () throws ClassNotFoundException {
        final Class<?> cls = Class.forName ("com.ajjpj.macrotest.annotationmacro.ReplaceClassTest$ReplacedNestedClass");

        try {
            cls.getDeclaredField ("original");
            fail ("exception expected");
        }
        catch (NoSuchFieldException e) { /**/ }

        // TODO add field 'replacement = true;'
    }
}

@SuppressWarnings ("unused")
@ReplacePart
class ReplacedTopLevelClass {
    public boolean original = true;
}
