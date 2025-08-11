package com.ferock.classicasp.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.ferock.classicasp.psi.ClassicASTypes;

import java.util.HashMap;
import java.util.Map;

public class ClassicASPSyntaxHighlighter extends SyntaxHighlighterBase {

    // 使用默认颜色方案，避免在IDE启动时创建自定义TextAttributesKey
    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    static {
        // 静态初始化块为空，所有映射都延迟到运行时
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        // 使用 Flex 生成的词法分析器，通过 FlexAdapter 适配
        return new FlexAdapter(new com.ferock.classicasp.ClassicASPLexer(null));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        // 动态设置 ATTRIBUTES 映射
        if (ATTRIBUTES.isEmpty()) {
            System.out.println("初始化语法高亮映射");

            // ASP 标签
            ATTRIBUTES.put(ClassicASTypes.ASP_OPEN, DefaultLanguageHighlighterColors.BRACES);
            ATTRIBUTES.put(ClassicASTypes.ASP_CLOSE, DefaultLanguageHighlighterColors.BRACES);

            // 关键字
            ATTRIBUTES.put(ClassicASTypes.DIM, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.SET, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.IF, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.THEN, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.ELSE, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.END_IF, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.FOR, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.NEXT, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.WHILE, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.WEND, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.DO, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.LOOP, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.SELECT, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.CASE, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.END_SELECT, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.FUNCTION, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.END_FUNCTION, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.SUB, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.END_SUB, DefaultLanguageHighlighterColors.KEYWORD);

            // ASP 内置对象
            ATTRIBUTES.put(ClassicASTypes.RESPONSE, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.REQUEST, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.SERVER, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.SESSION, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.APPLICATION, DefaultLanguageHighlighterColors.INSTANCE_FIELD);

            // 标识符
            ATTRIBUTES.put(ClassicASTypes.IDENTIFIER, DefaultLanguageHighlighterColors.IDENTIFIER);

            // 字符串字面量 - 使用绿色
            ATTRIBUTES.put(ClassicASTypes.STRING_LITERAL, DefaultLanguageHighlighterColors.STRING);

            // 数字字面量
            ATTRIBUTES.put(ClassicASTypes.NUMBER_LITERAL, DefaultLanguageHighlighterColors.NUMBER);
        }

        // 返回对应的 TextAttributesKey
        TextAttributesKey key = ATTRIBUTES.get(tokenType);
        return key != null ? new TextAttributesKey[]{key} : new TextAttributesKey[0];
    }


}