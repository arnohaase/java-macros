package com.ajjpj.macrotest.annotationmacro;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author arno
 */
public class ModifyClassTest {
    @AddMethod class NestedWithAddedMethod {}
    @RemoveMethod class NestedWithRemovedMethod {void removed() {}}
    @AddVariable static class NestedWithAddedVariable {}
    @RemoveVariable class NestedWithRemovedVariable {int removed = 42;}

    @Test public void testAddMethod () {
        assertEquals (42, new NestedWithAddedMethod().addedMethod ());
        assertEquals (42, new TopLevelWithAddedMethod ().addedMethod ());
    }

    @Test public void testRemoveMethod () {
        try {
            NestedWithRemovedMethod.class.getDeclaredMethod ("removed");
            fail ("exception expected");
        }
        catch (NoSuchMethodException e) { /**/ }

        try {
            TopLevelWithRemovedMethod.class.getDeclaredMethod ("removed");
            fail ("exception expected");
        }
        catch (NoSuchMethodException e) { /**/ }
    }

    @Test public void testAddAttribute () {
        assertEquals (42, NestedWithAddedVariable.addedVar);
        assertEquals (42, TopLevelWithAddedVariable.addedVar);
    }

    @Test public void testRemoveAttribute () {
        try {
            NestedWithRemovedVariable.class.getDeclaredField ("removed");
            fail ("exception expected");
        }
        catch (NoSuchFieldException e) { /**/ }

        try {
            TopLevelWithRemovedVariable.class.getDeclaredField ("removed");
            fail ("exception expected");
        }
        catch (NoSuchFieldException e) { /**/ }
    }
}

@AddMethod class TopLevelWithAddedMethod {}
@RemoveMethod class TopLevelWithRemovedMethod {void removed() {}}
@AddVariable class TopLevelWithAddedVariable {}
@RemoveVariable class TopLevelWithRemovedVariable {int removed = 42;}

