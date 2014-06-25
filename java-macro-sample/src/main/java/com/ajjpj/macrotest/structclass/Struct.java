package com.ajjpj.macrotest.structclass;

import com.ajjpj.macro.AnnotationMacroMarker;

import java.lang.annotation.*;

/**
 * @author arno
 */
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
@AnnotationMacroMarker (StructMacroImpl.class)
public @interface Struct {
}
