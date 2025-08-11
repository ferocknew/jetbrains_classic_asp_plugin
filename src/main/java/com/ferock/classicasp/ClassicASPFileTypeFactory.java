package com.ferock.classicasp;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class ClassicASPFileTypeFactory extends FileTypeFactory {

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        System.err.println("!!! ClassicASPFileTypeFactory.createFileTypes 被调用 !!!");
        consumer.consume(ClassicASPFileType.INSTANCE, "asp");
        System.err.println("!!! ASP 文件类型注册完成 !!!");
    }
}