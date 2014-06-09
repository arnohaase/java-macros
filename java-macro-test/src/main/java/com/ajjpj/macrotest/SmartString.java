package com.ajjpj.macrotest;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.ExpressionTree;
import com.ajjpj.macro.MethodMacro;


/**
 * @author arno
 */
public class SmartString {
    @MethodMacro public static ExpressionTree<String> s (CompilerContext ctx, ExpressionTree<String> string) {
        return string;
    }
}
