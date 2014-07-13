package com.ajjpj.macrotest.annotationmacro;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author arno
 */
public class RemovePartTest {
    @RemovePart
    public static class RemovedNestedClass {
    }

    @Test
    public void testRemoveNestedClass() {
        try {
            Class.forName ("com.ajjpj.macrotest.annotationmacro.RemovePartTest$RemovedNestedClass");
            fail("exception expected");
        }
        catch (ClassNotFoundException exc) { /**/ }
    }

    @Test
    public void testRemoveTopLevelClass() {
        try {
            Class.forName ("com.ajjpj.macrotest.annotationmacro.RemovedTopLevelClass");
            fail("exception expected");
        }
        catch (ClassNotFoundException exc) { /**/ }
    }
}

@RemovePart
class RemovedTopLevelClass {
}