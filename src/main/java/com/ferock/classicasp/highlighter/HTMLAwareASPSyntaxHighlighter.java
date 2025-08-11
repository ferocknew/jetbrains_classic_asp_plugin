package com.ferock.classicasp.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.ferock.classicasp.psi.ClassicASTypes;
import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * HTML感知的ASP语法高亮器
 * 使用IntelliJ内置的HTML颜色方案，减少代码重复
 */
public class HTMLAwareASPSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    static {
        // ASP 标签 - 使用HTML标签的颜色
        ATTRIBUTES.put(ClassicASTypes.ASP_OPEN, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASTypes.ASP_CLOSE, DefaultLanguageHighlighterColors.MARKUP_TAG);

        // 关键字 - 使用HTML属性的颜色
        ATTRIBUTES.put(ClassicASTypes.DIM, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.SET, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.IF, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.THEN, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.ELSE, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.END_IF, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.FOR, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.NEXT, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.WHILE, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.WEND, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.DO, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.LOOP, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.SELECT, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.CASE, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.END_SELECT, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.FUNCTION, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.END_FUNCTION, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.SUB, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASTypes.END_SUB, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);

        // ASP 内置对象 - 使用HTML标签的颜色
        ATTRIBUTES.put(ClassicASTypes.RESPONSE, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASTypes.REQUEST, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASTypes.SERVER, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASTypes.SESSION, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASTypes.APPLICATION, DefaultLanguageHighlighterColors.MARKUP_TAG);

        // 标识符 - 使用HTML文本的颜色
        ATTRIBUTES.put(ClassicASTypes.IDENTIFIER, DefaultLanguageHighlighterColors.IDENTIFIER);

        // 字符串字面量 - 使用HTML字符串的颜色
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_LITERAL, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_START, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_CONTENT, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_END, DefaultLanguageHighlighterColors.STRING);

        // 数字字面量 - 使用HTML数字的颜色
        ATTRIBUTES.put(ClassicASPTokenTypes.NUMBER_LITERAL, DefaultLanguageHighlighterColors.NUMBER);

        // HTML 相关的高亮 - 直接使用HTML的颜色方案
        ATTRIBUTES.put(ClassicASPTokenTypes.HTML_TAG, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASPTokenTypes.HTML_TAG_END, DefaultLanguageHighlighterColors.MARKUP_TAG);
        ATTRIBUTES.put(ClassicASPTokenTypes.HTML_ATTRIBUTE, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
        ATTRIBUTES.put(ClassicASPTokenTypes.HTML_ATTRIBUTE_VALUE, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.HTML_TEXT, DefaultLanguageHighlighterColors.IDENTIFIER);

        // 注释 - 使用HTML注释的颜色
        ATTRIBUTES.put(com.intellij.psi.TokenType.WHITE_SPACE, DefaultLanguageHighlighterColors.LINE_COMMENT);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        // 使用现有的合并词法分析器
        return new FlexAdapter(new com.ferock.classicasp.ClassicASPLexer(null));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey key = ATTRIBUTES.get(tokenType);
        return key != null ? new TextAttributesKey[]{key} : new TextAttributesKey[0];
    }
}
