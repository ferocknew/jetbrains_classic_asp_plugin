package com.ferock.classicasp.completion;

import com.ferock.classicasp.ASPSectionDetector;
import com.intellij.codeInsight.AutoPopupController;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;

public class ClassicASPTypedHandler extends TypedHandlerDelegate {

    @Override
    public Result charTyped(char c, Project project, Editor editor, PsiFile file) {
        String fileName = file.getName();
        String fileType = file.getFileType() != null ? file.getFileType().getName() : "null";
        String lang = file.getLanguage() != null ? file.getLanguage().getID() : "null";
        log("[ASP][Typed] char='" + c + "' file=" + fileName + ", fileType=" + fileType + ", lang=" + lang);

        if (fileName == null || !fileName.toLowerCase().endsWith(".asp")) {
            log("[ASP][Typed] skip (not .asp)");
            return Result.CONTINUE;
        }

        CharSequence text = editor.getDocument().getCharsSequence();
        int offset = editor.getCaretModel().getOffset();

        boolean inAsp = ASPSectionDetector.containsASPCode(text.toString()) && ASPSectionDetector.isInASPSection(text.toString(), offset);
        if (!inAsp) {
            log("[ASP][Typed] skip (not in ASP): offset=" + offset);
            return Result.CONTINUE;
        } else {
            log("[ASP][Typed] inAsp=true offset=" + offset);
        }

        if (Character.isLetter(c) || c == '_' || c == '.') {
            log("[ASP][Typed] auto-popup request");
            try {
                AutoPopupController controller = AutoPopupController.getInstance(project);
                controller.scheduleAutoPopup(editor);
            } catch (Throwable ignored) {
                // 部分平台API不同，忽略异常
            }
        }
        return Result.CONTINUE;
    }

    private static void log(String msg) {
        // 控制台输出
        System.out.println(msg);
    }
}
