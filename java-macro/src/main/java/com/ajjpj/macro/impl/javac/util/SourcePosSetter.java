package com.ajjpj.macro.impl.javac.util;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

/**
 * @author arno
 */
public class SourcePosSetter extends TreeScanner {
    private final int pos;

    public SourcePosSetter(int pos) {
        this.pos = pos;
    }

    @Override
    public void scan(JCTree tree) {
        super.scan(tree);

        if(tree != null) {
            tree.setPos(pos);
        }
    }
}
