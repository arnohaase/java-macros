package com.ajjpj.macro;

import com.ajjpj.macro.tree.MTree;
import com.ajjpj.macro.util.MTreeMaker;
import com.ajjpj.macro.util.MTypes;

/**
 * @author arno
 */
public interface CompilerContext {
    void msg (MTree codePos, String msg);
    void warn (MTree codePos, String msg);
    void error (MTree codePos, String msg);

    MTypes types();
    MTreeMaker treeMaker();
}
