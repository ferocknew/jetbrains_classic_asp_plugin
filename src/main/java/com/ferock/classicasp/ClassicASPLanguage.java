package com.ferock.classicasp;

import com.intellij.lang.Language;

public class ClassicASPLanguage extends Language {
    public static final ClassicASPLanguage INSTANCE = new ClassicASPLanguage();

    private ClassicASPLanguage() {
        super("ClassicASP");
        System.err.println("!!! ClassicASPLanguage 构造函数被调用 !!!");
    }
}