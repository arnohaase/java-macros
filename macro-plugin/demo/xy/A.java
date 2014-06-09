package xy;

import com.ajjpj.macro.*;

import java.lang.UnsupportedOperationException;

@DemoMacroAnnotation
class A {
    @MacroMethod
    public static ExpressionTree<String> s (CompilerContext ctx, ExpressionTree<String> stringParam) {
        throw new UnsupportedOperationException();
//        return stringParam;
    }
}