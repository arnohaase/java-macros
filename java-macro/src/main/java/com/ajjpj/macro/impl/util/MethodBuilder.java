package com.ajjpj.macro.impl.util;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;

import java.lang.reflect.Method;

/**
 * @author arno
 */
public class MethodBuilder {
    private final TreeMaker make;
    private final Names names;
    private final Enter enter;
    private final MemberEnter memberEnter;

    private long flags = Flags.PUBLIC | Flags.SYNTHETIC;
    private List<JCTree.JCAnnotation> annotations = List.nil();

    private final Name name;
    private final JCTree.JCExpression returnType;
    private final JCTree.JCBlock body;

    private ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();

    public MethodBuilder (Context context, Name name, Type returnType, JCTree.JCBlock body) {
        this.make = TreeMaker.instance (context);
        this.names = Names.instance (context);
        this.enter = Enter.instance (context);
        this.memberEnter = MemberEnter.instance (context);

        this.name = name;
        this.returnType = make.Type (returnType);
        this.body = body;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public void addParam(String paramName, Type type) {
        addParam (names.fromString (paramName), type);
    }
    public void addParam(Name paramName, Type type) {
        params.add(make.VarDef(
                make.Modifiers (Flags.PARAMETER | Flags.MANDATED),
                paramName,
                make.Type (type),
                null));
    }

    public JCTree.JCMethodDecl build() {
        final JCTree.JCMethodDecl synthetic = make.
                MethodDef(make.Modifiers(flags, annotations),
                        name,
                        returnType,
                        List.nil(), // type params
                        params.toList(),
                        List.nil(), // thrown
                        body,
                        null);
        return synthetic;
    }

    public void buildIntoClass (JCTree.JCClassDecl cls) {
        final JCTree.JCMethodDecl mtd = build();
        cls.defs = cls.defs.prepend (mtd);
        memberEnter (mtd, enter.getEnv (cls.sym)); //TODO optimization: set flag in 'enter'?

    }

    private void memberEnter(JCTree.JCMethodDecl synthetic, Env classEnv) {
        try {
            final Method reflectMethodForMemberEnter = memberEnter.getClass().getDeclaredMethod ("memberEnter", JCTree.class, Env.class);
            reflectMethodForMemberEnter.setAccessible (true);
            reflectMethodForMemberEnter.invoke(memberEnter, synthetic, classEnv);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
