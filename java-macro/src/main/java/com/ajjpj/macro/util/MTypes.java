package com.ajjpj.macro.util;

import com.ajjpj.macro.tree.support.MType;

/**
 * @author arno
 */
public interface MTypes {
    MType stringType();

    MType fromFqn(String fqn);

    //TODO add built-in types
}
