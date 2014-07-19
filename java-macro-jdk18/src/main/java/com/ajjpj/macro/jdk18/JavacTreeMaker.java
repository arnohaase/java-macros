package com.ajjpj.macro.jdk18;

import com.ajjpj.macro.jdk18.tree.JavacMethodTree;
import com.ajjpj.macro.jdk18.tree.MJavacClassTree;
import com.ajjpj.macro.jdk18.tree.expr.MJavacBinaryExpression;
import com.ajjpj.macro.jdk18.tree.expr.MJavacLiteralExpression;
import com.ajjpj.macro.jdk18.tree.stmt.MJavacBlockStatement;
import com.ajjpj.macro.jdk18.tree.stmt.MJavacVariableDeclStatement;
import com.ajjpj.macro.jdk18.tree.support.MJavacType;
import com.ajjpj.macro.jdk18.tree.support.WrapperFactory;
import com.ajjpj.macro.jdk18.util.ListHelper;
import com.ajjpj.macro.jdk18.util.SourcePosSetter;
import com.ajjpj.macro.tree.MClassTree;
import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MMethodTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.expr.BinaryOperator;
import com.ajjpj.macro.tree.expr.MBinaryExpressionTree;
import com.ajjpj.macro.tree.expr.MLiteralTree;
import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiers;
import com.ajjpj.macro.tree.support.MType;
import com.ajjpj.macro.util.MTreeMaker;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author arno
 */
class JavacTreeMaker implements MTreeMaker {
    private final TreeMaker make;
    private final ParserFactory parserFactory;
    private final Names names;
    private final Enter enter;
    private final MemberEnter memberEnter;

    JavacTreeMaker(Context context) {
        make = TreeMaker.instance (context);
        parserFactory = ParserFactory.instance (context);
        names = Names.instance(context);
        enter = Enter.instance(context);
        memberEnter = MemberEnter.instance(context);
    }

    @Override public MExpressionTree parseExpression (String expr) {
        return WrapperFactory.wrap (parserFactory.newParser (expr, false, false, false).parseExpression ());
    }

    @Override public MStatementTree parseStatement (String stmt) {
        return WrapperFactory.wrap (parserFactory.newParser(stmt, false, false, false).parseStatement()); //TODO apply 'partial Enter'
    }

    @Override public void addMethod (MClassTree cls, MMethodTree method) {
        final JCTree.JCClassDecl jcClassTree = (JCTree.JCClassDecl) cls.getInternalRepresentation();
        final JCTree.JCMethodDecl mtd = ((JavacMethodTree) method).getInternalRepresentation();

        new SourcePosSetter (jcClassTree.pos).scan(mtd);

        jcClassTree.defs = jcClassTree.defs.prepend (mtd);
        memberEnter (mtd, enter.getEnv (jcClassTree.sym)); //TODO optimization: set flag in 'enter'?
    }

    @Override public void removeMethod (MClassTree cls, MMethodTree method) {
        final JCTree.JCClassDecl jcClassTree = (JCTree.JCClassDecl) cls.getInternalRepresentation();
        final JCTree.JCMethodDecl mtd = ((JavacMethodTree) method).getInternalRepresentation();

        jcClassTree.defs = ListHelper.without (jcClassTree.defs, mtd);
    }

    @Override public void addVariable (MClassTree cls, MVariableDeclTree variable) {
        final JCTree.JCClassDecl jcClassTree = (JCTree.JCClassDecl) cls.getInternalRepresentation();
        final JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) variable.getInternalRepresentation();

        new SourcePosSetter (jcClassTree.pos).scan (var);

        jcClassTree.defs = jcClassTree.defs.prepend (var);
        memberEnter (var, enter.getEnv (jcClassTree.sym));
    }

    @Override public void removeVariable (MClassTree cls, MVariableDeclTree variable) {
        final JCTree.JCClassDecl jcClassTree = (JCTree.JCClassDecl) cls.getInternalRepresentation();
        final JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) variable.getInternalRepresentation();

        jcClassTree.defs = ListHelper.without (jcClassTree.defs, var);
    }

    private void memberEnter(JCTree syntheticMember, Env classEnv) {
        try {
            final Method reflectMethodForMemberEnter = memberEnter.getClass().getDeclaredMethod ("memberEnter", JCTree.class, Env.class);
            reflectMethodForMemberEnter.setAccessible (true);
            reflectMethodForMemberEnter.invoke(memberEnter, syntheticMember, classEnv);
        }
        catch (Exception e) {
            throw new RuntimeException(e); //TODO error handling
        }
    }


    @Override public MClassTree Class (String name, MModifiers modifiers) {
        final JCTree.JCClassDecl inner = make.ClassDef (
                make.Modifiers (extractFlags (modifiers), com.sun.tools.javac.util.List.nil () /* TODO annotations */ ),
                names.fromString (name),
                com.sun.tools.javac.util.List.<JCTree.JCTypeParameter> nil (), /* TODO type parameters */
                null, /* TODO */
                com.sun.tools.javac.util.List.<JCTree.JCExpression> nil(), /* TODO */
                com.sun.tools.javac.util.List.<JCTree> nil() /* TODO */
                );

        return new MJavacClassTree (inner);
    }

    //TODO annotations
    @Override public MVariableDeclTree Variable (String name, MType type, MModifiers modifiers, MExpressionTree<?> init) {
        final JCTree.JCVariableDecl result = make.VarDef (
                make.Modifiers (extractFlags (modifiers), com.sun.tools.javac.util.List.nil ()),
                names.fromString (name),
                make.Type (((MJavacType) type).getInternalRepresentation ()),
                (JCTree.JCExpression) init.getInternalRepresentation ()
        );

        return new MJavacVariableDeclStatement (result);
    }


    //TODO parameters
    //TODO annotations
    @Override public MMethodTree ConcreteMethod(String name, MType returnType, MModifiers modifiers, List<MVariableDeclTree> parameters, MBlockTree body) {
        final MJavacType javacReturnType = (MJavacType) returnType;
        final MJavacBlockStatement javacBody = (MJavacBlockStatement) body;

        final JCTree.JCMethodDecl synthetic = make.
                MethodDef(make.Modifiers(extractFlags(modifiers), com.sun.tools.javac.util.List.nil() /* TODO annotations */ ),
                        names.fromString(name),
                        make.Type(javacReturnType.getInternalRepresentation()),
                        com.sun.tools.javac.util.List.nil(), // TODO type params
                        com.sun.tools.javac.util.List.nil(), // TODO params.toList(),
                        com.sun.tools.javac.util.List.nil(), // TODO thrown
                        javacBody.getInternalRepresentation(),
                        null);


        return new JavacMethodTree(synthetic);
    }

    private long extractFlags(MModifiers modifiers) {
        long result = 0;

        switch(modifiers.getVisibility()) {
            case Public: result |= Flags.PUBLIC; break;
            case Private: result |= Flags.PRIVATE; break;
            case Protected: result |= Flags.PROTECTED; break;
        }
        if(modifiers.isStatic()) result |= Flags.STATIC;
        if(modifiers.isAbstract()) result |= Flags.ABSTRACT;
        if(modifiers.isAnnotation()) result |= Flags.ANNOTATION;
        if(modifiers.isEnum()) result |= Flags.ENUM;
        if(modifiers.isFinal()) result |= Flags.FINAL;
        if(modifiers.isInterface()) result |= Flags.INTERFACE;
        if(modifiers.isNative()) result |= Flags.NATIVE;
        if(modifiers.isStrictFp()) result |= Flags.STRICTFP;
        if(modifiers.isSynchronized()) result |= Flags.SYNCHRONIZED;
        if(modifiers.isTransient()) result |= Flags.TRANSIENT;
        if(modifiers.isVolatile()) result |= Flags.VOLATILE;

        return result;
    }

    @Override public MBlockTree Block (MStatementTree... statements) {
        com.sun.tools.javac.util.List<JCTree.JCStatement> stmts = com.sun.tools.javac.util.List.nil();

        for(int i=statements.length - 1; i >= 0; i--) {
            stmts = stmts.prepend((JCTree.JCStatement) statements[i].getInternalRepresentation());
        }

        return new MJavacBlockStatement (make.Block(0, stmts));
    }

    @Override public MBinaryExpressionTree BinaryExpression(MExpressionTree left, MExpressionTree right, BinaryOperator op) {
        final JCTree.Tag tag = JCTree.Tag.valueOf (op.name());
        final JCTree.JCBinary inner = make.Binary (tag,
                (JCTree.JCExpression) left.getInternalRepresentation(),
                (JCTree.JCExpression) right.getInternalRepresentation());

        return new MJavacBinaryExpression (inner);
    }

    @Override public MLiteralTree Literal (Object value) {
        final JCTree.JCLiteral inner = value != null ? make.Literal (value) : make.Literal (TypeTag.BOT, null);
        return new MJavacLiteralExpression (inner);
    }
}

//TODO what is a good time to apply 'PartialEnter' to newly created nodes?