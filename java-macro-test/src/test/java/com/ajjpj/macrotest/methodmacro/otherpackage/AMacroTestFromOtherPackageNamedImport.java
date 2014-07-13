package com.ajjpj.macrotest.methodmacro.otherpackage;

import com.ajjpj.macrotest.methodmacro.AMacro;
import org.junit.Test;

import static com.ajjpj.macrotest.methodmacro.AMacro.a;
import static org.junit.Assert.assertEquals;

/**
 * @author arno
 */
public class AMacroTestFromOtherPackageNamedImport {
    @Test
    public void testTransform() {
        assertEquals ("transformed Yo", com.ajjpj.macrotest.methodmacro.AMacro.a("Yo"));
        assertEquals ("transformed Hi", AMacro.a("Hi"));
    }

    @Test
    public void testAnonymousLocalClass() {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("Hi!");
            }
        }.run();
    }

    @Test
    public void testStaticImport() {
        assertEquals ("transformed Hi", a("Hi"));
    }

}
