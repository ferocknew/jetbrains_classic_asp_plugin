package com.ferock.classicasp;

/**
 * ASP区域检测器
 * 提供统一的ASP代码区域检测逻辑，供格式化器、语法高亮器等组件使用
 */
public class ASPSectionDetector {

    /**
     * 检查指定位置是否在ASP代码区域内
     * @param content 文件内容
     * @param position 要检查的位置
     * @return 是否在ASP区域内
     */
    public static boolean isInASPSection(String content, int position) {
        if (content == null || position < 0 || position >= content.length()) {
            return false;
        }

        // 简单遍历，只记录ASP区域状态
        boolean inASPSection = false;
        int currentPos = 0;

        while (currentPos < content.length() && currentPos <= position) {
            // 查找ASP开始标签 <%
            if (currentPos + 1 < content.length() &&
                content.charAt(currentPos) == '<' &&
                content.charAt(currentPos + 1) == '%') {
                inASPSection = true;
                currentPos += 2;
                continue;
            }

            // 查找ASP结束标签 %>
            if (currentPos + 1 < content.length() &&
                content.charAt(currentPos) == '%' &&
                content.charAt(currentPos + 1) == '>') {
                inASPSection = false;
                currentPos += 2;
                continue;
            }

            currentPos++;
        }

        return inASPSection;
    }

    /**
     * 检查指定行是否在ASP代码区域内
     * @param content 文件内容
     * @param lineNumber 行号（从0开始）
     * @return 是否在ASP区域内
     */
    public static boolean isLineInASPSection(String content, int lineNumber) {
        if (content == null || lineNumber < 0) {
            return false;
        }

        String[] lines = content.split("\n", -1);
        if (lineNumber >= lines.length) {
            return false;
        }

        // 计算该行的起始位置
        int lineStart = 0;
        for (int i = 0; i < lineNumber; i++) {
            lineStart += lines[i].length() + 1; // +1 for newline
        }

        return isInASPSection(content, lineStart);
    }

    /**
     * 获取ASP区域信息
     * @param content 文件内容
     * @return ASP区域信息数组，每个元素包含 [start, end]
     */
    public static int[][] getASPSections(String content) {
        if (content == null) {
            return new int[0][2];
        }

        java.util.List<int[]> sections = new java.util.ArrayList<>();
        boolean inASPSection = false;
        int startPos = -1;
        int currentPos = 0;

        while (currentPos < content.length()) {
            // 查找ASP开始标签 <%
            if (currentPos + 1 < content.length() &&
                content.charAt(currentPos) == '<' &&
                content.charAt(currentPos + 1) == '%') {

                if (!inASPSection) {
                    startPos = currentPos;
                    inASPSection = true;
                }
                currentPos += 2;
                continue;
            }

            // 查找ASP结束标签 %>
            if (currentPos + 1 < content.length() &&
                content.charAt(currentPos) == '%' &&
                content.charAt(currentPos + 1) == '>') {

                if (inASPSection && startPos >= 0) {
                    sections.add(new int[]{startPos, currentPos + 2});
                    inASPSection = false;
                    startPos = -1;
                }
                currentPos += 2;
                continue;
            }

            currentPos++;
        }

        return sections.toArray(new int[0][2]);
    }

    /**
     * 检查内容是否包含ASP代码
     * @param content 文件内容
     * @return 是否包含ASP代码
     */
    public static boolean containsASPCode(String content) {
        if (content == null) {
            return false;
        }

        return content.contains("<%") && content.contains("%>");
    }

    /**
     * 获取ASP代码行数统计
     * @param content 文件内容
     * @return 包含 [ASP代码行数, HTML行数, 总行数] 的数组
     */
    public static int[] getLineStatistics(String content) {
        if (content == null) {
            return new int[]{0, 0, 0};
        }

        String[] lines = content.split("\n", -1);
        boolean inASPSection = false;
        int aspCodeLines = 0;
        int htmlLines = 0;

        for (String line : lines) {
            String trimmedLine = line.trim();

            // 检测ASP区域边界
            if (trimmedLine.startsWith("<%")) {
                inASPSection = true;
                continue;
            }

            if (trimmedLine.endsWith("%>")) {
                inASPSection = false;
                continue;
            }

            // 统计行数
            if (inASPSection && !trimmedLine.isEmpty()) {
                aspCodeLines++;
            } else if (!inASPSection && !trimmedLine.isEmpty()) {
                htmlLines++;
            }
        }

        return new int[]{aspCodeLines, htmlLines, lines.length};
    }
}