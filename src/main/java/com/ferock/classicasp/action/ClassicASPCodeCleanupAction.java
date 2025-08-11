package com.ferock.classicasp.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Classic ASP 代码清理动作
 * 实现大驼峰转换等功能
 */
public class ClassicASPCodeCleanupAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        Document document = editor.getDocument();
        String originalText = document.getText();

        // 执行代码清理
        String cleanedText = cleanupCode(originalText);

        // 如果文本有变化，则应用更改
        if (!originalText.equals(cleanedText)) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                document.setText(cleanedText);
            });
        }
    }

    /**
     * 清理代码，实现大驼峰转换等功能
     */
    private String cleanupCode(String code) {
        // 关键词映射 - 按长度从长到短排序，避免部分匹配问题
        String[][] keywordMappings = {
            // 复合关键词先处理
            {"end if", "End If"},
            {"end function", "End Function"},
            {"end sub", "End Sub"},
            {"end for", "End For"},
            {"end while", "End While"},
            {"end do", "End Do"},
            {"end loop", "End Loop"},
            {"end select", "End Select"},
            {"end property", "End Property"},
            // 单个关键词
            {"if", "If"},
            {"then", "Then"},
            {"else", "Else"},
            {"for", "For"},
            {"next", "Next"},
            {"while", "While"},
            {"wend", "Wend"},
            {"do", "Do"},
            {"loop", "Loop"},
            {"function", "Function"},
            {"sub", "Sub"},
            {"dim", "Dim"},
            {"set", "Set"},
            {"select", "Select"},
            {"case", "Case"},
            {"property", "Property"},
            {"each", "Each"},
            {"in", "In"},
            {"response", "Response"},
            {"request", "Request"},
            {"server", "Server"},
            {"session", "Session"},
            {"application", "Application"},
            {"array", "Array"}
        };

        String result = code;

        // 应用关键词转换 - 使用不区分大小写的正则表达式
        for (String[] mapping : keywordMappings) {
            String original = mapping[0];
            String properCase = mapping[1];

            // 使用正则表达式确保只替换独立的单词，不区分大小写
            String regex = "(?i)\\b" + original.replace(" ", "\\s+") + "\\b";
            result = result.replaceAll(regex, properCase);
        }

        return result;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        // 只在 Classic ASP 文件中启用
        boolean enabled = project != null && psiFile != null &&
                        psiFile.getName().toLowerCase().endsWith(".asp");

        e.getPresentation().setEnabledAndVisible(enabled);
    }
}