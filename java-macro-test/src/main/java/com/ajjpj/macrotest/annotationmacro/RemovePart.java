package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.AnnotationMacroMarker;
import com.ajjpj.macrotest.structclass.StructMacroImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author arno
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationMacroMarker(RemovePartMacroImpl.class)
public @interface RemovePart {
}
