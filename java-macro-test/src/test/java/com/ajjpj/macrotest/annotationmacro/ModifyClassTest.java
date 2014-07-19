package com.ajjpj.macrotest.annotationmacro;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author arno
 */
public class ModifyClassTest {
    @AddMethod class NestedWithAddedMethod {}
    @RemoveMethod class NestedWithRemovedMethod {void removed() {}}

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
        fail ("TODO");
    }

    @Test public void testRemoveAttribute () {
        fail ("TODO");
    }
}

@AddMethod class TopLevelWithAddedMethod {}
@RemoveMethod class TopLevelWithRemovedMethod {void removed() {}}

