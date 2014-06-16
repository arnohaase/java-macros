package a;


import com.sun.tools.javac.Main;

/**
 * @author arno
 */
public class CompileMain {
    public static void main(String[] args) throws Exception {
        Main.main(new String[] {
                "-sourcepath", "java-macro-sample/src/main/java",
                "-d", "java-macro-debugcompilation/classes",
                "-classpath", "java-macro/target/classes:/opt/jdk1.8/lib/tools.jar",
                "java-macro-sample/src/main/java/com/ajjpj/macrotest/SmartString.java",
                "java-macro-sample/src/main/java/com/ajjpj/macrotest/structclass/Struct.java"
        });
    }
}
