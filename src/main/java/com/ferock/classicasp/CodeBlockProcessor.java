package com.ferock.classicasp;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码区块处理器
 * 负责分析源代码的区块嵌套逻辑，返回区块嵌套的规则
 */
public class CodeBlockProcessor {

    /**
     * 代码行信息
     */
    public static class CodeLine {
        public String content;           // 行内容
        public int indentLevel;         // 缩进层级
        public boolean isComment;       // 是否是注释
        public boolean isBlockStart;    // 是否是代码块开始
        public boolean isBlockEnd;      // 是否是代码块结束
        public String blockType;        // 代码块类型
        public int blockLevel;          // 代码块层级（嵌套深度）

        public CodeLine(String content) {
            this.content = content;
            this.indentLevel = 0;
            this.blockLevel = 0;
            this.isComment = content.trim().startsWith("'");
            this.isBlockStart = false;
            this.isBlockEnd = false;
            this.blockType = null;
        }
    }

    /**
     * 区块嵌套规则
     */
    public static class BlockRule {
        public String blockType;        // 区块类型
        public int startLevel;          // 开始层级
        public int endLevel;            // 结束层级
        public int indentLevel;         // 缩进级别
        public List<CodeLine> content;  // 区块内容

        public BlockRule(String blockType) {
            this.blockType = blockType;
            this.startLevel = 0;
            this.endLevel = 0;
            this.indentLevel = 0;
            this.content = new ArrayList<>();
        }
    }

    /**
     * 分析代码区块嵌套逻辑
     * @param lines 代码行数组
     * @return 区块嵌套规则列表
     */
    public static List<BlockRule> analyzeBlockStructure(String[] lines) {
        List<CodeLine> codeLines = new ArrayList<>();
        List<BlockRule> blockRules = new ArrayList<>();

        try {
            // 第一步：分析代码块结构
            analyzeCodeBlocks(lines, codeLines);

            // 第二步：计算区块层级
            calculateBlockLevels(codeLines);

            // 第三步：处理Case的特殊缩进逻辑
            processCaseIndentation(codeLines);

            // 第四步：生成区块嵌套规则
            generateBlockRules(codeLines, blockRules);

        } catch (Exception e) {
            System.err.println("CodeBlockProcessor error: " + e.getMessage());
        }

        return blockRules;
    }

    /**
     * 分析代码块结构
     */
    private static void analyzeCodeBlocks(String[] lines, List<CodeLine> codeLines) {
        for (String line : lines) {
            CodeLine codeLine = new CodeLine(line);
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                codeLines.add(codeLine);
                continue;
            }

            // 识别代码块类型
            String lowerContent = trimmed.toLowerCase();

            // 代码块开始识别
            if (lowerContent.length() >= 3) {
                String prefix = lowerContent.substring(0, 3);
                switch (prefix) {
                    case "if ":
                        codeLine.isBlockStart = true;
                        codeLine.blockType = "if";
                        // System.out.println("🔍 [BLOCK] 识别到If区块开始: " + trimmed);
                        break;
                    case "for":
                        if (lowerContent.startsWith("for ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "for";
                            // System.out.println("🔍 [BLOCK] 识别到For区块开始: " + trimmed);
                        }
                        break;
                    case "do ":
                        codeLine.isBlockStart = true;
                        codeLine.blockType = "do";
                        // System.out.println("🔍 [BLOCK] 识别到Do区块开始: " + trimmed);
                        break;
                }
            }

            if (lowerContent.length() >= 4) {
                String prefix = lowerContent.substring(0, 4);
                switch (prefix) {
                    case "case":
                        if (lowerContent.startsWith("case ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "case";
                            // System.out.println("🔍 [BLOCK] 识别到Case区块开始: " + trimmed);
                        }
                        break;
                }
            }

            if (lowerContent.length() >= 5) {
                String prefix = lowerContent.substring(0, 5);
                switch (prefix) {
                    case "while":
                        if (lowerContent.startsWith("while ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "while";
                            // System.out.println("🔍 [BLOCK] 识别到While区块开始: " + trimmed);
                        }
                        break;
                }
            }

            if (lowerContent.length() >= 6) {
                String prefix = lowerContent.substring(0, 6);
                switch (prefix) {
                    case "select":
                        if (lowerContent.startsWith("select ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "select";
                            // System.out.println("🔍 [BLOCK] 识别到Select区块开始: " + trimmed);
                        }
                        break;
                }
            }

            if (lowerContent.length() >= 8) {
                String prefix = lowerContent.substring(0, 8);
                switch (prefix) {
                    case "function":
                        if (lowerContent.startsWith("function ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "function";
                            // System.out.println("🔍 [BLOCK] 识别到Function区块开始: " + trimmed);
                        }
                        break;
                    case "property":
                        if (lowerContent.startsWith("property ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "property";
                            // System.out.println("🔍 [BLOCK] 识别到Property区块开始: " + trimmed);
                        }
                        break;
                }
            }

            if (lowerContent.length() >= 3) {
                String prefix = lowerContent.substring(0, 3);
                switch (prefix) {
                    case "sub":
                        if (lowerContent.startsWith("sub ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "sub";
                            // System.out.println("🔍 [BLOCK] 识别到Sub区块开始: " + trimmed);
                        }
                        break;
                }
            }

            if (lowerContent.length() >= 5) {
                String prefix = lowerContent.substring(0, 5);
                switch (prefix) {
                    case "class":
                        if (lowerContent.startsWith("class ")) {
                            codeLine.isBlockStart = true;
                            codeLine.blockType = "class";
                            // System.out.println("🔍 [BLOCK] 识别到Class区块开始: " + trimmed);
                        }
                        break;
                }
            }

            // 代码块结束识别
            switch (lowerContent) {
                case "end if":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "if";
                    // System.out.println("🔍 [BLOCK] 识别到If区块结束: " + trimmed);
                    break;
                case "end function":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "function";
                    // System.out.println("🔍 [BLOCK] 识别到Function区块结束: " + trimmed);
                    break;
                case "end sub":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "sub";
                    // System.out.println("🔍 [BLOCK] 识别到Sub区块结束: " + trimmed);
                    break;
                case "end class":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "class";
                    // System.out.println("🔍 [BLOCK] 识别到Class区块结束: " + trimmed);
                    break;
                case "end select":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "select";
                    // System.out.println("🔍 [BLOCK] 识别到Select区块结束: " + trimmed);
                    break;
                case "end property":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "property";
                    // System.out.println("🔍 [BLOCK] 识别到Property区块结束: " + trimmed);
                    break;
                case "next":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "for";
                    // System.out.println("🔍 [BLOCK] 识别到For区块结束: " + trimmed);
                    break;
                case "wend":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "while";
                    // System.out.println("🔍 [BLOCK] 识别到While区块结束: " + trimmed);
                    break;
                case "loop":
                    codeLine.isBlockEnd = true;
                    codeLine.blockType = "do";
                    // System.out.println("🔍 [BLOCK] 识别到Do区块结束: " + trimmed);
                    break;
            }

            codeLines.add(codeLine);
        }
    }

    /**
     * 计算区块层级
     */
    private static void calculateBlockLevels(List<CodeLine> codeLines) {
        int currentBlockLevel = 0;

        for (CodeLine codeLine : codeLines) {
            if (codeLine.content.trim().isEmpty()) {
                continue;
            }

            // 设置当前行的区块层级
            codeLine.blockLevel = currentBlockLevel;

            // 代码块结束，减少层级
            if (codeLine.isBlockEnd) {
                currentBlockLevel = Math.max(0, currentBlockLevel - 1);
            }

            // 代码块开始，增加层级（但Case不增加层级，因为它是Select的子区块）
            if (codeLine.isBlockStart && !codeLine.blockType.equals("case")) {
                currentBlockLevel++;
            }
        }
    }

    /**
     * 处理Case的特殊缩进逻辑
     * Case属于Select的子区块，应该比Select多缩进一级
     */
    private static void processCaseIndentation(List<CodeLine> codeLines) {
        boolean inSelectBlock = false;

        for (CodeLine codeLine : codeLines) {
            if (codeLine.content.trim().isEmpty()) {
                continue;
            }

            // 进入Select区块
            if (codeLine.blockType != null && codeLine.blockType.equals("select") && codeLine.isBlockStart) {
                inSelectBlock = true;
            }

            // 离开Select区块
            if (codeLine.blockType != null && codeLine.blockType.equals("select") && codeLine.isBlockEnd) {
                inSelectBlock = false;
            }

            // 如果是Case，且在Select区块内，增加缩进级别
            if (codeLine.blockType != null && codeLine.blockType.equals("case") && inSelectBlock) {
                codeLine.indentLevel = codeLine.blockLevel + 1;
            }
        }
    }

    /**
     * 生成区块嵌套规则
     */
    private static void generateBlockRules(List<CodeLine> codeLines, List<BlockRule> blockRules) {
        BlockRule currentRule = null;

        for (CodeLine codeLine : codeLines) {
            if (codeLine.content.trim().isEmpty()) {
                continue;
            }

            // 代码块开始，创建新规则
            if (codeLine.isBlockStart) {
                currentRule = new BlockRule(codeLine.blockType);
                currentRule.startLevel = codeLine.blockLevel;
                currentRule.indentLevel = codeLine.blockLevel;
                blockRules.add(currentRule);
            }

            // 添加到当前规则的内容中
            if (currentRule != null) {
                currentRule.content.add(codeLine);
            }

            // 代码块结束，完成当前规则
            if (codeLine.isBlockEnd && currentRule != null) {
                currentRule.endLevel = codeLine.blockLevel;
                currentRule = null;
            }
        }
    }

    /**
     * 获取区块嵌套统计信息
     */
    public static String getBlockStatistics(List<BlockRule> blockRules) {
        if (blockRules == null || blockRules.isEmpty()) {
            return "无代码区块信息";
        }

        StringBuilder stats = new StringBuilder();
        stats.append("代码区块统计:\n");

        // 统计各类型区块数量
        int controlBlocks = 0;
        int loopBlocks = 0;
        int functionBlocks = 0;
        int subBlocks = 0;
        int classBlocks = 0;
        int selectBlocks = 0;
        int propertyBlocks = 0;

        for (BlockRule rule : blockRules) {
            switch (rule.blockType) {
                case "if":
                    controlBlocks++;
                    break;
                case "for":
                case "while":
                case "do":
                    loopBlocks++;
                    break;
                case "function":
                    functionBlocks++;
                    break;
                case "sub":
                    subBlocks++;
                    break;
                case "class":
                    classBlocks++;
                    break;
                case "select":
                    selectBlocks++;
                    break;
                case "case":
                    // Case属于Select，不单独统计
                    break;
                case "property":
                    propertyBlocks++;
                    break;
            }
        }

        stats.append("- 控制结构: ").append(controlBlocks).append("\n");
        stats.append("- 循环结构: ").append(loopBlocks).append("\n");
        stats.append("- 函数结构: ").append(functionBlocks).append("\n");
        stats.append("- 子程序结构: ").append(subBlocks).append("\n");
        stats.append("- 类结构: ").append(classBlocks).append("\n");
        stats.append("- 选择结构: ").append(selectBlocks).append("\n");
        stats.append("- Property结构: ").append(propertyBlocks).append("\n");
        stats.append("- 总区块数: ").append(blockRules.size()).append("\n");

        return stats.toString();
    }
}