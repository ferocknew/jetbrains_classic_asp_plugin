package com.ferock.classicasp;

import java.util.List;

/**
 * 层级管理器
 * 负责管理代码块的层级关系和缩进规则
 * 为语法高亮和代码格式化提供统一的层级信息
 */
public class CodeHierarchyManager {

    /**
     * 层级信息类
     */
    public static class HierarchyInfo {
        private int indentLevel;
        private CodeBlockAnalyzer.CodeBlock currentBlock;
        private CodeBlockAnalyzer.CodeBlock parentBlock;
        private List<CodeBlockAnalyzer.CodeBlock> siblingBlocks;

        public HierarchyInfo(int indentLevel, CodeBlockAnalyzer.CodeBlock currentBlock) {
            this.indentLevel = indentLevel;
            this.currentBlock = currentBlock;
        }

        // Getters and setters
        public int getIndentLevel() { return indentLevel; }
        public void setIndentLevel(int indentLevel) { this.indentLevel = indentLevel; }
        public CodeBlockAnalyzer.CodeBlock getCurrentBlock() { return currentBlock; }
        public void setCurrentBlock(CodeBlockAnalyzer.CodeBlock currentBlock) { this.currentBlock = currentBlock; }
        public CodeBlockAnalyzer.CodeBlock getParentBlock() { return parentBlock; }
        public void setParentBlock(CodeBlockAnalyzer.CodeBlock parentBlock) { this.parentBlock = parentBlock; }
        public List<CodeBlockAnalyzer.CodeBlock> getSiblingBlocks() { return siblingBlocks; }
        public void setSiblingBlocks(List<CodeBlockAnalyzer.CodeBlock> siblingBlocks) { this.siblingBlocks = siblingBlocks; }
    }

    /**
     * 获取指定行的层级信息
     * @param blocks 代码块列表
     * @param lineNum 行号
     * @return 层级信息
     */
    public static HierarchyInfo getHierarchyInfo(List<CodeBlockAnalyzer.CodeBlock> blocks, int lineNum) {
        CodeBlockAnalyzer.CodeBlock currentBlock = CodeBlockAnalyzer.getBlockAtLine(blocks, lineNum);

        if (currentBlock == null) {
                            // System.out.println("🔍 [HIERARCHY_DEBUG] 第" + (lineNum+1) + "行: 未找到代码块");
            return new HierarchyInfo(0, null);
        }

        // 计算缩进级别
        int indentLevel = calculateIndentLevel(currentBlock);

        // 查找父块
        CodeBlockAnalyzer.CodeBlock parentBlock = findParentBlock(blocks, currentBlock);

        // System.out.println("🔍 [HIERARCHY_DEBUG] 第" + (lineNum+1) + "行: 找到代码块 " + currentBlock.getType() +
        //      ", 原始indentLevel=" + currentBlock.getIndentLevel() +
        //      ", 计算后indentLevel=" + indentLevel);

        HierarchyInfo info = new HierarchyInfo(indentLevel, currentBlock);
        info.setParentBlock(parentBlock);

        return info;
    }

    /**
     * 计算代码块的缩进级别
     * @param block 代码块
     * @return 缩进级别
     */
    public static int calculateIndentLevel(CodeBlockAnalyzer.CodeBlock block) {
        if (block == null) {
            return 0;
        }

        // 基础缩进级别
        int baseLevel = block.getIndentLevel();

        // 根据代码块类型调整缩进
        switch (block.getType()) {
            case CONTROL:
                return baseLevel + 1; // if语句内容缩进一级
            case LOOP:
                return baseLevel + 1; // 循环内容缩进一级
            case FUNCTION:
                return baseLevel + 1; // 函数内容缩进一级
            case SUB:
                return baseLevel + 1; // 子程序内容缩进一级
            case CLASS:
                return baseLevel + 1; // 类内容缩进一级
            case SELECT:
                return baseLevel + 2; // select case内容缩进两级
            case PROPERTY:
                return baseLevel + 1; // 属性内容缩进一级
            default:
                return baseLevel;
        }
    }

    /**
     * 查找父代码块
     */
    private static CodeBlockAnalyzer.CodeBlock findParentBlock(List<CodeBlockAnalyzer.CodeBlock> blocks,
                                                              CodeBlockAnalyzer.CodeBlock targetBlock) {
        for (CodeBlockAnalyzer.CodeBlock block : blocks) {
            if (block.getChildren().contains(targetBlock)) {
                return block;
            }
            // 递归查找
            CodeBlockAnalyzer.CodeBlock parent = findParentBlock(block.getChildren(), targetBlock);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    /**
     * 应用格式化规则
     * @param content 原始内容
     * @param blocks 代码块列表
     * @return 格式化后的内容
     */
    public static String applyFormattingRules(String content, List<CodeBlockAnalyzer.CodeBlock> blocks) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }

        String[] lines = content.split("\n", -1);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmedLine = line.trim();

            // 检查是否在ASP区域内
            if (!ASPSectionDetector.isLineInASPSection(content, i)) {
                // HTML区域保持原样
                result.append(line).append("\n");
                continue;
            }

            // 获取层级信息
            HierarchyInfo hierarchy = getHierarchyInfo(blocks, i);

            // 应用缩进
            String indentedLine = applyIndentation(line, hierarchy);

            // 应用其他格式化规则
            String formattedLine = applyOtherFormattingRules(indentedLine, hierarchy);

            result.append(formattedLine).append("\n");
        }

        return result.toString();
    }

    /**
     * 应用缩进规则
     */
    private static String applyIndentation(String line, HierarchyInfo hierarchy) {
        if (line == null || line.trim().isEmpty()) {
            return line;
        }

        String trimmedLine = line.trim();
        int indentSpaces = hierarchy.getIndentLevel() * 4;
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentSpaces; i++) {
            indent.append(" ");
        }

        String result = indent.toString() + trimmedLine;

        return result;
    }

    /**
     * 应用其他格式化规则
     */
    private static String applyOtherFormattingRules(String line, HierarchyInfo hierarchy) {
        String result = line;

        // 根据代码块类型应用特定规则
        if (hierarchy.getCurrentBlock() != null) {
            CodeBlockAnalyzer.CodeBlock block = hierarchy.getCurrentBlock();

            switch (block.getType()) {
                case SELECT:
                    // Select Case的特殊处理
                    if (line.trim().toLowerCase().startsWith("case ")) {
                        // Case语句缩进一级
                        result = "    " + line.trim();
                    }
                    break;
                case CONTROL:
                    // 控制结构的特殊处理
                    if (line.trim().toLowerCase().startsWith("else")) {
                        // Else语句与If对齐
                        result = line.trim();
                    }
                    break;
                default:
                    // 其他情况保持原样
                    break;
            }
        }

        return result;
    }

    /**
     * 获取层级统计信息
     */
    public static String getHierarchyStatistics(List<CodeBlockAnalyzer.CodeBlock> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return "无代码块层级信息";
        }

        StringBuilder stats = new StringBuilder();
        stats.append("层级统计信息:\n");

        // 统计各层级的代码块数量
        int maxLevel = getMaxIndentLevel(blocks);
        for (int level = 0; level <= maxLevel; level++) {
            int count = countBlocksAtLevel(blocks, level);
            if (count > 0) {
                stats.append("- 层级 ").append(level).append(": ").append(count).append(" 个代码块\n");
            }
        }

        // 统计嵌套深度
        int maxDepth = getMaxNestingDepth(blocks);
        stats.append("- 最大嵌套深度: ").append(maxDepth).append("\n");

        return stats.toString();
    }

    private static int getMaxIndentLevel(List<CodeBlockAnalyzer.CodeBlock> blocks) {
        int maxLevel = 0;
        for (CodeBlockAnalyzer.CodeBlock block : blocks) {
            maxLevel = Math.max(maxLevel, block.getIndentLevel());
            maxLevel = Math.max(maxLevel, getMaxIndentLevel(block.getChildren()));
        }
        return maxLevel;
    }

    private static int countBlocksAtLevel(List<CodeBlockAnalyzer.CodeBlock> blocks, int level) {
        int count = 0;
        for (CodeBlockAnalyzer.CodeBlock block : blocks) {
            if (block.getIndentLevel() == level) {
                count++;
            }
            count += countBlocksAtLevel(block.getChildren(), level);
        }
        return count;
    }

    private static int getMaxNestingDepth(List<CodeBlockAnalyzer.CodeBlock> blocks) {
        int maxDepth = 0;
        for (CodeBlockAnalyzer.CodeBlock block : blocks) {
            maxDepth = Math.max(maxDepth, block.getIndentLevel() + 1);
            maxDepth = Math.max(maxDepth, getMaxNestingDepth(block.getChildren()));
        }
        return maxDepth;
    }
}