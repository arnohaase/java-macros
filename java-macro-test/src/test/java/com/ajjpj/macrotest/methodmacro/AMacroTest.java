package com.ajjpj.macrotest.methodmacro;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static com.ajjpj.macrotest.methodmacro.AMacro.*;


/**
 * @author arno
 */
public class AMacroTest {
    @Test
    public void testTransform() {
        assertEquals ("transformed Yo", com.ajjpj.macrotest.methodmacro.AMacro.a("Yo"));
        assertEquals ("transformed Hi", AMacro.a("Hi"));

        new Runnable() {
            @Override
            public void run() {
                System.out.println("Moin!");
            }
        }.run();
    }

    @Test
    public void testStaticImport() {
        assertEquals ("transformed Hi", a("Hi"));
    }

    //TODO test named import
    //TODO test star import
}
