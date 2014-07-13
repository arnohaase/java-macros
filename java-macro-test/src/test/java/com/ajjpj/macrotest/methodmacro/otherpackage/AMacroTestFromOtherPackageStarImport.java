package com.ajjpj.macrotest.methodmacro.otherpackage;

import com.ajjpj.macrotest.methodmacro.*;
import org.junit.Test;

import static com.ajjpj.macrotest.methodmacro.AMacro.*;
import static org.junit.Assert.*;

/**
 * @author arno
 */
public class AMacroTestFromOtherPackageStarImport {
    @Test
    public void testTransform() {
        assertEquals ("transformed Yo", AMacro.a("Yo"));
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
