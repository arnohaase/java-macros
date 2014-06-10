package com.ajjpj.macro.impl.util;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

/**
 * @author arno
 */
public class TreeDumper extends TreeScanner {
    private int indent = 0;

    @Override public void scan(JCTree tree) {
        if(tree == null) {
            return;
        }

        printIndent();
        System.out.println(tree.getClass().getName());

        indent += 1;
        super.scan(tree);
        indent -= 1;
    }

    private void printIndent() {
        for(int i=0; i<indent; i++) {
            System.out.print("  ");
        }
    }

    @Override
    public void visitSelect(JCTree.JCFieldAccess tree) {
        printIndent();
        System.out.println("name: " + tree.name);
        super.visitSelect(tree);
    }

    @Override
    public void visitLiteral(JCTree.JCLiteral tree) {
        printIndent();
        System.out.println(tree.value + " [" + tree.type + "] " + tree.typetag);
    }
}
