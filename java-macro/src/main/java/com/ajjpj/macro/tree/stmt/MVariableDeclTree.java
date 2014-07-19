package com.ajjpj.macro.tree.stmt;

import com.ajjpj.macro.tree.MStatementTree;
import com.ajjpj.macro.tree.support.MType;


/**
 * @author arno
 */
public interface MVariableDeclTree extends MStatementTree {
    String getName ();
    MType getType ();

    //TODO init clause
}
