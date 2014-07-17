package com.ajjpj.macro.impl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * This interface encapsulates a macro processor implementation for a specific compiler implementation
 *
 * @author arno
 */
public interface MacroProcessor {
    boolean canHandle (ProcessingEnvironment env);

    void init (ProcessingEnvironment env);
    void process (Element rootElement);
}
