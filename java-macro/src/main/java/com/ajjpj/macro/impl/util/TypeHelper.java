package com.ajjpj.macro.impl.util;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * @author arno
 */
public class TypeHelper {
    private final TreeMaker make;
    private final Names names;

    public TypeHelper(Context context) {
        this.make = TreeMaker.instance (context);
        this.names = Names.instance (context);
    }

    public JCTree makeTypeTree (String typeName) {
        String remainder = typeName;
        JCTree.JCExpression result = null;
        int idxDot;

        do {
            idxDot = remainder.indexOf('.');

            final String nameString = idxDot > 0 ? remainder.substring(0, idxDot) : remainder;
            final Name name = names.fromString (nameString);

            result = result != null ? make.Select (result, name) : make.Ident(name);

            remainder = remainder.substring (idxDot + 1);
        }
        while (idxDot > 0);

        return result;
        //TODO how to handle '$'?
    }
}
