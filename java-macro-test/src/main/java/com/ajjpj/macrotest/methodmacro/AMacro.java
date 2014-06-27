package com.ajjpj.macrotest.methodmacro;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.MethodMacro;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.util.MTreeMaker;

/**
 * @author arno
 */
public class AMacro {
    @MethodMacro public static MExpressionTree<String> a (CompilerContext ctx, MExpressionTree<String> expr) {
        ctx.msg (expr, "transforming");

        final MTreeMaker make = ctx.treeMaker();

        return make.BinaryExpression (make.Literal("transformed "), expr, BinaryOperator.PLUS);
    }
}
