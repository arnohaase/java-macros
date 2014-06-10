package com.ajjpj.macrotest;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author arno
 */
public class SmartStringTest {
    @Test
    public void testSimple() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Method m: SmartString.class.getMethods()) {
            for(Annotation a: m.getAnnotations()) {
                System.out.print(a + " ");
            }
            System.out.println(m);
        }

        try {
            SmartString.s ("abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
