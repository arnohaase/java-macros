package com.ajjpj.macrotest.structclass;

import com.ajjpj.macro.ClassTransformation;
import com.ajjpj.macro.ClassTransformationFactory;

import java.lang.annotation.Annotation;

/**
 * @author arno
 */
public class StructClassTransformationFactory implements ClassTransformationFactory {
    @Override public ClassTransformation create (Annotation annotation) {
        return new StructClassTransformation();
    }
}
