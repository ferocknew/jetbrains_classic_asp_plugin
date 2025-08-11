package com.ferock.classicasp.formatting;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor;
import com.ferock.classicasp.ClassicASPLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理Classic ASP代码的空行格式化
 * 确保控制结构前后有适当的空行
 */
public class BlankLineProcessor implements PostFormatProcessor {

    // 需要前置空行的关键字
    private static final String[] KEYWORDS_NEEDING_BLANK_LINE_BEFORE = {
        "If", "For", "While", "Do", "Function", "Sub", "Select", "Property"
    };

    // 需要后置空行的关键字
    private static final String[] KEYWORDS_NEEDING_BLANK_LINE_AFTER = {
        "End If", "Next", "Wend", "Loop", "End Function", "End Sub", "End Select", "End Property"
    };

    @Override
    public PsiElement processElement(@NotNull PsiElement source, @NotNull CodeStyleSettings settings) {
        return source;
    }

        // 添加标志防止重复处理
    private static final ThreadLocal<Boolean> PROCESSING = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public TextRange processText(@NotNull PsiFile source, @NotNull TextRange rangeToReformat, @NotNull CodeStyleSettings settings) {
        try {
            // 防止重复处理
            if (PROCESSING.get()) {
                return rangeToReformat;
            }

            // 只处理ClassicASP文件
            if (!source.getName().toLowerCase().endsWith(".asp")) {
                return rangeToReformat;
            }

            // 获取文档
            Document document = source.getViewProvider().getDocument();
            if (document == null) {
                return rangeToReformat;
            }

            PROCESSING.set(true);

            String text = document.getText();
            String processedText = addBlankLines(text);

            if (!text.equals(processedText)) {
                document.replaceString(0, text.length(), processedText);
                return new TextRange(0, processedText.length());
            }

            return rangeToReformat;
        } catch (Exception e) {
            e.printStackTrace();
            return rangeToReformat;
        } finally {
            PROCESSING.set(false);
        }
    }

    private String addBlankLines(String text) {
        String result = text;

        // 为关键字前添加空行
        for (String keyword : KEYWORDS_NEEDING_BLANK_LINE_BEFORE) {
            result = addBlankLineBeforeKeyword(result, keyword);
        }

        // 为结束关键字后添加空行
        for (String keyword : KEYWORDS_NEEDING_BLANK_LINE_AFTER) {
            result = addBlankLineAfterKeyword(result, keyword);
        }

        return result;
    }

    private String addBlankLineBeforeKeyword(String text, String keyword) {
        // 简单而有效的方法：查找关键字前没有空行的情况
        String[] lines = text.split("\n", -1); // -1 保留末尾空行
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmedLine = line.trim();

            // 检查是否是目标关键字开头的行
            if (trimmedLine.toLowerCase().startsWith(keyword.toLowerCase() + " ") ||
                trimmedLine.toLowerCase().equals(keyword.toLowerCase())) {

                // 检查前一行是否为空行，并且不是文件开头
                boolean needBlankLine = false;
                if (i > 0) {
                    String prevLine = lines[i - 1].trim();
                    // 如果前一行不为空，且不是ASP标签或注释，则需要空行
                    if (!prevLine.isEmpty() &&
                        !prevLine.startsWith("<%") &&
                        !prevLine.endsWith("%>") &&
                        !prevLine.startsWith("'")) {
                        needBlankLine = true;
                    }
                }

                if (needBlankLine) {
                    result.append("\n");  // 添加空行
                }
            }

            result.append(line);
            if (i < lines.length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    private String addBlankLineAfterKeyword(String text, String keyword) {
        // 简单而有效的方法：查找结束关键字后没有空行的情况
        String[] lines = text.split("\n", -1); // -1 保留末尾空行
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmedLine = line.trim();

            result.append(line);

            // 检查是否是目标结束关键字的行
            if (trimmedLine.toLowerCase().equals(keyword.toLowerCase()) ||
                trimmedLine.toLowerCase().startsWith(keyword.toLowerCase() + " ")) {

                // 检查下一行是否为空行或文件末尾
                boolean needBlankLine = false;
                if (i < lines.length - 1) {
                    String nextLine = lines[i + 1].trim();
                    // 如果下一行不为空，且不是ASP标签结束或另一个结束关键字，则需要空行
                    if (!nextLine.isEmpty() &&
                        !nextLine.startsWith("%>") &&
                        !nextLine.toLowerCase().startsWith("end ") &&
                        !nextLine.toLowerCase().equals("wend") &&
                        !nextLine.toLowerCase().equals("loop") &&
                        !nextLine.toLowerCase().equals("next")) {
                        needBlankLine = true;
                    }
                }

                if (needBlankLine) {
                    result.append("\n");  // 添加换行
                    result.append("\n");  // 添加空行
                } else if (i < lines.length - 1) {
                    result.append("\n");  // 正常换行
                }
            } else if (i < lines.length - 1) {
                result.append("\n");  // 正常换行
            }
        }

        return result.toString();
    }
}