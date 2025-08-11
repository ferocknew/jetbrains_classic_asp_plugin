package com.ferock.classicasp;

import com.intellij.lang.Language;
import com.intellij.psi.LanguageSubstitutor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassicASPLanguageSubstitutor extends LanguageSubstitutor {

    @Override
    public @Nullable Language getLanguage(@NotNull VirtualFile file, @NotNull Project project) {
        String name = file.getName();
        String ext = file.getExtension();
        String fileType = file.getFileType() != null ? file.getFileType().getName() : "null";
        String psiLang = "未知";
        try {
            PsiFile psi = PsiManager.getInstance(project).findFile(file);
            if (psi != null && psi.getLanguage() != null) {
                psiLang = psi.getLanguage().getID();
            } else {
                psiLang = "空(可能未创建PSI)";
            }
        } catch (Throwable ignored) {
            psiLang = "获取失败";
        }

        if (ext != null && ext.equalsIgnoreCase("asp")) {
            System.out.println("[ASP][语言替换] 命中 .asp -> 文件=" + name + ", 扩展名=" + ext + ", 文件类型=" + fileType + ", 原PSI语言=" + psiLang + ", 替换为=ClassicASP");
            return ClassicASPLanguage.INSTANCE;
        }
        return null; // 不替换
    }
}
