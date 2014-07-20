package com.ajjpj.macro.ecj;

import com.ajjpj.macro.impl.MacroProcessor;
import org.eclipse.jdt.internal.compiler.apt.dispatch.BaseProcessingEnvImpl;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;


/**
 * @author arno
 */
public class EcjProcessor implements MacroProcessor {
    @Override public boolean canHandle (ProcessingEnvironment env) {
        return env instanceof BaseProcessingEnvImpl;
    }

    @Override public void init (ProcessingEnvironment env) {

    }

    @Override public void process (Element rootElement) {

    }
}
