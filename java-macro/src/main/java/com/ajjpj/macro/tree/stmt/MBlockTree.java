package com.ajjpj.macro.tree.stmt;

import com.ajjpj.macro.tree.MStatementTree;

import java.util.List;

/**
 * @author arno
 */
public interface MBlockTree extends MStatementTree {
    List<MStatementTree> getStatements(); //TODO r/o or r/w?
}
