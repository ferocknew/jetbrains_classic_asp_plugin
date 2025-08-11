package com.ferock.classicasp.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Classic ASP 键盘调试动作
 * 用于调试和记录用户按下的快捷键
 * 快捷键：Ctrl + Alt + D
 */
public class ClassicASPKeyboardDebugAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        // 记录详细的调试信息
        logDebugInfo(e, project, editor);

        // 显示简单的通知
        if (project != null) {
            // Messages.showInfoMessage(project,
                                  // "键盘事件已记录到控制台\n请查看IDE控制台输出",
                  // "Classic ASP Plugin Debug");
        }
    }

    private void logDebugInfo(AnActionEvent e, Project project, Editor editor) {
        // System.out.println("\n=== Classic ASP Plugin Debug Event ===");
        // System.out.println("Timestamp: " + System.currentTimeMillis());
        // System.out.println("Event Type: Keyboard Debug Action");

        // 项目信息
        // System.out.println("Project: " + (project != null ? project.getName() : "null"));
        // System.out.println("Project Base Path: " + (project != null ? project.getBasePath() : "null"));

        // 编辑器信息
        // System.out.println("Editor: " + (editor != null ? "available" : "null"));

        if (editor != null) {
            // 文件信息
            String fileName = editor.getVirtualFile() != null ?
                editor.getVirtualFile().getName() : "unknown";
            String filePath = editor.getVirtualFile() != null ?
                editor.getVirtualFile().getPath() : "unknown";
            // System.out.println("Current File: " + fileName);
            // System.out.println("File Path: " + filePath);

            // 光标信息
            int offset = editor.getCaretModel().getOffset();
            // System.out.println("Cursor Offset: " + offset);

            // 光标位置
            try {
                int line = editor.getCaretModel().getLogicalPosition().line;
                int column = editor.getCaretModel().getLogicalPosition().column;
                // System.out.println("Cursor Position: Line " + (line + 1) + ", Column " + (column + 1));
            } catch (Exception ex) {
                // System.out.println("Error getting cursor position: " + ex.getMessage());
            }

            // 选中文本
            String selectedText = editor.getSelectionModel().getSelectedText();
            if (selectedText != null && !selectedText.isEmpty()) {
                            // System.out.println("Selected Text: " + selectedText);
            // System.out.println("Selection Length: " + selectedText.length());
            }

            // 当前行文本
            try {
                int lineNumber = editor.getCaretModel().getLogicalPosition().line;
                int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);
                int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
                String lineText = editor.getDocument().getText().substring(lineStartOffset, lineEndOffset);
                // System.out.println("Current Line (" + (lineNumber + 1) + "): " + lineText);
            } catch (Exception ex) {
                // System.out.println("Error getting current line: " + ex.getMessage());
            }

            // 文档信息
            // System.out.println("Document Length: " + editor.getDocument().getTextLength());
            // System.out.println("Line Count: " + editor.getDocument().getLineCount());
        }

        // 动作信息
        String actionId = e.getActionManager().getId(this);
        String presentationText = e.getPresentation().getText();
        String description = e.getPresentation().getDescription();
        // System.out.println("Action ID: " + actionId);
        // System.out.println("Action Text: " + presentationText);
        // System.out.println("Action Description: " + description);

        // 数据上下文信息
        // System.out.println("Data Context: Available");
        // System.out.println("  Project: " + (e.getData(CommonDataKeys.PROJECT) != null ? "available" : "null"));
        // System.out.println("  Editor: " + (e.getData(CommonDataKeys.EDITOR) != null ? "available" : "null"));
        // System.out.println("  Virtual File: " + (e.getData(CommonDataKeys.VIRTUAL_FILE) != null ? "available" : "null"));

        // System.out.println("=== End Debug Event ===\n");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // 确保动作始终可用
        e.getPresentation().setEnabledAndVisible(true);
    }
}