package com.ajjpj.macro.samplemacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.MacroMethod;
import com.ajjpj.macro.TypedExpressionTree;

/**
 * @author arno
 */
public class SampleMacros {
    @MacroMethod public static TypedExpressionTree<String> s(CompilerContext ctx, TypedExpressionTree<String> inputString) {
        System.out.println("processing " + inputString);
        return inputString;
    }

    public static void asdf(String abc) {
    }
}
