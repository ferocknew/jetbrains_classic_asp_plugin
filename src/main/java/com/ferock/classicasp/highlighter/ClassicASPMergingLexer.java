package com.ferock.classicasp.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.lexer.FlexAdapter;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.ferock.classicasp.VBScriptKeywords;
import com.ferock.classicasp.VBScriptOperators;
import org.jetbrains.annotations.NotNull;

// 引入 Flex 生成的 ASP 词法器
import com.ferock.classicasp.ClassicASPLexer;

public class ClassicASPMergingLexer extends LexerBase {

    private Lexer htmlLexer;
    private Lexer aspLexer;
    private Lexer currentLexer;
    private CharSequence buffer;
    private int start;
    private int end;
    private int currentPosition;
    private IElementType currentTokenType;
    private boolean inASPSection = false;

    public ClassicASPMergingLexer() {
        // 简化处理：仅构建 ASP 词法器
        this.aspLexer = new FlexAdapter(new ClassicASPLexer(null));
    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.start = startOffset;
        this.end = endOffset;
        this.currentPosition = startOffset;
        this.inASPSection = false;
        // 词法分析器开始处理文件
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
        try {
            start = currentPosition;

            if (currentPosition >= end) {
                currentTokenType = null;
                return;
            }

            // 安全检查边界
            if (currentPosition < 0 || currentPosition >= buffer.length()) {
                currentTokenType = null;
                return;
            }

            char currentChar = buffer.charAt(currentPosition);

            // 🔍 增加详细调试日志
                    // 移除调试日志以提升性能

        // 跳过空白字符
        if (Character.isWhitespace(currentChar)) {
            int iterations = 0;
            while (currentPosition < end && Character.isWhitespace(buffer.charAt(currentPosition)) &&
                   iterations < com.ferock.classicasp.SafetyLimits.MAX_LOOP_ITERATIONS) {
                currentPosition++;
                iterations++;
            }
            com.ferock.classicasp.SafetyLimits.checkLoopLimit(iterations, "ClassicASPMergingLexer.processHTMLToken whitespace");
            currentTokenType = TokenType.WHITE_SPACE;
            return;
        }

        // 检查是否进入ASP部分
        if (currentChar == '<' && currentPosition + 1 < end && buffer.charAt(currentPosition + 1) == '%') {
            currentPosition += 2;
            currentTokenType = ClassicASPTokenTypes.ASP_START;
            inASPSection = true;
            // System.out.println("Token: ASP_START (entering ASP section)");
            return;
        }

        // 检查是否退出ASP部分
        if (currentChar == '%' && currentPosition + 1 < end && buffer.charAt(currentPosition + 1) == '>') {
            currentPosition += 2;
            currentTokenType = ClassicASPTokenTypes.ASP_END;
            inASPSection = false;
            // System.out.println("Token: ASP_END (exiting ASP section)");
            return;
        }

        // 如果在ASP部分，使用ASP词法分析器
        if (inASPSection) {
            int oldPos = currentPosition;
            processASPToken();
            if (currentPosition == oldPos) {
                // 强制推进防止死循环
                currentPosition++;
                currentTokenType = TokenType.BAD_CHARACTER;
            }
        } else {
            // 在HTML部分，处理HTML标签和内容
            int oldPos = currentPosition;
            processHTMLToken();
            if (currentPosition == oldPos) {
                // 强制推进防止死循环
                currentPosition++;
                currentTokenType = TokenType.BAD_CHARACTER;
            }
        }
        } catch (Exception e) {
            // 发生任何错误时，安全地推进一个字符
            System.err.println("ERROR in ClassicASPMergingLexer.advance(): " + e.getMessage());
            currentPosition = Math.min(currentPosition + 1, end);
            currentTokenType = TokenType.BAD_CHARACTER;
        }
    }

        private void processASPToken() {
        // 使用ASP词法分析器的逻辑
        // 安全检查：确保不会越界
        if (currentPosition >= end) {
            // 已到达文件末尾
            currentTokenType = null;
            return;
        }

        char currentChar = buffer.charAt(currentPosition);
        int originalPosition = currentPosition; // 记录原始位置用于安全检查

                // 移除详细日志以提升性能

        // 处理注释
        if (currentChar == '\'') {
            currentPosition++;
            StringBuilder comment = new StringBuilder();
            int iterations = 0;
            while (currentPosition < end && buffer.charAt(currentPosition) != '\n' &&
                   iterations < com.ferock.classicasp.SafetyLimits.MAX_LOOP_ITERATIONS) {
                comment.append(buffer.charAt(currentPosition));
                currentPosition++;
                iterations++;
            }
            com.ferock.classicasp.SafetyLimits.checkLoopLimit(iterations, "ClassicASPMergingLexer.processASPToken comment");
            currentTokenType = ClassicASPTokenTypes.COMMENT;
            // System.out.println("Token: COMMENT -> " + comment.toString());
            return;
        }

        // 处理 @ 符号
        if (currentChar == '@') {
            currentPosition++;
            currentTokenType = ClassicASPTokenTypes.AT_SYMBOL;
            // System.out.println("Token: AT_SYMBOL");
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
            currentTokenType = ClassicASPTokenTypes.STRING_CONTENT;
            // System.out.println("Token: STRING_CONTENT");
            return;
        }

        // 处理日期字面量 #date#
        if (currentChar == '#') {
            currentPosition++;
            // 跳过日期内容直到找到结束的 #
            while (currentPosition < end && buffer.charAt(currentPosition) != '#') {
                currentPosition++;
            }
            if (currentPosition < end) {
                currentPosition++; // 跳过结束的 #
            }
            currentTokenType = ClassicASPTokenTypes.DATE_LITERAL;
            // System.out.println("Token: DATE_LITERAL");
            return;
        }

        // 处理关键字和标识符
        if (Character.isLetter(currentChar) || currentChar == '_') {
            StringBuilder identifier = new StringBuilder();
            int iterations = 0;
            while (currentPosition < end &&
                   (Character.isLetterOrDigit(buffer.charAt(currentPosition)) ||
                    buffer.charAt(currentPosition) == '_' ||
                    buffer.charAt(currentPosition) == '.') &&
                   iterations < com.ferock.classicasp.SafetyLimits.MAX_LOOP_ITERATIONS) {
                identifier.append(buffer.charAt(currentPosition));
                currentPosition++;
                iterations++;
            }
            com.ferock.classicasp.SafetyLimits.checkLoopLimit(iterations, "ClassicASPMergingLexer.processASPToken identifier");

                        String word = identifier.toString();

            // 大驼峰转换：将关键词转换为正确的大小写格式
            String normalizedWord = normalizeKeyword(word);

            // 特殊处理所有 "End xxx" 组合
            if (normalizedWord.equalsIgnoreCase("end") && currentPosition < end) {
                // 跳过空白字符
                int tempPos = currentPosition;
                while (tempPos < end && Character.isWhitespace(buffer.charAt(tempPos))) {
                    tempPos++;
                }

                // 检查下一个单词
                if (tempPos < end && Character.isLetter(buffer.charAt(tempPos))) {
                    StringBuilder nextWord = new StringBuilder();
                    while (tempPos < end && Character.isLetterOrDigit(buffer.charAt(tempPos))) {
                        nextWord.append(buffer.charAt(tempPos));
                        tempPos++;
                    }

                    String nextWordStr = nextWord.toString().toLowerCase();
                    IElementType endTokenType = null;

                    // 检查所有可能的 End 组合
                    switch (nextWordStr) {
                        case "if":
                            endTokenType = ClassicASPTokenTypes.END_IF;
                            break;
                        case "function":
                            endTokenType = ClassicASPTokenTypes.END_FUNCTION;
                            break;
                        case "sub":
                            endTokenType = ClassicASPTokenTypes.END_SUB;
                            break;
                        case "for":
                            endTokenType = ClassicASPTokenTypes.END_FOR;
                            break;
                        case "while":
                            endTokenType = ClassicASPTokenTypes.END_WHILE;
                            break;
                        case "do":
                            endTokenType = ClassicASPTokenTypes.END_DO;
                            break;
                        case "loop":
                            endTokenType = ClassicASPTokenTypes.END_LOOP;
                            break;
                        case "select":
                            endTokenType = ClassicASPTokenTypes.END_SELECT;
                            break;
                        case "property":
                            endTokenType = ClassicASPTokenTypes.END_PROPERTY;
                            break;
                        case "class":
                            endTokenType = ClassicASPTokenTypes.END_CLASS;
                            break;
                    }

                    if (endTokenType != null) {
                        // 这是一个 "End xxx" 组合
                        currentPosition = tempPos;
                        currentTokenType = endTokenType;
                        // System.out.println("Token: " + endTokenType + " (End " + nextWordStr + ")");
                        return;
                    }
                }
            }

            currentTokenType = getKeywordTokenType(normalizedWord);
            // System.out.println("Token: " + normalizedWord + " -> " + currentTokenType);
            return;
        }

        // 处理数字
        if (Character.isDigit(currentChar)) {
            int iterations = 0;
            while (currentPosition < end && Character.isDigit(buffer.charAt(currentPosition)) &&
                   iterations < com.ferock.classicasp.SafetyLimits.MAX_LOOP_ITERATIONS) {
                currentPosition++;
                iterations++;
            }
            com.ferock.classicasp.SafetyLimits.checkLoopLimit(iterations, "ClassicASPMergingLexer.processASPToken number");
            currentTokenType = ClassicASPTokenTypes.NUMBER;
            // System.out.println("Token: NUMBER");
            return;
        }

        // 处理复合运算符
        VBScriptOperators.CompoundOperatorInfo compoundOp = VBScriptOperators.parseCompoundOperator(buffer, currentPosition);
        if (compoundOp != null) {
            currentPosition += compoundOp.length;
            currentTokenType = compoundOp.tokenType;
            return;
        }

        // 处理单个字符
        currentPosition++;
        currentTokenType = getSingleCharTokenType(currentChar);
        // System.out.println("Token: " + currentChar + " -> " + currentTokenType);

        // 安全检查：确保位置被推进，防止无限循环
        if (currentPosition == originalPosition) {
            System.err.println("CRITICAL: processASPToken() did not advance position at " + currentPosition +
                             ", char='" + currentChar + "' (code=" + (int)currentChar + "). Forcing advance.");
            currentPosition = Math.min(currentPosition + 1, end);
            currentTokenType = TokenType.BAD_CHARACTER;
        }
    }

    private void processHTMLToken() {
        char currentChar = buffer.charAt(currentPosition);
        int originalPosition = currentPosition; // 记录原始位置用于安全检查

        // 处理HTML标签开始
        if (currentChar == '<') {
            currentPosition++;
            // 检查是否是结束标签
            if (currentPosition < end && buffer.charAt(currentPosition) == '/') {
                currentPosition++;
                currentTokenType = ClassicASPTokenTypes.HTML_TAG_END;
                // System.out.println("Token: HTML_TAG_END");
            } else {
                currentTokenType = ClassicASPTokenTypes.HTML_TAG;
                // System.out.println("Token: HTML_TAG");
            }
            return;
        }

        // 处理HTML标签结束
        if (currentChar == '>') {
            currentPosition++;
            currentTokenType = ClassicASPTokenTypes.HTML_TAG_END;
            // System.out.println("Token: HTML_TAG_END");
            return;
        }

        // 处理HTML属性
        if (Character.isLetter(currentChar) || currentChar == '_') {
            StringBuilder identifier = new StringBuilder();
            while (currentPosition < end &&
                   (Character.isLetterOrDigit(buffer.charAt(currentPosition)) ||
                    buffer.charAt(currentPosition) == '_' ||
                    buffer.charAt(currentPosition) == '-')) {
                identifier.append(buffer.charAt(currentPosition));
                currentPosition++;
            }

            String word = identifier.toString();
            currentTokenType = ClassicASPTokenTypes.HTML_ATTRIBUTE;
            // System.out.println("Token: HTML_ATTRIBUTE -> " + word);
            return;
        }

        // 处理等号
        if (currentChar == '=') {
            currentPosition++;
            currentTokenType = ClassicASPTokenTypes.EQUALS;
            // System.out.println("Token: EQUALS");
            return;
        }

        // 处理HTML属性值
        if (currentChar == '"') {
            currentPosition++;
            while (currentPosition < end && buffer.charAt(currentPosition) != '"') {
                currentPosition++;
            }
            if (currentPosition < end) {
                currentPosition++;
            }
            currentTokenType = ClassicASPTokenTypes.HTML_ATTRIBUTE_VALUE;
            // System.out.println("Token: HTML_ATTRIBUTE_VALUE");
            return;
        }

        // 处理HTML文本内容
        if (Character.isLetterOrDigit(currentChar) || currentChar == ' ') {
            StringBuilder text = new StringBuilder();
            while (currentPosition < end &&
                   buffer.charAt(currentPosition) != '<' &&
                   buffer.charAt(currentPosition) != '>') {
                text.append(buffer.charAt(currentPosition));
                currentPosition++;
            }
            currentTokenType = ClassicASPTokenTypes.HTML_TEXT;
            // System.out.println("Token: HTML_TEXT -> " + text.toString().trim());
            return;
        }

        // 处理其他字符
        currentPosition++;
        currentTokenType = TokenType.BAD_CHARACTER;
        // System.out.println("Token: BAD_CHARACTER -> " + currentChar);
    }

    private IElementType getKeywordTokenType(String word) {
        String lowerWord = word.toLowerCase();

        switch (lowerWord) {
            case "dim": return ClassicASPTokenTypes.DIM;
            case "set": return ClassicASPTokenTypes.SET;
            case "if": return ClassicASPTokenTypes.IF;
            case "then": return ClassicASPTokenTypes.THEN;
            case "else": return ClassicASPTokenTypes.ELSE;
            case "for": return ClassicASPTokenTypes.FOR;
            case "next": return ClassicASPTokenTypes.NEXT;
            case "while": return ClassicASPTokenTypes.WHILE;
            case "wend": return ClassicASPTokenTypes.WEND;
            case "do": return ClassicASPTokenTypes.DO;
            case "loop": return ClassicASPTokenTypes.LOOP;
            case "function": return ClassicASPTokenTypes.FUNCTION;
            case "sub": return ClassicASPTokenTypes.SUB;
            case "class": return ClassicASPTokenTypes.CLASS;
            case "public": return ClassicASPTokenTypes.PUBLIC;
            case "private": return ClassicASPTokenTypes.PRIVATE;
            case "response.write": return ClassicASPTokenTypes.RESPONSE_WRITE;
            case "request.form": return ClassicASPTokenTypes.REQUEST_FORM;
            case "request.querystring": return ClassicASPTokenTypes.REQUEST_QUERYSTRING;
            case "server.createobject": return ClassicASPTokenTypes.SERVER_CREATEOBJECT;
            case "session": return ClassicASPTokenTypes.SESSION;
            case "application": return ClassicASPTokenTypes.APPLICATION;
            case "array": return ClassicASPTokenTypes.ARRAY;
            case "each": return ClassicASPTokenTypes.EACH;
            case "in": return ClassicASPTokenTypes.IN;
            case "response": return ClassicASPTokenTypes.RESPONSE;
            case "request": return ClassicASPTokenTypes.REQUEST;
            case "server": return ClassicASPTokenTypes.SERVER;
            case "select": return ClassicASPTokenTypes.SELECT;
            case "case": return ClassicASPTokenTypes.CASE;
            case "property": return ClassicASPTokenTypes.PROPERTY;
            default: return ClassicASPTokenTypes.IDENTIFIER;
        }
    }

    private IElementType getSingleCharTokenType(char c) {
        // 首先检查是否是运算符
        IElementType operatorType = VBScriptOperators.getSingleCharOperator(c);
        if (operatorType != null) {
            return operatorType;
        }

        // 其他符号
        switch (c) {
            case '(': return ClassicASPTokenTypes.LPAREN;
            case ')': return ClassicASPTokenTypes.RPAREN;
            case '[': return ClassicASPTokenTypes.LBRACKET;
            case ']': return ClassicASPTokenTypes.RBRACKET;
            case '.': return ClassicASPTokenTypes.DOT;
            case ':': return ClassicASPTokenTypes.COLON;
            case ',': return ClassicASPTokenTypes.COMMA;
            case ';': return ClassicASPTokenTypes.SEMICOLON;
            // 注意：# 字符在 processASPToken 中专门处理为日期字面量
            default: return TokenType.BAD_CHARACTER;
        }
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

    /**
     * 将关键词转换为大驼峰格式
     * 例如：end if -> End If
     */
    private String normalizeKeyword(String word) {
        String lowerWord = word.toLowerCase();

                    // 检查是否是关键词
            switch (lowerWord) {
                case "if":
                case "then":
                case "else":
                case "end":
                case "for":
                case "next":
                case "while":
                case "wend":
                case "do":
                case "loop":
                case "function":
                case "sub":
                case "dim":
                case "set":
                case "select":
                case "case":
                case "property":
                case "each":
                case "in":
                case "response":
                case "request":
                case "server":
                case "session":
                case "application":
                case "array":
                    // 转换为大驼峰格式
                    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                default:
                    return word; // 非关键词保持原样
            }
    }
}