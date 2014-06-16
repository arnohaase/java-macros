package com.ajjpj.macrotest.structclass;

import com.ajjpj.macro.ClassTransformation;
import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.impl.util.MethodBuilder;
import com.ajjpj.macro.tree.MClassTree;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;


/**
 * @author arno
 */
public class StructClassTransformation implements ClassTransformation {
    @Override
    public void transform (CompilerContext context, MClassTree raw) {
        addToStringMethod(context.getContext(), (JCTree.JCClassDecl) raw.getInternalRepresentation());
    }

    private void addToStringMethod (Context context, JCTree.JCClassDecl cls) {
        final Names names = Names.instance (context);
        final Symtab syms = Symtab.instance (context);

        final TreeMaker make = TreeMaker.instance (context);
        final ParserFactory parserFactory = ParserFactory.instance (context);

        final JCTree.JCStatement stmt = parserFactory.newParser ("return getClass().getSimpleName() + \" [\" + super.toString() + ']';", false, false, false).parseStatement();

        final JCTree.JCBlock body = make.Block (0, List.of (stmt));

        final MethodBuilder methodBuilder = new MethodBuilder(context, names.fromString("toString"), syms.stringType, body);

        methodBuilder.buildIntoClass (cls);
    }
}
