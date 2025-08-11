package com.ferock.classicasp.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;
import com.ferock.classicasp.psi.ClassicASTypes;
import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;

// 引用 Flex 生成的 ASP 词法器
import com.ferock.classicasp.ClassicASPLexer;

import java.util.HashMap;
import java.util.Map;

/**
 * 混合语法高亮器
 * 使用内置HTML解析器处理HTML部分，自定义解析器处理ASP部分
 */
public class HybridASPSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    static {
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
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_LITERAL, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_START, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_CONTENT, DefaultLanguageHighlighterColors.STRING);
        ATTRIBUTES.put(ClassicASPTokenTypes.STRING_END, DefaultLanguageHighlighterColors.STRING);

        // 数字字面量
        ATTRIBUTES.put(ClassicASPTokenTypes.NUMBER_LITERAL, DefaultLanguageHighlighterColors.NUMBER);

        // 注释
        ATTRIBUTES.put(TokenType.WHITE_SPACE, DefaultLanguageHighlighterColors.LINE_COMMENT);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new HybridASPLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey key = ATTRIBUTES.get(tokenType);
        return key != null ? new TextAttributesKey[]{key} : new TextAttributesKey[0];
    }

    /**
     * 混合词法分析器
     * 在ASP部分使用自定义解析器，在HTML部分使用内置HTML解析器
     */
    private static class HybridASPLexer extends LexerBase {
        private CharSequence buffer;
        private int start;
        private int end;
        private int currentPosition;
        private IElementType currentTokenType;
        private boolean inASPSection = false;
        private Lexer aspLexer;
        private Lexer htmlLexer;

        public HybridASPLexer() {
            this.aspLexer = new FlexAdapter(new ClassicASPLexer(null));
            // 尝试获取HTML词法分析器
            try {
                // 使用反射获取HTML词法分析器
                Class<?> htmlLanguageClass = Class.forName("com.intellij.lang.html.HTMLLanguage");
                Class<?> htmlLexerClass = Class.forName("com.intellij.lang.html.HTMLLexer");
                this.htmlLexer = (Lexer) htmlLexerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                // 如果无法获取HTML词法分析器，使用简化的HTML处理
                this.htmlLexer = null;
            }
        }

        @Override
        public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
            this.buffer = buffer;
            this.start = startOffset;
            this.end = endOffset;
            this.currentPosition = startOffset;
            this.inASPSection = false;
            advance();
        }

        @Override
        public int getState() {
            return inASPSection ? 1 : 0;
        }

        @Override
        public IElementType getTokenType() {
            return currentTokenType;
        }

        @Override
        public int getTokenStart() {
            return start;
        }

        @Override
        public int getTokenEnd() {
            return currentPosition;
        }

        @Override
        public void advance() {
            start = currentPosition;

            if (currentPosition >= end) {
                currentTokenType = null;
                return;
            }

            char currentChar = buffer.charAt(currentPosition);

            // 跳过空白字符
            if (Character.isWhitespace(currentChar)) {
                while (currentPosition < end && Character.isWhitespace(buffer.charAt(currentPosition))) {
                    currentPosition++;
                }
                currentTokenType = TokenType.WHITE_SPACE;
                return;
            }

            // 检查是否进入ASP部分
            if (currentChar == '<' && currentPosition + 1 < end && buffer.charAt(currentPosition + 1) == '%') {
                currentPosition += 2;
                currentTokenType = ClassicASTypes.ASP_OPEN;
                inASPSection = true;
                return;
            }

            // 检查是否退出ASP部分
            if (inASPSection && currentChar == '%' && currentPosition + 1 < end && buffer.charAt(currentPosition + 1) == '>') {
                currentPosition += 2;
                currentTokenType = ClassicASTypes.ASP_CLOSE;
                inASPSection = false;
                return;
            }

            if (inASPSection) {
                // 在ASP部分，使用自定义解析器
                processASPToken();
            } else {
                // 在HTML部分，使用HTML解析器或简化处理
                processHTMLToken();
            }
        }

        private void processASPToken() {
            // 使用现有的ASP词法分析逻辑
            char currentChar = buffer.charAt(currentPosition);

            // 处理注释
            if (currentChar == '\'') {
                while (currentPosition < end && buffer.charAt(currentPosition) != '\n') {
                    currentPosition++;
                }
                currentTokenType = ClassicASPTokenTypes.COMMENT;
                return;
            }

            // 处理字符串
            if (currentChar == '"') {
                currentPosition++;
                while (currentPosition < end && buffer.charAt(currentPosition) != '"') {
                    currentPosition++;
                }
                if (currentPosition < end) {
                    currentPosition++;
                }
                currentTokenType = ClassicASPTokenTypes.STRING_LITERAL;
                return;
            }

            // 处理关键字和标识符
            if (Character.isLetter(currentChar) || currentChar == '_') {
                StringBuilder identifier = new StringBuilder();
                while (currentPosition < end &&
                       (Character.isLetterOrDigit(buffer.charAt(currentPosition)) ||
                        buffer.charAt(currentPosition) == '_' ||
                        buffer.charAt(currentPosition) == '.')) {
                    identifier.append(buffer.charAt(currentPosition));
                    currentPosition++;
                }

                String word = identifier.toString().toLowerCase();
                // 这里可以添加关键字识别逻辑
                currentTokenType = ClassicASTypes.IDENTIFIER;
                return;
            }

            // 处理数字
            if (Character.isDigit(currentChar)) {
                while (currentPosition < end && Character.isDigit(buffer.charAt(currentPosition))) {
                    currentPosition++;
                }
                currentTokenType = ClassicASPTokenTypes.NUMBER_LITERAL;
                return;
            }

            // 处理其他字符
            currentPosition++;
            currentTokenType = TokenType.BAD_CHARACTER;
        }

        private void processHTMLToken() {
            // 简化版HTML处理，或者使用内置HTML解析器
            char currentChar = buffer.charAt(currentPosition);

            // 处理HTML标签
            if (currentChar == '<') {
                currentPosition++;
                currentTokenType = ClassicASPTokenTypes.HTML_TAG;
                return;
            }

            if (currentChar == '>') {
                currentPosition++;
                currentTokenType = ClassicASPTokenTypes.HTML_TAG_END;
                return;
            }

            // 处理HTML文本
            StringBuilder text = new StringBuilder();
            while (currentPosition < end && buffer.charAt(currentPosition) != '<') {
                text.append(buffer.charAt(currentPosition));
                currentPosition++;
            }
            currentTokenType = ClassicASPTokenTypes.HTML_TEXT;
        }

        @NotNull
        @Override
        public CharSequence getBufferSequence() {
            return buffer;
        }

        @Override
        public int getBufferEnd() {
            return end;
        }
    }
}
