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



//@interface ClassMacro2 {
//    Class<? extends StructClassTransformationFactory> value();
//    Include[] includes() default {};
//}
//
//@interface Include {
//    Class<? extends Annotation> value();
//    String[] params() default {};
//}
//
//@ClassMacro2 (StructClassTransformationFactory.class)
//@interface Hash {
//    int prim() default 29;
//}
//
//@ClassMacro2 (value = StructClassTransformationFactory.class,
//        includes = {@Include (value = Hash.class, params = {"transform(prime2)"})})
//@interface ArnosStruct {
//    int prim2();
//}
//
