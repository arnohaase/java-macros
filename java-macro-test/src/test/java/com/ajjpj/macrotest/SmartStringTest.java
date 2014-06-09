package com.ajjpj.macrotest;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author arno
 */
public class SmartStringTest {
    @Test
    public void testSimple() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Method m: SmartString.class.getMethods()) {
            System.out.println(m);
        }

        try {
            SmartString.s ("abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
