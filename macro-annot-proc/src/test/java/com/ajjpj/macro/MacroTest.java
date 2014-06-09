package com.ajjpj.macro;

import org.junit.Test;

/**
 * @author arno
 */
public class MacroTest {
    @Test
    public void testPlugin() throws Exception {

        com.sun.tools.javac.Main.main(new String[]{
                "-g",
                "-sourcepath", "demo",
                "-d", "demo",
                "-s", "demo",
//                "-processorpath", "out/production/java-macros/",
                "-Xplugin:MacroPlugin",
                "demo/xy/A.java"});
    }

}
