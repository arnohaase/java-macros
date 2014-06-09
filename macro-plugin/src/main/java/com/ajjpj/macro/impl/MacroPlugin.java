package com.ajjpj.macro.impl;

import com.ajjpj.macro.util.Dumper;
import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author arno
 */
public class MacroPlugin implements Plugin {
    @Override public String getName() {
        return "MacroPlugin";
    }

    @Override public void init (JavacTask task, String... args) {
//        final JavacTaskImplJavacTaskImpl taskImpl = (JavacTaskImpl) task;
        final BasicJavacTask taskImpl = (BasicJavacTask) task;
        final Context context = taskImpl.getContext();

        final Log log = Log.instance(context);

        final JavacProcessingEnvironment pe = JavacProcessingEnvironment.instance(context);
        final Trees trees = Trees.instance(pe);

//        TreeInfo.scopeFor()



        task.addTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent e) {
//                System.out.println("start " + e);
//                Dumper.dump(e.getCompilationUnit());
            }

            @Override
            public void finished(TaskEvent e) {
                System.out.println("end " + e);

                if(e.getKind() == TaskEvent.Kind.ENTER) {
//                    final MacroMethodCollector mmc = new MacroMethodCollector();
//                    mmc.visitTopLevel((JCTree.JCCompilationUnit) e.getCompilationUnit());
//
                    final MacroMethodCompiler macroMethodCompiler = new MacroMethodCompiler(context);
                    macroMethodCompiler.compileMacroMethods((JCTree.JCCompilationUnit) e.getCompilationUnit());

//                    System.out.println("com.ajjpj.macro methods: " + mmc.macroMethods);
                }

//                Dumper.dump(e.getCompilationUnit());
            }
        });
    }
}
