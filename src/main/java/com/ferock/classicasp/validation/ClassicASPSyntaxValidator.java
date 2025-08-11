package com.ferock.classicasp.validation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import org.jetbrains.annotations.NotNull;

public class ClassicASPSyntaxValidator implements Annotator {

    @Override
    public void annotate(@NotNull com.intellij.psi.PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof com.intellij.psi.impl.source.tree.PsiErrorElementImpl) {
            String text = element.getText();
            String snippet = text == null ? "<null>" : (text.length() > 80 ? text.substring(0, 80) + "..." : text);
            System.out.println("[ASP][PsiError] text=" + snippet);
            // 打印错误节点的父节点简述，辅助判断上下文
            com.intellij.psi.PsiElement parent = element.getParent();
            if (parent != null) {
                String parentInfo = parent.getClass().getSimpleName();
                System.out.println("[ASP][PsiError] parent=" + parentInfo);
            }
        }
    }
}
