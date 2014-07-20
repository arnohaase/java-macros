package com.ajjpj.macro.tree;

import com.ajjpj.macro.tree.support.MSourcePosition;


/**
 * @author arno
 */
public interface MTree {
    MSourcePosition getSourcePosition ();
    Object getInternalRepresentation ();
}
