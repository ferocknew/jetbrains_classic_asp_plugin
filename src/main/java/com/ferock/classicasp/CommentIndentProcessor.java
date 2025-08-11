package com.ferock.classicasp;

import java.util.List;

/**
 * 注释缩进处理器
 * 专门处理注释的缩进逻辑，避免累积缩进问题
 */
public class CommentIndentProcessor {

    /**
     * 处理注释缩进
     * @param lines 代码行数组
     * @param blocks 代码块列表，用于计算缩进级别
     * @return 处理后的代码行数组
     */
    public static String[] processCommentIndentation(String[] lines, List<CodeBlockAnalyzer.CodeBlock> blocks) {
        String[] result = new String[lines.length];

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();

            if (trimmed.startsWith("'")) {
                // 检查注释是否已经有缩进（避免重复处理）
                if (line.startsWith(" ") && !line.trim().equals(trimmed)) {
                    // 注释已经有缩进，直接返回原行
                    // System.out.println("🔍 [COMMENT_INDENT_DEBUG] 注释已有缩进，跳过处理: " + trimmed);
                    result[i] = line;
                    continue;
                }

                // 注释行：使用和代码相同的缩进逻辑
                CodeHierarchyManager.HierarchyInfo hierarchy = CodeHierarchyManager.getHierarchyInfo(blocks, i);
                int indentLevel = hierarchy.getIndentLevel();

                // System.out.println("🔍 [COMMENT_INDENT_DEBUG] 处理注释: '" + trimmed + "', 基于代码块层级的缩进级别: " + indentLevel);

                if (indentLevel == 0) {
                    // 顶级注释不缩进
                    // System.out.println("🔍 [COMMENT_INDENT_DEBUG] 顶级注释不缩进");
                    result[i] = trimmed;
                } else {
                    // 使用和代码相同的缩进逻辑
                    int indentSpaces = indentLevel * 4;
                    StringBuilder indent = new StringBuilder();
                    for (int j = 0; j < indentSpaces; j++) {
                        indent.append(" ");
                    }
                    String indentedComment = indent.toString() + trimmed;
                    // System.out.println("🔍 [COMMENT_INDENT_DEBUG] 注释缩进结果: '" + indentedComment + "'");
                    result[i] = indentedComment;
                }
            } else {
                // 非注释行保持原样
                result[i] = line;
            }
        }

        return result;
    }

    /**
     * 兼容旧版本的接口
     */
    public static String[] processCommentIndentation(String[] lines) {
        // 如果没有提供代码块信息，使用原来的逻辑
        return processCommentIndentation(lines, null);
    }
}