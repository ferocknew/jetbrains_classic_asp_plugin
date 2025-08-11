package com.ferock.classicasp;

import com.intellij.lang.Language;
import com.intellij.psi.LanguageSubstitutor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassicASPLanguageSubstitutor extends LanguageSubstitutor {

    @Override
    public @Nullable Language getLanguage(@NotNull VirtualFile file, @NotNull Project project) {
        // 绝不能在语言替换阶段触发 PSI 创建，否则会导致递归（findFile -> substitutor -> findFile...）
        String ext = file.getExtension();
        if (ext != null && ext.equalsIgnoreCase("asp")) {
            // 调试日志会造成噪音，问题解决后注释掉
            // System.out.println("[ASP][语言替换] 命中 .asp, 替换为 ClassicASP");
            return ClassicASPLanguage.INSTANCE;
        }
        return null; // 不替换
    }
}
