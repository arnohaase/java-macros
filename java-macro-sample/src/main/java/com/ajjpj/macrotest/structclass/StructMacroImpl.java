package com.ajjpj.macrotest.structclass;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.impl.util.MethodBuilder;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.support.MModifiersBuilderForMethod;
import com.ajjpj.macro.util.AbstractAnnotationMacro;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import java.lang.reflect.Modifier;
import java.util.Collections;


/**
 * @author arno
 */
public class StructMacroImpl extends AbstractAnnotationMacro {

    @Override public void transform (CompilerContext context, MClassTree raw) {
        addToStringMethod(context.getContext(), (JCTree.JCClassDecl) raw.getInternalRepresentation());
    }

    private MMethodTree createToStringMethod (CompilerContext context) {
        final MTreeMaker make = context.treeMaker();

        final MStatementTree stmt = make.parseStatement("return getClass().getSimpleName() + \" [\" + super.toString() + ']';");
        final MBlockTree body = make.Block (stmt);

        final MModifiersBuilderForMethod modifiers = new MModifiersBuilderForMethod();

        return make.ConcreteMethod("toString", context.types().stringType(), modifiers.build(), Collections.emptyList(), body);
    }

    private void addToStringMethod (Context contextRaw, JCTree.JCClassDecl cls) {
        final Names names = Names.instance (contextRaw);
        final Symtab syms = Symtab.instance (contextRaw);

        final TreeMaker make = TreeMaker.instance (contextRaw);
        final ParserFactory parserFactory = ParserFactory.instance (contextRaw);

        final JCTree.JCStatement stmt = parserFactory.newParser ("return getClass().getSimpleName() + \" [\" + super.toString() + ']';", false, false, false).parseStatement();

        final JCTree.JCBlock body = make.Block (0, List.of (stmt));

        final MethodBuilder methodBuilder = new MethodBuilder(contextRaw, names.fromString("toString"), syms.stringType, body);

        methodBuilder.buildIntoClass (cls, cls.pos);
    }
}
