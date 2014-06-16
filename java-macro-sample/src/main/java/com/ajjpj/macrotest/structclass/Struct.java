package com.ajjpj.macrotest.structclass;

import com.ajjpj.macro.ClassMacro;

import java.lang.annotation.*;

/**
 * @author arno
 */
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
@ClassMacro (StructClassTransformationFactory.class)
public @interface Struct {
}
