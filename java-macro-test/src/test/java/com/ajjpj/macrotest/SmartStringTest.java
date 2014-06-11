package com.ajjpj.macrotest;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * @author arno
 */
public class SmartStringTest {
    @Test
    public void testSimple() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String now = new Date().toString();

        System.out.println ((String) SmartString.s ("The current time is ${now}!"));
        System.out.println ((String) com.ajjpj.macrotest.SmartString.s ("... or as a time stamp: ${System.currentTimeMillis()}."));
    }
}
