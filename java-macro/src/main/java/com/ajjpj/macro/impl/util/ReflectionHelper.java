package com.ajjpj.macro.impl.util;

import java.lang.reflect.Field;

/**
 * @author arno
 */
public class ReflectionHelper {
    public static Field getField (Class<?> cls, String name) {
        try {
            final Field result = cls.getDeclaredField (name);
            result.setAccessible (true);
            return result;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException (e); //TODO error handling
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get (Field f, Object o) {
        try {
            return (T) f.get (o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException (e); //TODO error handling
        }
    }
}
