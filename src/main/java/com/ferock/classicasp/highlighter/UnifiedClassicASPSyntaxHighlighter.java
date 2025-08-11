package com.ferock.classicasp.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.ferock.classicasp.psi.ClassicASTypes;

import java.util.HashMap;
import java.util.Map;

public class UnifiedClassicASPSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        System.out.println("[ASP][高亮] 获取词法器(支持方法名识别) —— 解析关闭场景使用词法高亮");
        return new MethodAwareLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (ATTRIBUTES.isEmpty()) {
            System.out.println("[ASP][高亮] 初始化颜色映射（仅词法，不依赖语法树）");

            // ASP 标签
            ATTRIBUTES.put(ClassicASTypes.ASP_OPEN, DefaultLanguageHighlighterColors.BRACES);
            ATTRIBUTES.put(ClassicASTypes.ASP_CLOSE, DefaultLanguageHighlighterColors.BRACES);

            // 关键字
            ATTRIBUTES.put(ClassicASTypes.DIM, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.SET, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.IF, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.THEN, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.ELSE, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.ELSEIF, DefaultLanguageHighlighterColors.KEYWORD);
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
            // 需求：@ 和 Language 与 Function 同色
            ATTRIBUTES.put(ClassicASTypes.LANGUAGE_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.ASP_DIRECTIVE_START, DefaultLanguageHighlighterColors.KEYWORD);
            // 新增：类与成员可见性
            ATTRIBUTES.put(ClassicASTypes.CLASS, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.END_CLASS, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.PUBLIC, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASTypes.PRIVATE, DefaultLanguageHighlighterColors.KEYWORD);

            // ASP 内置对象
            // 内置对象着色（实例字段色系，更接近“系统对象”观感）
            ATTRIBUTES.put(ClassicASTypes.RESPONSE, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.REQUEST, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.SERVER, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.SESSION, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
            ATTRIBUTES.put(ClassicASTypes.APPLICATION, DefaultLanguageHighlighterColors.INSTANCE_FIELD);

            // 标识符
            ATTRIBUTES.put(ClassicASTypes.IDENTIFIER, DefaultLanguageHighlighterColors.IDENTIFIER);

            // 成员访问
            ATTRIBUTES.put(ClassicASTypes.DOT, DefaultLanguageHighlighterColors.DOT);
            // 方法名（由词法器重写为 OBJECT_METHOD 时）
            // 注意：OBJECT_METHOD 在 ClassicASTypes 中声明
            // 若无该类型则仅使用 WRITE 兜底
            try {
                IElementType objectMethodType = (IElementType) ClassicASTypes.class.getField("OBJECT_METHOD").get(null);
                if (objectMethodType != null) {
                    ATTRIBUTES.put(objectMethodType, DefaultLanguageHighlighterColors.FUNCTION_CALL);
                }
            } catch (Throwable ignored) {
                // 忽略，使用 WRITE 兜底
            }
            // 兼容：高亮包内自定义的 OBJECT_METHOD（由 MethodAwareLexer 重写得到）
            ATTRIBUTES.put(ClassicASPTokenTypes.OBJECT_METHOD, DefaultLanguageHighlighterColors.FUNCTION_CALL);
            // 兜底：WRITE 也按方法调用颜色处理
            ATTRIBUTES.put(ClassicASTypes.WRITE, DefaultLanguageHighlighterColors.FUNCTION_CALL);

            // 关键字增强：On / Error / Resume / Err / Goto（Err 在此按关键字色系处理）
            ATTRIBUTES.put(ClassicASPTokenTypes.ON, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASPTokenTypes.ERROR, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASPTokenTypes.RESUME, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASPTokenTypes.ERR, DefaultLanguageHighlighterColors.KEYWORD);
            ATTRIBUTES.put(ClassicASPTokenTypes.GOTO, DefaultLanguageHighlighterColors.KEYWORD);
            // YAML 驱动的通用关键字
            ATTRIBUTES.put(ClassicASPTokenTypes.KEYWORD_GENERIC, DefaultLanguageHighlighterColors.KEYWORD);

            // 字面量
            ATTRIBUTES.put(ClassicASTypes.STRING_LITERAL, DefaultLanguageHighlighterColors.STRING);
            ATTRIBUTES.put(ClassicASTypes.NUMBER_LITERAL, DefaultLanguageHighlighterColors.NUMBER);

            // 注释
            ATTRIBUTES.put(ClassicASTypes.COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT);
        }

        TextAttributesKey key = ATTRIBUTES.get(tokenType);
        return key != null ? new TextAttributesKey[]{key} : new TextAttributesKey[0];
    }
}
