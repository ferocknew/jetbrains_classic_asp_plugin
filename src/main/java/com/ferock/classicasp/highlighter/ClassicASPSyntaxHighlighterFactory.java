package com.ferock.classicasp.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 统一的语法高亮器工厂
 * 使用 UnifiedClassicASPSyntaxHighlighter
 */
public class ClassicASPSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile) {
        String fileName = virtualFile != null ? virtualFile.getName() : "null";
        String ext = virtualFile != null ? virtualFile.getExtension() : "null";
        String fileType = (virtualFile != null && virtualFile.getFileType() != null) ? virtualFile.getFileType().getName() : "null";
        String psiLang = "未知";
        try {
            if (project != null && virtualFile != null) {
                PsiFile psi = PsiManager.getInstance(project).findFile(virtualFile);
                if (psi != null && psi.getLanguage() != null) {
                    psiLang = psi.getLanguage().getID();
                } else {
                    psiLang = "空(可能未创建PSI)";
                }
            }
        } catch (Throwable ignored) {
            psiLang = "获取失败";
        }
        System.out.println("[ASP][高亮] 创建语法高亮器 -> 文件=" + fileName + ", 扩展名=" + ext + ", 文件类型=" + fileType + ", PSI语言=" + psiLang + "。说明：禁用解析后PSI语言可能为TEXT，但仍可按文件类型进行高亮。");
        return new UnifiedClassicASPSyntaxHighlighter();
    }
}