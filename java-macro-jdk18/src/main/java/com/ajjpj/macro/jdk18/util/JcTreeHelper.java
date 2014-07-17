package com.ajjpj.macro.jdk18.util;

import com.ajjpj.macro.impl.util.ReflectionHelper;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.tree.JCTree;

import java.lang.reflect.Field;

/**
 * @author arno
 */
public class JcTreeHelper {
    private static final Field scopeField = ReflectionHelper.getField (AttrContext.class, "scope");

    public static Scope getScope(JCTree.JCClassDecl classDecl, Enter enter) {
        final AttrContext attrContext = enter.getEnv (classDecl.sym).info;
        return ReflectionHelper.get(scopeField, attrContext);
    }
}
