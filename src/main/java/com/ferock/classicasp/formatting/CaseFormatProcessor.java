package com.ferock.classicasp.formatting;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor;
import com.ferock.classicasp.VBScriptKeywords;
import com.ferock.classicasp.ASPSectionDetector;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class CaseFormatProcessor implements PostFormatProcessor {

    // 添加标志防止重复处理
    private static final ThreadLocal<Boolean> PROCESSING = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public PsiElement processElement(@NotNull PsiElement source, @NotNull CodeStyleSettings settings) {
        return source;
    }

        @Override
    public TextRange processText(@NotNull com.intellij.psi.PsiFile source, @NotNull TextRange rangeToReformat, @NotNull CodeStyleSettings settings) {
        try {
            // 防止重复处理
            if (PROCESSING.get()) {
                return rangeToReformat;
            }

            // 只处理ClassicASP文件
            if (!source.getName().toLowerCase().endsWith(".asp")) {
                return rangeToReformat;
            }

            PROCESSING.set(true);

            // 获取文档
            Document document = PsiDocumentManager.getInstance(source.getProject()).getDocument(source);
            if (document == null) {
                return rangeToReformat;
            }

            // 验证范围有效性
            int docLength = document.getTextLength();
            if (rangeToReformat.getStartOffset() < 0 ||
                rangeToReformat.getEndOffset() > docLength ||
                rangeToReformat.getStartOffset() > rangeToReformat.getEndOffset()) {
                return rangeToReformat;
            }

            // 获取要格式化的文本范围
            String text = document.getText(rangeToReformat);

            // 检查这个范围是否包含ASP代码
            if (!ASPSectionDetector.containsASPCode(text)) {
                // 如果不包含ASP代码，直接返回原范围，不进行格式化
                return rangeToReformat;
            }

            String processedText = SimpleAspFormatter.format(text);

            // 如果有变化，应用更改
            if (!text.equals(processedText)) {
                // 再次验证范围，因为文档可能已被修改
                if (rangeToReformat.getEndOffset() <= document.getTextLength()) {
                    document.replaceString(rangeToReformat.getStartOffset(), rangeToReformat.getEndOffset(), processedText);

                    // 返回新的范围（可能长度改变了）
                    int newEndOffset = rangeToReformat.getStartOffset() + processedText.length();
                    return new TextRange(rangeToReformat.getStartOffset(), newEndOffset);
                }
            }

            return rangeToReformat;
        } catch (Exception e) {
            // 记录错误但不抛出，避免干扰格式化过程
            System.err.println("CaseFormatProcessor error: " + e.getMessage());
            return rangeToReformat;
        } finally {
            PROCESSING.set(false);
        }
    }

}
