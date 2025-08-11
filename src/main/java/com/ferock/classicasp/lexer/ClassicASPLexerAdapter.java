package com.ferock.classicasp.lexer;

import com.intellij.lexer.FlexAdapter;
import com.ferock.classicasp.ClassicASPLexer;

public class ClassicASPLexerAdapter extends FlexAdapter {
    public ClassicASPLexerAdapter() {
        super(new ClassicASPLexer(null));
    }
}
