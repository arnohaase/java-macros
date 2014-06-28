package com.ajjpj.macro.impl.shared.methodmacro;

import com.ajjpj.macro.MethodMacro;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author arno
 */
class StaticMethodResolver {
    private final ClassReader classes;
    private final Names names;

    StaticMethodResolver (Context context) {
        this.classes = ClassReader.instance (context);
        this.names = Names.instance (context);
    }

    /**
     * returns
     * <ul>
     *     <li> the invoked method iff it is a macro placeholder and there is no room for ambiguity, i.e. there is only one method that matches name
     *           and number of parameters,</li>
     *     <li> null iff the invoked method is definitely no macro placeholder, and</li>
     *     <li> throws an exception if several imported methods match the name and parameter number of the invoked method, and one or more of them is
     *           a macro placeholder</li>
     * </ul>
     */
    Symbol.MethodSymbol getUniqueInvokedMacroPlaceholder (JCTree.JCCompilationUnit compilationUnit, JCTree.JCMethodInvocation methodInvocation) {
        final String methodNameFqn = methodNameFqn (methodInvocation.getMethodSelect());
        if (methodNameFqn == null) {
            return null; // only straight-forward invocations are applicable for macros
        }

        final int idxLastDot = methodNameFqn.lastIndexOf('.');
        final Name methodName = names.fromString(methodNameFqn.substring(idxLastDot + 1));

        if (idxLastDot == -1) {
            //TODO check if the invoked method is in the current class' scope

            // simple name --> check static imports
            final Pair<Symbol.MethodSymbol, Boolean> fromNamed = fromStaticImports (methodName, methodInvocation.getArguments().size(), compilationUnit.namedImportScope);
            if (fromNamed.fst != null) {
                return fromNamed.fst;
            }
            if (fromNamed.snd) {
                return null;
            }

            return fromStaticImports (methodName, methodInvocation.getArguments().size(), compilationUnit.starImportScope).fst;
        }
        else {
            //TODO check if the invocation's qualifier is a variable in current scope

            // class name present --> check non-static imports

            final String className = methodNameFqn.substring(0, idxLastDot);

            final Symbol.ClassSymbol cls = resolveClass (compilationUnit, className);
            if (cls == null) {
                return null; // method invocation on some variable --> no macro
            }

            final List<Symbol.MethodSymbol> candidates = new ArrayList<>();
            boolean hasMacroCandidate = false;

            for (Symbol m: cls.members().getElements()) {
                if (! (m instanceof Symbol.MethodSymbol)) {
                    continue;
                }

                final Symbol.MethodSymbol mtd = (Symbol.MethodSymbol) m;

                if (mtd.getAnnotation (MethodMacro.class) != null) {
                    // skip actual method implementations to avoid tangling if there are several macro method of same name with different parameter count
                    continue;
                }

                if (mtd.name.equals (methodName) &&
                        mtd.getParameters().size() == methodInvocation.getArguments().size()) {
                    candidates.add(mtd);
                    if (mtd.getAnnotation (MethodMacroPlaceholder.class) != null) {
                        hasMacroCandidate = true;
                    }
                }
            }

            if (hasMacroCandidate) {
                if (candidates.size() != 1) {
                    throw new RuntimeException (); //TODO error handling
                }
                return candidates.get(0);
            }
            else {
                return null;
            }
        }
    }

    /**
     * @return pair of macro method, finished
     */
    private Pair<Symbol.MethodSymbol, Boolean> fromStaticImports (Name methodName, int numArgs, Scope scope) {
        final List<Symbol.MethodSymbol> candidates = new ArrayList<>();
        boolean hasMacroMethod = false;

        for (Symbol s: scope.getElements()) {
            if (! (s instanceof Symbol.MethodSymbol)) {
                continue;
            }

            final Symbol.MethodSymbol mtd = (Symbol.MethodSymbol) s;
            if (methodName.equals (mtd.name) && numArgs == mtd.getParameters().size()) {
                candidates.add (mtd);
                hasMacroMethod = hasMacroMethod || mtd.getAnnotation (MethodMacroPlaceholder.class) != null;
            }
        }

        if (candidates.isEmpty()) {
            return Pair.of (null, false);
        }

        if (! hasMacroMethod) {
            return Pair.of (null, true);
        }
        if (candidates.size() == 1) {
            return Pair.of (candidates.get (0), true);
        }
        throw new RuntimeException (); //TODO error handling
    }

    private Symbol.ClassSymbol resolveClass (JCTree.JCCompilationUnit compilationUnit, String className) {

        try {
            // there is a class with className as its fqn
            return classes.loadClass (names.fromString(className));
        } catch (Symbol.CompletionFailure completionFailure) { /**/
        }

        if (className.indexOf('.') >= 0) {
            return null; // qualified name --> no use searching in imports
            //TODO this covers only top-level classes --> at least document this
        }

        final Name simpleName = names.fromString (className);

        final Symbol fromNamedImport = compilationUnit.namedImportScope.lookup (simpleName).sym;
        if (fromNamedImport instanceof Symbol.ClassSymbol) {
            return (Symbol.ClassSymbol) fromNamedImport;
        }

        try {
            // try loading class from same package as the invoking code
            return classes.loadClass (names.fromString (compilationUnit.packge.fullname.toString() + "." + className));
        } catch (Symbol.CompletionFailure completionFailure) { /**/
        }

        final Symbol fromStarImport = compilationUnit.starImportScope.lookup (simpleName).sym;
        if (fromStarImport instanceof Symbol.ClassSymbol) {
            return (Symbol.ClassSymbol) fromStarImport;
        }

        return null;
    }

    private String methodNameFqn (JCTree.JCExpression methodSelect) {
        if (methodSelect instanceof JCTree.JCIdent) {
            return ((JCTree.JCIdent) methodSelect).getName().toString();
        }
        if (methodSelect instanceof JCTree.JCFieldAccess) {
            final JCTree.JCFieldAccess fieldAccess = (JCTree.JCFieldAccess) methodSelect;
            return methodNameFqn (fieldAccess.getExpression()) + "." + fieldAccess.name;
        }
        return null;
    }

}
