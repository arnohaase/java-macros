package com.ajjpj.macro.impl.javac.util;

import com.sun.tools.javac.util.List;

import java.util.Objects;

/**
 * @author arno
 */
public class ListHelper {
    public static <T> List<T> without (List<T> list, T toBeRemoved) {
        List<T> result = List.nil();

        for (T el: list) {
            if (!Objects.equals (el, toBeRemoved)) {
                result = result.prepend(el);
            }
        }
        return result.reverse();
    }
}
