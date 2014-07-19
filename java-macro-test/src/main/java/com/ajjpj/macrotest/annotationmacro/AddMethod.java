package com.ajjpj.macrotest.annotationmacro;

import com.ajjpj.macro.AnnotationMacroMarker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author arno
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationMacroMarker(AddMethodMacroImpl.class)
public @interface AddMethod {
}
