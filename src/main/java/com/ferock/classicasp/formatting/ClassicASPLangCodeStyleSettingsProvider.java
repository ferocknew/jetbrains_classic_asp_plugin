package com.ferock.classicasp.formatting;

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.ferock.classicasp.ClassicASPLanguage;

public class ClassicASPLangCodeStyleSettingsProvider extends CustomCodeStyleSettings {

    public ClassicASPLangCodeStyleSettingsProvider(CodeStyleSettings settings) {
        super(ClassicASPLanguage.INSTANCE.getID(), settings);
    }

    /**
     * 获取语言实例
     */
    public Language getLanguage() {
        return ClassicASPLanguage.INSTANCE;
    }
}