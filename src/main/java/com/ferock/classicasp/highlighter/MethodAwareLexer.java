package com.ferock.classicasp.highlighter;

import com.ferock.classicasp.SpecRegistry;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * 轻量级语义包装词法器：
 * 将 DOT 后紧邻的 IDENTIFIER/WRITE 在满足对象方法或系统函数的情况下重写为 OBJECT_METHOD。
 */
public class MethodAwareLexer extends LexerBase {
    private final Lexer delegate;

    private CharSequence buffer;
    private int endOffset;

    private int tokenStart;
    private int tokenEnd;
    private IElementType tokenType;

    private IElementType lastSignificantTokenType;
    private boolean rewriteLogEmitted = false;

    // 为了判定对象名，记录上一个标识符内容
    private String lastIdentifierText = null;

    public MethodAwareLexer() {
        this.delegate = new FlexAdapter(new com.ferock.classicasp.ClassicASPLexer(null));
    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.endOffset = endOffset;
        this.lastSignificantTokenType = null;
        this.rewriteLogEmitted = false;
        this.lastIdentifierText = null;

        delegate.start(buffer, startOffset, endOffset, initialState);
        pullAndRewrite();
    }

    @Override
    public int getState() { return delegate.getState(); }

    @Override
    public IElementType getTokenType() { return tokenType; }

    @Override
    public int getTokenStart() { return tokenStart; }

    @Override
    public int getTokenEnd() { return tokenEnd; }

    @Override
    public void advance() {
        if (tokenType != null && !isWhitespaceOrComment(tokenType)) {
            lastSignificantTokenType = tokenType;
            // 记录最近一次可作为“对象名”的文本
            if (tokenType == com.ferock.classicasp.psi.ClassicASTypes.IDENTIFIER ||
                tokenType == com.ferock.classicasp.psi.ClassicASTypes.WRITE ||
                tokenType == com.ferock.classicasp.psi.ClassicASTypes.RESPONSE ||
                tokenType == com.ferock.classicasp.psi.ClassicASTypes.REQUEST ||
                tokenType == com.ferock.classicasp.psi.ClassicASTypes.SERVER ||
                tokenType == com.ferock.classicasp.psi.ClassicASTypes.SESSION ||
                tokenType == com.ferock.classicasp.psi.ClassicASTypes.APPLICATION ||
                tokenType == ClassicASPTokenTypes.ERR) {
                lastIdentifierText = buffer.subSequence(tokenStart, tokenEnd).toString();
            }
        }
        delegate.advance();
        pullAndRewrite();
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() { return buffer; }

    @Override
    public int getBufferEnd() { return endOffset; }

    private void pullAndRewrite() {
        tokenType = delegate.getTokenType();
        if (tokenType == null) {
            tokenStart = tokenEnd = endOffset;
            return;
        }
        tokenStart = delegate.getTokenStart();
        tokenEnd = delegate.getTokenEnd();

        // 规则A：将 On / Error / Resume / Err / Goto 等识别为关键字（词法层）
        if (tokenType == com.ferock.classicasp.psi.ClassicASTypes.IDENTIFIER) {
            String text = buffer.subSequence(tokenStart, tokenEnd).toString();
            String lower = text.toLowerCase();
            if ("on".equals(lower)) {
                tokenType = ClassicASPTokenTypes.ON;
            } else if ("error".equals(lower)) {
                tokenType = ClassicASPTokenTypes.ERROR;
            } else if ("resume".equals(lower)) {
                tokenType = ClassicASPTokenTypes.RESUME;
            } else if ("err".equals(lower)) {
                tokenType = ClassicASPTokenTypes.ERR;
            } else if ("goto".equals(lower)) {
                tokenType = ClassicASPTokenTypes.GOTO;
            } else {
                // YAML 驱动：若存在于 keywords.* 任一分组，则作为通用关键字高亮
                Set<String> yamlKeywords = SpecRegistry.getKeywords();
                if (yamlKeywords != null && yamlKeywords.contains(lower)) {
                    tokenType = ClassicASPTokenTypes.KEYWORD_GENERIC;
                }
            }
        }

        if (isMethodNameCandidate(tokenType) && isAfterDotWithKnownObject()) {
            tokenType = ClassicASPTokenTypes.OBJECT_METHOD;
            if (!rewriteLogEmitted) {
                System.out.println("[ASP][MethodAwareLexer] rewrite to OBJECT_METHOD at [" + tokenStart + "," + tokenEnd + "]");
                rewriteLogEmitted = true;
            }
        }
    }

    private boolean isAfterDotWithKnownObject() {
        if (lastSignificantTokenType != com.ferock.classicasp.psi.ClassicASTypes.DOT) return false;
        // DOT 之前应当是对象名；我们已在 advance 时记录了上一个标识符文本
        // 这里需要再往前取一次：在 DOT 之前的上一个显著 token 应该是 IDENTIFIER（对象名）
        // 简化：使用 lastIdentifierText 作为对象名候选
        String methodName = buffer.subSequence(tokenStart, tokenEnd).toString();
        String objectName = lastIdentifierText != null ? lastIdentifierText : "";
        if (objectName.isEmpty()) return false;

        Map<String, Set<String>> objMethods = SpecRegistry.getObjectToMethods();
        Set<String> sysFns = SpecRegistry.getSysFunctions();
        Set<String> methods = objMethods.get(objectName.toLowerCase());
        if (methods != null && methods.contains(methodName.toLowerCase())) {
            return true;
        }
        // 兜底：允许系统函数在 DOT 后也着成方法名（例如自定义对象也可能调用内置函数名）
        return sysFns != null && sysFns.contains(methodName.toLowerCase());
    }

    private boolean isMethodNameCandidate(IElementType type) {
        return type == com.ferock.classicasp.psi.ClassicASTypes.IDENTIFIER
                || type == com.ferock.classicasp.psi.ClassicASTypes.WRITE;
    }

    private boolean isWhitespaceOrComment(IElementType type) {
        return type == TokenType.WHITE_SPACE
                || type == com.ferock.classicasp.psi.ClassicASTypes.WHITE_SPACE
                || type == com.ferock.classicasp.psi.ClassicASTypes.COMMENT;
    }
}
