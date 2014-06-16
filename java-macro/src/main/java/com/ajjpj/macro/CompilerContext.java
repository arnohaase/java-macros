package com.ajjpj.macro;

import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MExpressionTree;
import com.sun.tools.javac.util.Context;

/**
 * @author arno
 */
public interface CompilerContext {
    Context getContext(); //TODO remove this

    void msg (String msg); //TODO code position
    void warn (String msg); //TODO code position
    void error (String msg); //TODO code position

    MExpressionTree parseExpression (String expr);

//        void addClass (MClassTree classTree);
}
