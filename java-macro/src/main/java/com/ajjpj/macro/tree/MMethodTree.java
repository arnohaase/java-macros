package com.ajjpj.macro.tree;

import com.ajjpj.macro.tree.stmt.MBlockTree;
import com.ajjpj.macro.tree.stmt.MVariableDeclTree;
import com.ajjpj.macro.tree.support.MModifiers;
import com.ajjpj.macro.tree.support.MType;

import java.util.List;

/**
 * @author arno
 */
public interface MMethodTree extends MTree {
    String getName();
    MType getReturnType();
    MModifiers getModifiers();
    //TODO annotations

    List<MVariableDeclTree> getParameters();
    //TODO throws clause

    /**
     * <code>null</code> for abstract methods
     */
    MBlockTree getBody();

    //TODO default initializer for annotation methods
}
