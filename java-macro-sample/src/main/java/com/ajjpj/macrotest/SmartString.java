package com.ajjpj.macrotest;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.MethodMacro;
import com.ajjpj.macro.impl.tree.ExpressionTreeImpl;
import com.ajjpj.macro.tree.ExpressionTree;
import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;


/**
 * @author arno
 */
public class SmartString {
    @MethodMacro public static ExpressionTree<String> s (CompilerContext ctx, ExpressionTree<String> string) {

        //TODO error reporting if the expression that was passed in is not a literal
        String remainder = (String) ((JCTree.JCLiteral) string.getInternalRepresentation()).getValue(); //TODO make this more robust; error reporting
        if(remainder == null) {
            return string;
        }

        final TreeMaker make = TreeMaker.instance (ctx.getContext());
        final ParserFactory parserFactory = ParserFactory.instance (ctx.getContext());

        JCTree.JCExpression result = make.Literal("");

        while (! remainder.isEmpty()) {
            final int exprStart = remainder.indexOf("${");
            switch (exprStart) {
                case -1: // no expressions left --> use the entire string
                    result = make.Binary (JCTree.Tag.PLUS, result, make.Literal (remainder));
                    remainder = "";
                    break;
                case 0:
                    final int exprEnd = remainder.indexOf ('}');
                    final String exprString = remainder.substring(2, exprEnd);

                    //TODO keep source ref
                    final JCTree.JCExpression expr = parserFactory.newParser (exprString, false, false, false).parseExpression();

                    //TODO enter the expr (?)

                    result = make.Binary (JCTree.Tag.PLUS, result, expr);
                    remainder = remainder.substring (exprEnd + 1);
                    break;
                default:
                    result = make.Binary (JCTree.Tag.PLUS, result, make.Literal (remainder.substring (0, exprStart)));
                    remainder = remainder.substring (exprStart);
                    break;
            }
        }

        return new ExpressionTreeImpl<> (result);
    }

}
