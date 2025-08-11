package com.ferock.classicasp.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.ferock.classicasp.ASPSectionDetector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassicASPGotoDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (sourceElement == null) return PsiElement.EMPTY_ARRAY;
        PsiFile file = sourceElement.getContainingFile();
        if (file == null) return PsiElement.EMPTY_ARRAY;

        String text = file.getText();
        if (text == null || text.isEmpty()) return PsiElement.EMPTY_ARRAY;

        // 仅在 ASP 区域内启用跳转
        boolean inAsp = ASPSectionDetector.containsASPCode(text) && ASPSectionDetector.isInASPSection(text, offset);
        if (!inAsp) return PsiElement.EMPTY_ARRAY;

        // 提取光标下的标识符（大小写不敏感）
        int start = offset - 1;
        while (start >= 0) {
            char c = text.charAt(start);
            if (Character.isLetterOrDigit(c) || c == '_') start--; else break;
        }
        int end = offset;
        while (end < text.length()) {
            char c = text.charAt(end);
            if (Character.isLetterOrDigit(c) || c == '_') end++; else break;
        }
        if (end <= start + 1) return PsiElement.EMPTY_ARRAY;
        String ident = text.substring(start + 1, end);
        String identLower = ident.toLowerCase();

        // 仅在 ASP 段内扫描定义位置，优先向上最近的定义
        int targetOffset = findDefinitionOffset(text, offset, identLower);
        if (targetOffset < 0) return PsiElement.EMPTY_ARRAY;

        PsiElement target = file.findElementAt(targetOffset);
        if (target == null) return PsiElement.EMPTY_ARRAY;

        System.out.println("[ASP][GoTo] 跳转: 标识符='" + ident + "', 目标偏移=" + targetOffset);
        return new PsiElement[]{target};
    }

    private int findDefinitionOffset(@NotNull String text, int fromOffset, @NotNull String identLower) {
        int[][] sections = ASPSectionDetector.getASPSections(text);
        if (sections == null || sections.length == 0) return -1;

        // 正则定义（多行、忽略大小写）
        Pattern dimLine = Pattern.compile("(?im)^\\s*Dim\\b\\s+([^'\\r\\n]+)");
        Pattern classField = Pattern.compile("(?im)^\\s*(Public|Private)\\b\\s+([A-Za-z_][A-Za-z0-9_]*)\\b(?!\\s*\\()");
        Pattern funcDecl = Pattern.compile("(?im)^\\s*(Public|Private)?\\s*(Function|Sub)\\s+([A-Za-z_][A-Za-z0-9_]*)\\b");
        Pattern propDecl = Pattern.compile("(?im)^\\s*(Public|Private)?\\s*Property\\s+(Get|Let|Set)\\s+([A-Za-z_][A-Za-z0-9_]*)\\b");

        int best = -1;

        for (int[] sec : sections) {
            int s = Math.max(0, sec[0]);
            int e = Math.min(text.length(), sec[1]);
            if (s >= e) continue;
            // 仅考虑当前光标之前的 ASP 内容，优先最近的定义
            int scanEnd = Math.min(e, fromOffset);
            if (scanEnd <= s) continue;
            String asp = text.substring(s, scanEnd);

            // Dim a, b, c
            Matcher mDim = dimLine.matcher(asp);
            while (mDim.find()) {
                int groupStart = mDim.start(1);
                String list = mDim.group(1);
                if (list == null) continue;
                int idx = indexOfIdentifier(list, identLower);
                if (idx >= 0) {
                    int abs = s + groupStart + idx;
                    if (abs > best) best = abs;
                }
            }
            // Public/Private foo
            Matcher mField = classField.matcher(asp);
            while (mField.find()) {
                String name = mField.group(2);
                if (name != null && name.equalsIgnoreCase(identLower)) {
                    int abs = s + mField.start(2);
                    if (abs > best) best = abs;
                }
            }
            // Function/Sub name
            Matcher mFn = funcDecl.matcher(asp);
            while (mFn.find()) {
                String name = mFn.group(3);
                if (name != null && name.equalsIgnoreCase(identLower)) {
                    int abs = s + mFn.start(3);
                    if (abs > best) best = abs;
                }
            }
            // Property Get/Let/Set name
            Matcher mProp = propDecl.matcher(asp);
            while (mProp.find()) {
                String name = mProp.group(3);
                if (name != null && name.equalsIgnoreCase(identLower)) {
                    int abs = s + mProp.start(3);
                    if (abs > best) best = abs;
                }
            }
        }
        return best;
    }

    private int indexOfIdentifier(String list, String identLower) {
        int i = 0;
        while (i < list.length()) {
            // 跳过前导空白
            while (i < list.length() && Character.isWhitespace(list.charAt(i))) i++;
            int start = i;
            while (i < list.length()) {
                char c = list.charAt(i);
                if (c == ',' || c == '\r' || c == '\n' || c == '"' || c == '\'') break;
                i++;
            }
            String token = list.substring(start, i).trim();
            if (!token.isEmpty()) {
                // 去掉可能的 As 类型声明：x As Integer
                String name = token.split("\\s+As\\s+", 2)[0].trim();
                if (name.equalsIgnoreCase(identLower)) {
                    // 返回该名称在 list 中的相对偏移
                    int rel = list.toLowerCase().indexOf(name.toLowerCase(), start);
                    return rel >= 0 ? rel : start;
                }
            }
            // 跳过逗号
            while (i < list.length() && list.charAt(i) != ',') i++;
            if (i < list.length() && list.charAt(i) == ',') i++;
        }
        return -1;
    }
}