package com.ferock.classicasp;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.ferock.classicasp.psi.ClassicASTypes;
import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 统一的 Classic ASP 语言处理器
 * 整合语法高亮和语法验证逻辑，避免重复解析
 */
public class ClassicASPLanguageProcessor {

    // 缓存处理器
    private static final Map<String, ProcessingResult> resultCache = new HashMap<>();
    private static final long CACHE_TIMEOUT = 30000; // 30秒缓存超时

    /**
     * 处理结果类
     */
    public static class ProcessingResult {
        private final List<TokenInfo> tokens;
        private final List<HighlightInfo> highlights;
        private final List<ValidationError> errors;
        private final long timestamp;

        public ProcessingResult(List<TokenInfo> tokens, List<HighlightInfo> highlights, List<ValidationError> errors) {
            this.tokens = tokens;
            this.highlights = highlights;
            this.errors = errors;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public List<TokenInfo> getTokens() { return tokens; }
        public List<HighlightInfo> getHighlights() { return highlights; }
        public List<ValidationError> getErrors() { return errors; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * Token 信息类
     */
    public static class TokenInfo {
        private final IElementType type;
        private final String text;
        private final int startOffset;
        private final int endOffset;

        public TokenInfo(IElementType type, String text, int startOffset, int endOffset) {
            this.type = type;
            this.text = text;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        // Getters
        public IElementType getType() { return type; }
        public String getText() { return text; }
        public int getStartOffset() { return startOffset; }
        public int getEndOffset() { return endOffset; }
    }

    /**
     * 高亮信息类
     */
    public static class HighlightInfo {
        private final TextRange range;
        private final TextAttributesKey attributes;

        public HighlightInfo(TextRange range, TextAttributesKey attributes) {
            this.range = range;
            this.attributes = attributes;
        }

        // Getters
        public TextRange getRange() { return range; }
        public TextAttributesKey getAttributes() { return attributes; }
    }

    /**
     * 验证错误类
     */
    public static class ValidationError {
        private final TextRange range;
        private final String message;
        private final HighlightSeverity severity;

        public ValidationError(TextRange range, String message, HighlightSeverity severity) {
            this.range = range;
            this.message = message;
            this.severity = severity;
        }

        // Getters
        public TextRange getRange() { return range; }
        public String getMessage() { return message; }
        public HighlightSeverity getSeverity() { return severity; }
    }

    /**
     * 统一处理文件内容
     * @param content 文件内容
     * @param filePath 文件路径（用于缓存）
     * @return 处理结果
     */
    public static ProcessingResult process(String content, String filePath) {
        if (content == null || content.trim().isEmpty()) {
            return new ProcessingResult(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // 生成缓存键
        String cacheKey = generateCacheKey(content, filePath);

        // 检查缓存
        ProcessingResult cachedResult = getCachedResult(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        // 执行统一处理
        ProcessingResult result = performUnifiedProcessing(content);

        // 缓存结果
        cacheResult(cacheKey, result);

        return result;
    }

    /**
     * 执行统一的处理逻辑
     */
    private static ProcessingResult performUnifiedProcessing(String content) {
        // 1. 词法分析
        List<TokenInfo> tokens = performLexicalAnalysis(content);

        // 2. 语义分析
        SemanticAnalyzer.AnalysisResult semantic = SemanticAnalyzer.analyze(content);

        // 3. 生成高亮信息
        List<HighlightInfo> highlights = generateHighlights(tokens, semantic);

        // 4. 生成验证错误
        List<ValidationError> errors = generateValidationErrors(tokens, semantic, content);

        return new ProcessingResult(tokens, highlights, errors);
    }

    /**
     * 执行词法分析
     */
    private static List<TokenInfo> performLexicalAnalysis(String content) {
        List<TokenInfo> tokens = new ArrayList<>();
        com.intellij.lexer.Lexer lexer = new com.intellij.lexer.FlexAdapter(new ClassicASPLexer(null));
        lexer.start(content, 0, content.length(), 0);

        boolean printedDirectiveSample = false;
        int directiveStartIndex = -1;

        while (lexer.getTokenType() != null) {
            IElementType tokenType = lexer.getTokenType();
            int startOffset = lexer.getTokenStart();
            int endOffset = lexer.getTokenEnd();
            String tokenText = content.substring(startOffset, endOffset);

            tokens.add(new TokenInfo(tokenType, tokenText, startOffset, endOffset));

            // 仅采样一次：遇到 <%@ 后收集直到 %> 并打印
            if (!printedDirectiveSample && tokenType.toString().equals("ASP_DIRECTIVE_START")) {
                directiveStartIndex = tokens.size() - 1;
            } else if (directiveStartIndex >= 0 && tokenType.toString().equals("ASP_CLOSE")) {
                StringBuilder sb = new StringBuilder();
                sb.append("[ASP][DirectiveSample]");
                for (int i = directiveStartIndex; i < tokens.size(); i++) {
                    TokenInfo ti = tokens.get(i);
                    String t = ti.getType().toString();
                    String txt = ti.getText();
                    if (txt.length() > 20) txt = txt.substring(0, 20) + "...";
                    sb.append(" | ").append(t).append(':').append(txt);
                }
                System.out.println(sb.toString());
                printedDirectiveSample = true;
                directiveStartIndex = -1;
            }

            lexer.advance();
        }

        return tokens;
    }

    /**
     * 生成高亮信息
     */
    private static List<HighlightInfo> generateHighlights(List<TokenInfo> tokens, SemanticAnalyzer.AnalysisResult semantic) {
        List<HighlightInfo> highlights = new ArrayList<>();
        Map<IElementType, TextAttributesKey> attributesMap = getAttributesMap();

        for (TokenInfo token : tokens) {
            TextAttributesKey attributes = attributesMap.get(token.getType());
            if (attributes != null) {
                TextRange range = new TextRange(token.getStartOffset(), token.getEndOffset());
                highlights.add(new HighlightInfo(range, attributes));
            }
        }

        return highlights;
    }

    /**
     * 生成验证错误
     */
    private static List<ValidationError> generateValidationErrors(List<TokenInfo> tokens,
                                                                SemanticAnalyzer.AnalysisResult semantic,
                                                                String content) {
        List<ValidationError> errors = new ArrayList<>();

        // 暂时不进行任何语法验证，避免生成错误的语法错误提示
        // 注释掉以下验证逻辑
        /*
        // 1. 检查 ASP 标签匹配
        checkASPTagMatching(content, errors);

        // 2. 检查重复的 ASP 开始标签
        checkAdjacentASPTags(content, errors);

        // 3. 检查语义错误
        checkSemanticErrors(tokens, semantic, errors);
        */

        return errors;
    }

    /**
     * 检查 ASP 标签匹配
     */
    private static void checkASPTagMatching(String content, List<ValidationError> errors) {
        int startCount = 0;
        int endCount = 0;
        List<Integer> unmatchedStarts = new ArrayList<>();

        for (int i = 0; i < content.length() - 1; i++) {
            if (content.charAt(i) == '<' && content.charAt(i + 1) == '%') {
                startCount++;
                unmatchedStarts.add(i);
            }
            if (content.charAt(i) == '%' && content.charAt(i + 1) == '>') {
                endCount++;

                if (endCount > startCount) {
                    TextRange range = new TextRange(i, i + 2);
                    errors.add(new ValidationError(range, "ASP标签不匹配", HighlightSeverity.ERROR));
                    return;
                }

                if (!unmatchedStarts.isEmpty()) {
                    unmatchedStarts.remove(unmatchedStarts.size() - 1);
                }
            }
        }

        // 检查未闭合的 ASP 标签
        for (int startPos : unmatchedStarts) {
            TextRange range = new TextRange(startPos, startPos + 2);
            errors.add(new ValidationError(range, "未闭合的ASP标签", HighlightSeverity.ERROR));
        }
    }

    /**
     * 检查紧邻的 ASP 标签
     */
    private static void checkAdjacentASPTags(String content, List<ValidationError> errors) {
        for (int i = 0; i < content.length() - 3; i++) {
            if (content.charAt(i) == '<' &&
                content.charAt(i + 1) == '%' &&
                content.charAt(i + 2) == '<' &&
                content.charAt(i + 3) == '%') {

                TextRange range = new TextRange(i + 2, i + 4);
                errors.add(new ValidationError(range, "重复的ASP开始标签", HighlightSeverity.ERROR));
            }
        }
    }

    /**
     * 检查语义错误
     */
    private static void checkSemanticErrors(List<TokenInfo> tokens,
                                          SemanticAnalyzer.AnalysisResult semantic,
                                          List<ValidationError> errors) {
        // 这里可以添加更多的语义检查逻辑
        // 例如：检查未定义的变量、函数调用错误等
    }

    /**
     * 获取属性映射
     */
    private static Map<IElementType, TextAttributesKey> getAttributesMap() {
        Map<IElementType, TextAttributesKey> attributes = new HashMap<>();

        // ASP 标签
        attributes.put(ClassicASTypes.ASP_OPEN, DefaultLanguageHighlighterColors.BRACES);
        attributes.put(ClassicASTypes.ASP_CLOSE, DefaultLanguageHighlighterColors.BRACES);

        // 关键字
        attributes.put(ClassicASTypes.DIM, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.SET, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.IF, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.THEN, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.ELSE, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.END_IF, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.FOR, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.NEXT, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.WHILE, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.WEND, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.DO, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.LOOP, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.SELECT, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.CASE, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.END_SELECT, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.FUNCTION, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.END_FUNCTION, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.SUB, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASTypes.END_SUB, DefaultLanguageHighlighterColors.KEYWORD);

        // ASP 内置对象
        attributes.put(ClassicASTypes.RESPONSE, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
        attributes.put(ClassicASTypes.REQUEST, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
        attributes.put(ClassicASTypes.SERVER, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
        attributes.put(ClassicASTypes.SESSION, DefaultLanguageHighlighterColors.INSTANCE_FIELD);
        attributes.put(ClassicASTypes.APPLICATION, DefaultLanguageHighlighterColors.INSTANCE_FIELD);

        // 标识符
        attributes.put(ClassicASTypes.IDENTIFIER, DefaultLanguageHighlighterColors.IDENTIFIER);

        // 字符串字面量 - 使用绿色
        attributes.put(ClassicASPTokenTypes.STRING_LITERAL, DefaultLanguageHighlighterColors.STRING);
        attributes.put(com.ferock.classicasp.psi.ClassicASTypes.STRING_LITERAL, DefaultLanguageHighlighterColors.STRING);
        attributes.put(ClassicASPTokenTypes.STRING_START, DefaultLanguageHighlighterColors.STRING);
        attributes.put(ClassicASPTokenTypes.STRING_CONTENT, DefaultLanguageHighlighterColors.STRING);
        attributes.put(ClassicASPTokenTypes.STRING_END, DefaultLanguageHighlighterColors.STRING);

        // ASP指令符号
        attributes.put(ClassicASPTokenTypes.ASP_DIRECTIVE, DefaultLanguageHighlighterColors.KEYWORD);

        // ASP指令关键词
        attributes.put(ClassicASPTokenTypes.LANGUAGE, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASPTokenTypes.OPTION, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASPTokenTypes.EXPLICIT, DefaultLanguageHighlighterColors.KEYWORD);
        attributes.put(ClassicASPTokenTypes.CODEPAGE, DefaultLanguageHighlighterColors.KEYWORD);

        // @ 符号 - 使用关键字颜色
        attributes.put(ClassicASPTokenTypes.AT_SYMBOL, DefaultLanguageHighlighterColors.KEYWORD);

        // 数字字面量
        attributes.put(ClassicASPTokenTypes.NUMBER_LITERAL, DefaultLanguageHighlighterColors.NUMBER);

        // 注释
        attributes.put(com.intellij.psi.TokenType.WHITE_SPACE, DefaultLanguageHighlighterColors.LINE_COMMENT);

        return attributes;
    }

    /**
     * 生成缓存键
     */
    private static String generateCacheKey(String content, String filePath) {
        int contentHash = content.hashCode();
        return filePath + ":" + contentHash;
    }

    /**
     * 获取缓存的结果
     */
    private static ProcessingResult getCachedResult(String cacheKey) {
        ProcessingResult cached = resultCache.get(cacheKey);
        if (cached == null) {
            return null;
        }

        // 检查缓存是否过期
        if (System.currentTimeMillis() - cached.getTimestamp() > CACHE_TIMEOUT) {
            resultCache.remove(cacheKey);
            return null;
        }

        return cached;
    }

    /**
     * 缓存结果
     */
    private static void cacheResult(String cacheKey, ProcessingResult result) {
        resultCache.put(cacheKey, result);
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        resultCache.clear();
    }

    /**
     * 为 Annotator 提供统一的处理接口
     */
    public static void processForAnnotator(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiFile) {
            PsiFile file = (PsiFile) element;
            String content = file.getText();
            String filePath = file.getVirtualFile() != null ? file.getVirtualFile().getPath() : "unknown";

            ProcessingResult result = process(content, filePath);

            // 暂时不应用验证错误，只进行语法高亮
            // 注释掉以下代码，避免生成语法错误提示
            /*
            for (ValidationError error : result.getErrors()) {
                holder.newAnnotation(error.getSeverity(), error.getMessage())
                    .range(error.getRange())
                    .create();
            }
            */
        }
    }
}
