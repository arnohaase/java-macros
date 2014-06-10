package com.ajjpj.macrotest;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.ajjpj.macrotest.SmartString.s;

/**
 * @author arno
 */
public class SmartStringTest {
    @Test
    public void testSimple() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            System.out.println ((String) SmartString.s ("abc"));
            System.out.println ((String) com.ajjpj.macrotest.SmartString.s ("xyz"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
