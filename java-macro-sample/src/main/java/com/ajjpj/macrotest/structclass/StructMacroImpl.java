package com.ajjpj.macrotest.structclass;

import com.ajjpj.macro.CompilerContext;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.support.MModifiersBuilderForMethod;
import com.ajjpj.macro.util.AbstractAnnotationMacro;
import com.ajjpj.macro.util.MTreeMaker;

import java.util.Collections;


/**
 * @author arno
 */
public class StructMacroImpl extends AbstractAnnotationMacro {

    @Override public MClassTree transformClass(CompilerContext context, MClassTree cls) {
        addToStringMethod (context, cls);
        return cls;
    }

    private void addToStringMethod (CompilerContext context, MClassTree cls) {
        final MTreeMaker make = context.treeMaker();

        final MStatementTree stmt = make.parseStatement("return getClass().getSimpleName() + \" [\" + super.toString() + ']';");
        final MBlockTree body = make.Block (stmt);

        final MModifiersBuilderForMethod modifiers = new MModifiersBuilderForMethod();

        final MMethodTree mtd = make.ConcreteMethod("toString", context.types().fromFqn("java.lang.String"), modifiers.build(), Collections.emptyList(), body);

        make.addMethod (cls, mtd);
    }
}
