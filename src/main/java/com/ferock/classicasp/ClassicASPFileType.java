package com.ferock.classicasp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ClassicASPFileType extends LanguageFileType {

    public static final ClassicASPFileType INSTANCE = new ClassicASPFileType();

    private ClassicASPFileType() {
        super(ClassicASPLanguage.INSTANCE);
        // System.out.println("=== ClassicASPFileType constructor called ===");
        // System.out.println("=== ClassicASPFileType language: " + ClassicASPLanguage.INSTANCE + " ===");
    }

    @NotNull
    @Override
    public String getName() {
        // System.out.println("=== ClassicASPFileType.getName() called ===");
        return "ClassicASP";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Classic ASP language file with HTML support";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "asp";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null; // 使用默认图标
    }
}