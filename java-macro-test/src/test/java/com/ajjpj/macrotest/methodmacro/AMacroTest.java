package com.ajjpj.macrotest.methodmacro;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.ajjpj.macrotest.methodmacro.AMacro.*;


/**
 * @author arno
 */
public class AMacroTest {
    @Test
    public void testTransform() {
        assertEquals ("transformed Hi", AMacro.a("Hi"));
        assertEquals ("transformed Yo", com.ajjpj.macrotest.methodmacro.AMacro.a("Yo"));
    }

    @Test
    public void testStaticImport() {
        assertEquals ("transformed Hi", a("Hi"));
    }
}
