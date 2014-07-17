package com.ajjpj.macro.impl;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ServiceLoader;
import java.util.Set;


/**
 * @author arno
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GenericMacroAnnotationProcessorImpl extends AbstractProcessor {
    private MacroProcessor macroProcessor;

    @Override public synchronized void init (ProcessingEnvironment env) {
        super.init (env);

        // actual processors are presumably available via the same class loader that loaded this class
        for (MacroProcessor candidate: ServiceLoader.load (MacroProcessor.class, getClass ().getClassLoader ())) {
            if (candidate.canHandle(env)) {
                this.macroProcessor = candidate;
                this.macroProcessor.init (env);
                return;
            }
        }

        throw new RuntimeException ("no macro processor for this compiler"); //TODO error handling
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(roundEnv.processingOver()) {
            return false;
        }

        for(Element rootEl: roundEnv.getRootElements()) {
            if(rootEl.getKind() != ElementKind.CLASS) {
                continue;
            }

            macroProcessor.process(rootEl);
        }

        return false;
    }
}
