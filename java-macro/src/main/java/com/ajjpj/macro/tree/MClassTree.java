package com.ajjpj.macro.tree;

import com.ajjpj.macro.tree.support.MModifiers;


/**
 * @author arno
 */
public interface MClassTree extends MTree {
    String getName ();
    MModifiers getModifiers ();
}
