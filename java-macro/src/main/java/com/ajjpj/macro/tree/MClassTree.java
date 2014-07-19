package com.ajjpj.macro.tree;

import com.ajjpj.macro.tree.support.MModifiers;

import java.util.Collection;


/**
 * @author arno
 */
public interface MClassTree extends MTree {
    String getName ();
    MModifiers getModifiers ();

    /**
     * The returned collection is read-only - use MTreeMaker methods to add / remove methods
     */
    Collection<MMethodTree> getMethods();

    /**
     * The returned collection is read-only - use MTreeMaker methods to add / remove methods
     */
    Collection<MMethodTree> getMethods(String name);
}
