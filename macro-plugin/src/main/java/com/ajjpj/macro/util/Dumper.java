package com.ajjpj.macro.util;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;

/**
 * @author arno
 */
public class Dumper {
    public static void dump(CompilationUnitTree cut) {
        final TreeScanner<?, ?> scanner = new TreeScanner<Object, Object>() {
            int indent = 0;

            String indentString() {
                final StringBuilder result = new StringBuilder();
                for(int i=0; i<indent; i++) {
                    result.append ("  ");
                }
                return result.toString();
            }

            @Override public Object scan(Tree node, Object o) {
                if(node != null)
                    System.out.println("  " + indentString() + node.getClass().getSimpleName());

                indent += 1;
                try {
                    return super.scan(node, o);
                }
                finally {
                    indent -= 1;
                }
            }

            @Override public Object visitClass(ClassTree node, Object o) {
                final JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) node;
                System.out.println("      sym:  " + classDecl.sym);

                for(JCTree.JCAnnotation annot: classDecl.mods.annotations) {
                    final JCTree.JCIdent ident = (JCTree.JCIdent) annot.annotationType;
                    System.out.println("        @" + ident.sym);
                }

                return super.visitClass(node, o);
            }
        };

        scanner.scan(cut, null);

//        System.out.println(cut);

        System.out.println();
        System.out.println("----------------------------------");
        System.out.println();
    }

}
