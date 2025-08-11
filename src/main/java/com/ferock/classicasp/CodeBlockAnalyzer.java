package com.ferock.classicasp;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 代码块分析器
 * 负责识别和分析ASP代码中的各种代码块结构
 * 包括控制结构、循环结构、函数结构等
 */
public class CodeBlockAnalyzer {

    /**
     * 代码块类型枚举
     */
    public enum BlockType {
        CONTROL("控制结构", "if", "else", "end if"),
        LOOP("循环结构", "for", "next", "while", "wend", "do", "loop"),
        FUNCTION("函数结构", "function", "end function"),
        SUB("子程序结构", "sub", "end sub"),
        CLASS("类结构", "class", "end class"),
        SELECT("选择结构", "select", "case", "end select"),
        PROPERTY("属性结构", "property", "end property");

        private final String description;
        private final String[] keywords;

        BlockType(String description, String... keywords) {
            this.description = description;
            this.keywords = keywords;
        }

        public String getDescription() {
            return description;
        }

        public String[] getKeywords() {
            return keywords;
        }
    }

    /**
     * 代码块信息类
     */
    public static class CodeBlock {
        private BlockType type;
        private int startLine;
        private int endLine;
        private int indentLevel;
        private String startKeyword;
        private String endKeyword;
        private List<CodeBlock> children;

        public CodeBlock(BlockType type, int startLine, String startKeyword) {
            this.type = type;
            this.startLine = startLine;
            this.startKeyword = startKeyword;
            this.children = new ArrayList<>();
        }

        // Getters and setters
        public BlockType getType() { return type; }
        public int getStartLine() { return startLine; }
        public int getEndLine() { return endLine; }
        public void setEndLine(int endLine) { this.endLine = endLine; }
        public int getIndentLevel() { return indentLevel; }
        public void setIndentLevel(int indentLevel) { this.indentLevel = indentLevel; }
        public String getStartKeyword() { return startKeyword; }
        public String getEndKeyword() { return endKeyword; }
        public void setEndKeyword(String endKeyword) { this.endKeyword = endKeyword; }
        public List<CodeBlock> getChildren() { return children; }

        @Override
        public String toString() {
            return String.format("%s[%d-%d] %s", type.getDescription(), startLine, endLine, startKeyword);
        }
    }

    /**
     * 分析代码块结构
     * @param content 文件内容
     * @return 代码块列表
     */
    public static List<CodeBlock> analyzeCodeBlocks(String content) {
        if (content == null || content.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<CodeBlock> blocks = new ArrayList<>();
        String[] lines = content.split("\n", -1);
        Stack<CodeBlock> blockStack = new Stack<>();

        for (int lineNum = 0; lineNum < lines.length; lineNum++) {
            String line = lines[lineNum].trim();

            // 检查是否在ASP区域内
            if (!ASPSectionDetector.isLineInASPSection(content, lineNum)) {
                continue; // 跳过HTML区域
            }

            // 识别开始关键字
            CodeBlock newBlock = identifyStartBlock(line, lineNum);
            if (newBlock != null) {
                newBlock.setIndentLevel(blockStack.size());
                blockStack.push(newBlock);
                blocks.add(newBlock);
                // System.out.println("🔍 [BLOCK_DEBUG] 第" + (lineNum+1) + "行: 开始代码块 " + newBlock.getType() + " [" + newBlock.getStartKeyword() + "]");
                continue;
            }

            // 识别结束关键字
            CodeBlock closedBlock = identifyEndBlock(line, blockStack);
            if (closedBlock != null) {
                closedBlock.setEndLine(lineNum);
                closedBlock.setEndKeyword(extractKeyword(line));
                // System.out.println("🔍 [BLOCK_DEBUG] 第" + (lineNum+1) + "行: 结束代码块 " + closedBlock.getType() + " [" + closedBlock.getEndKeyword() + "]");

                // 更新父子关系
                if (!blockStack.isEmpty()) {
                    CodeBlock parent = blockStack.peek();
                    if (parent != closedBlock) {
                        parent.getChildren().add(closedBlock);
                    }
                }
            }
        }

        // 处理未闭合的代码块
        for (CodeBlock block : blocks) {
            if (block.getEndLine() == -1) {
                block.setEndLine(lines.length - 1);
                // System.out.println("🔍 [BLOCK_DEBUG] 未闭合代码块 " + block.getType() + " [" + block.getStartKeyword() + "] -> 设置结束行: " + (block.getEndLine()+1));
            }
        }

        return blocks;
    }

    /**
     * 识别开始代码块
     */
    private static CodeBlock identifyStartBlock(String line, int lineNum) {
        String lowerLine = line.toLowerCase();

        // 检查各种代码块类型，只识别开始关键字
        if (lowerLine.startsWith("if ") || lowerLine.equals("if")) {
            return new CodeBlock(BlockType.CONTROL, lineNum, "if");
        }
        if (lowerLine.startsWith("for ") || lowerLine.equals("for")) {
            return new CodeBlock(BlockType.LOOP, lineNum, "for");
        }
        if (lowerLine.startsWith("while ") || lowerLine.equals("while")) {
            return new CodeBlock(BlockType.LOOP, lineNum, "while");
        }
        if (lowerLine.startsWith("do ") || lowerLine.equals("do")) {
            return new CodeBlock(BlockType.LOOP, lineNum, "do");
        }
        if (lowerLine.startsWith("function ") || lowerLine.equals("function")) {
            return new CodeBlock(BlockType.FUNCTION, lineNum, "function");
        }
        if (lowerLine.startsWith("sub ") || lowerLine.equals("sub")) {
            return new CodeBlock(BlockType.SUB, lineNum, "sub");
        }
        if (lowerLine.startsWith("class ") || lowerLine.equals("class")) {
            return new CodeBlock(BlockType.CLASS, lineNum, "class");
        }
        if (lowerLine.startsWith("select ") || lowerLine.equals("select")) {
            return new CodeBlock(BlockType.SELECT, lineNum, "select");
        }
        if (lowerLine.startsWith("property ") || lowerLine.equals("property")) {
            return new CodeBlock(BlockType.PROPERTY, lineNum, "property");
        }

        return null;
    }

    /**
     * 识别结束代码块
     */
    private static CodeBlock identifyEndBlock(String line, Stack<CodeBlock> blockStack) {
        if (blockStack.isEmpty()) {
            return null;
        }

        String lowerLine = line.toLowerCase();
        CodeBlock currentBlock = blockStack.peek();

        // 检查是否匹配当前块的结束关键字
        for (String keyword : currentBlock.getType().getKeywords()) {
            if (lowerLine.startsWith(keyword.toLowerCase() + " ") ||
                lowerLine.equals(keyword.toLowerCase())) {
                // 检查是否是结束关键字
                if (isEndKeyword(currentBlock.getType(), keyword)) {
                    blockStack.pop();
                    return currentBlock;
                }
            }
        }

        return null;
    }

    /**
     * 判断是否为结束关键字
     */
    private static boolean isEndKeyword(BlockType type, String keyword) {
        switch (type) {
            case CONTROL:
                return keyword.equalsIgnoreCase("end if");
            case LOOP:
                return keyword.equalsIgnoreCase("next") ||
                       keyword.equalsIgnoreCase("wend") ||
                       keyword.equalsIgnoreCase("loop");
            case FUNCTION:
                return keyword.equalsIgnoreCase("end function");
            case SUB:
                return keyword.equalsIgnoreCase("end sub");
            case CLASS:
                return keyword.equalsIgnoreCase("end class");
            case SELECT:
                return keyword.equalsIgnoreCase("end select");
            case PROPERTY:
                return keyword.equalsIgnoreCase("end property");
            default:
                return false;
        }
    }

    /**
     * 提取关键字
     */
    private static String extractKeyword(String line) {
        String[] parts = line.split("\\s+", 2);
        return parts[0];
    }

    /**
     * 获取指定行的代码块信息
     */
    public static CodeBlock getBlockAtLine(List<CodeBlock> blocks, int lineNum) {
        for (CodeBlock block : blocks) {
            if (lineNum >= block.getStartLine() && lineNum <= block.getEndLine()) {
                return block;
            }
            // 递归检查子块
            CodeBlock childBlock = getBlockAtLine(block.getChildren(), lineNum);
            if (childBlock != null) {
                return childBlock;
            }
        }
        return null;
    }

    /**
     * 获取代码块统计信息
     */
    public static String getBlockStatistics(List<CodeBlock> blocks) {
        int totalBlocks = countBlocks(blocks);
        int maxDepth = getMaxDepth(blocks);

        StringBuilder stats = new StringBuilder();
        stats.append("代码块统计:\n");
        stats.append("- 总代码块数: ").append(totalBlocks).append("\n");
        stats.append("- 最大嵌套深度: ").append(maxDepth).append("\n");

        // 按类型统计
        for (BlockType type : BlockType.values()) {
            int count = countBlocksByType(blocks, type);
            if (count > 0) {
                stats.append("- ").append(type.getDescription()).append(": ").append(count).append("\n");
            }
        }

        return stats.toString();
    }

    private static int countBlocks(List<CodeBlock> blocks) {
        int count = blocks.size();
        for (CodeBlock block : blocks) {
            count += countBlocks(block.getChildren());
        }
        return count;
    }

    private static int countBlocksByType(List<CodeBlock> blocks, BlockType type) {
        int count = 0;
        for (CodeBlock block : blocks) {
            if (block.getType() == type) {
                count++;
            }
            count += countBlocksByType(block.getChildren(), type);
        }
        return count;
    }

    private static int getMaxDepth(List<CodeBlock> blocks) {
        int maxDepth = 0;
        for (CodeBlock block : blocks) {
            maxDepth = Math.max(maxDepth, block.getIndentLevel());
            maxDepth = Math.max(maxDepth, getMaxDepth(block.getChildren()));
        }
        return maxDepth;
    }
}