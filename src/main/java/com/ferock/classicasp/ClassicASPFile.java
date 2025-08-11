package com.ferock.classicasp;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class ClassicASPFile extends PsiFileBase {
    public ClassicASPFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ClassicASPLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ClassicASPFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Classic ASP File";
    }
}
