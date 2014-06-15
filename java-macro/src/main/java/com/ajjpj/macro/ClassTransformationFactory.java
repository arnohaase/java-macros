package com.ajjpj.macro;

import java.lang.annotation.Annotation;

/**
 * @author arno
 */
public interface ClassTransformationFactory {
    ClassTransformation create (Annotation annotation);
}
