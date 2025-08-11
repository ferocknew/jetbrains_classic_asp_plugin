package com.ferock.classicasp.formatting;

import com.ferock.classicasp.SafetyLimits;
import com.ferock.classicasp.VBScriptKeywords;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ASP代码缩进处理器
 * 负责：
 * 1. 第一次循环时对文件层级进行正确拆分
 * 2. 根据层级处理缩进
 * 3. 根据层级（代码块）处理前后空行
 */
public class AspIndentProcessor {

    /**
     * 代码行信息
     */
    public static class CodeLine {
        public String content;           // 行内容
        public int indentLevel;         // 缩进层级
        public boolean needBlankBefore; // 前面需要空行
        public boolean needBlankAfter;  // 后面需要空行
        public boolean isBlockStart;    // 是否是代码块开始
        public boolean isBlockEnd;      // 是否是代码块结束
        public String blockType;        // 代码块类型（sub, if, for等）
        public boolean dedentOne;       // 本行需要回退一级缩进（用于 Else/ElseIf 与 If 对齐）
        public boolean isAsp;           // 是否在 ASP 区域（非ASP区域完全不格式化）

        public CodeLine(String content) {
            this.content = content;
            this.indentLevel = 0;
            this.needBlankBefore = false;
            this.needBlankAfter = false;
            this.isBlockStart = false;
            this.isBlockEnd = false;
            this.blockType = "";
            this.dedentOne = false;
            this.isAsp = false;
        }
    }

    /**
     * 处理整个文件的缩进和空行
     * @param lines 原始行数组
     * @return 处理后的代码行列表
     */
    public static List<CodeLine> processIndentation(String[] lines) {
        if (lines == null || lines.length == 0) {
            return new ArrayList<>();
        }

        int maxLines = Math.min(lines.length, SafetyLimits.MAX_FILE_LINES);
        List<CodeLine> codeLines = new ArrayList<>();

        try {
            int currentIndentLevel = 0;
            boolean inASPSection = false;

            for (int i = 0; i < maxLines; i++) {
                String line = lines[i];
                String trimmed = line.trim();

                CodeLine codeLine = new CodeLine(line);

                // ASP 边界
                if (trimmed.startsWith("<%")) {
                    inASPSection = true;
                    codeLine.indentLevel = 0;
                    codeLine.isAsp = true; // ASP标签行
                    codeLines.add(codeLine);
                    continue;
                } else if (trimmed.endsWith("%>")) {
                    inASPSection = false;
                    codeLine.indentLevel = 0;
                    codeLine.isAsp = true; // ASP标签行
                    codeLines.add(codeLine);
                    continue;
                }

                // 空行
                if (trimmed.isEmpty()) {
                    codeLine.indentLevel = 0;
                    codeLine.isAsp = inASPSection; // 空行也标记归属，用于生成阶段原样输出
                    codeLines.add(codeLine);
                    continue;
                }

                if (inASPSection) {
                    codeLine.isAsp = true;
                    boolean isComment = trimmed.startsWith("'");
                    if (isComment) {
                        codeLine.indentLevel = -1;
                    } else {
                        analyzeCodeBlock(codeLine);

                        if (codeLine.isBlockEnd) {
                            currentIndentLevel = Math.max(0, currentIndentLevel - 1);
                        }

                        int baseIndent = currentIndentLevel;
                        if (codeLine.dedentOne) {
                            baseIndent = Math.max(0, baseIndent - 1);
                        }
                        codeLine.indentLevel = baseIndent;

                        if (codeLine.isBlockStart) {
                            currentIndentLevel++;
                        }
                    }
                } else {
                    // 非ASP区域：不做任何缩进/内容分析，仅记录归属
                    codeLine.indentLevel = 0;
                    codeLine.isAsp = false;
                }

                codeLines.add(codeLine);
            }

            // 注释对齐（仅 ASP 区域）
            for (int i = 0; i < codeLines.size(); i++) {
                CodeLine codeLine = codeLines.get(i);
                String trimmed = codeLine.content.trim();
                if (codeLine.isAsp && trimmed.startsWith("'")) {
                    int commentIndentLevel = findCommentIndentLevel(codeLines, i);
                    codeLine.indentLevel = commentIndentLevel;
                    codeLine.content = trimmed;
                }
            }

            // 空行处理（仅 ASP 区域）
            for (int i = 0; i < codeLines.size(); i++) {
                if (codeLines.get(i).isAsp) {
                    processBlankLines(codeLines.get(i), codeLines, i);
                }
            }

            return codeLines;

        } catch (Exception e) {
            System.err.println("AspIndentProcessor error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** 分析代码块信息 */
    private static void analyzeCodeBlock(CodeLine codeLine) {
        String lowerContent = codeLine.content.toLowerCase().trim();

        Set<String> procedureKeywords = VBScriptKeywords.getProcedureKeywords();
        Set<String> conditionKeywords = VBScriptKeywords.getConditionKeywords();
        Set<String> loopKeywords = VBScriptKeywords.getLoopKeywords();
        Set<String> selectionKeywords = VBScriptKeywords.getSelectionKeywords();

        if (lowerContent.startsWith("sub ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "sub";
        } else if (lowerContent.startsWith("function ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "function";
        } else if (lowerContent.startsWith("property ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "property";
        } else if (lowerContent.startsWith("public function ") || lowerContent.startsWith("private function ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "function";
            System.out.println("[FORMAT][Class] access-modified Function start");
        } else if (lowerContent.startsWith("public sub ") || lowerContent.startsWith("private sub ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "sub";
            System.out.println("[FORMAT][Class] access-modified Sub start");
        } else if (lowerContent.startsWith("public property ") || lowerContent.startsWith("private property ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "property";
            System.out.println("[FORMAT][Class] access-modified Property start");
        } else if (lowerContent.startsWith("if ")) {
            if (lowerContent.matches("^if .+ then\\s*$")) {
                codeLine.isBlockStart = true;
            }
            codeLine.blockType = "if";
        } else if (lowerContent.startsWith("elseif ") || lowerContent.startsWith("else if ")) {
            codeLine.blockType = "elseif";
            codeLine.dedentOne = true;
            System.out.println("[FORMAT][IfChain] ElseIf dedent");
        } else if (lowerContent.equals("else")) {
            codeLine.blockType = "else";
            codeLine.dedentOne = true;
            System.out.println("[FORMAT][IfChain] Else dedent");
        } else if (lowerContent.startsWith("select ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "select";
        } else if (lowerContent.startsWith("for ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "for";
        } else if (lowerContent.startsWith("while ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "while";
        } else if (lowerContent.startsWith("do ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "do";
        } else if (lowerContent.startsWith("case ")) {
            codeLine.blockType = "case";
        } else if (lowerContent.equals("do")) {
            codeLine.isBlockStart = true; codeLine.blockType = "do";
        } else if (lowerContent.startsWith("class ")) {
            codeLine.isBlockStart = true; codeLine.blockType = "class";
        }
        // 结束块
        else if (procedureKeywords.contains(lowerContent)) {
            if (lowerContent.equals("end sub")) { codeLine.isBlockEnd = true; codeLine.blockType = "sub"; }
            else if (lowerContent.equals("end function")) { codeLine.isBlockEnd = true; codeLine.blockType = "function"; }
            else if (lowerContent.equals("end property")) { codeLine.isBlockEnd = true; codeLine.blockType = "property"; }
        } else if (conditionKeywords.contains(lowerContent)) {
            if (lowerContent.equals("end if")) { codeLine.isBlockEnd = true; codeLine.blockType = "if"; }
        } else if (selectionKeywords.contains(lowerContent)) {
            if (lowerContent.equals("end select")) { codeLine.isBlockEnd = true; codeLine.blockType = "select"; }
        } else if (loopKeywords.contains(lowerContent)) {
            if (lowerContent.equals("next")) { codeLine.isBlockEnd = true; codeLine.blockType = "for"; }
            else if (lowerContent.equals("wend")) { codeLine.isBlockEnd = true; codeLine.blockType = "while"; }
            else if (lowerContent.equals("loop")) { codeLine.isBlockEnd = true; codeLine.blockType = "do"; }
        } else if (lowerContent.equals("end class")) {
            codeLine.isBlockEnd = true; codeLine.blockType = "class";
        }
    }

    private static void processBlankLines(CodeLine currentLine, List<CodeLine> existingLines, int currentIndex) {
        // 仅在块开始前插入空行；如果上一行已经是空行（或仅空白），则不插入
        if (isBlockNeedingBlankBefore(currentLine)) {
            if (currentIndex > 0) {
                CodeLine prev = existingLines.get(currentIndex - 1);
                String prevTrim = prev.content == null ? "" : prev.content.trim();
                if (!prevTrim.isEmpty() && !prevTrim.startsWith("'")) {
                    currentLine.needBlankBefore = true;
                }
            } else {
                currentLine.needBlankBefore = false;
            }
        }
        // 禁用“块结束后添加空行”的策略，防止与前置空行叠加导致重复空行
        currentLine.needBlankAfter = false;
    }

    private static boolean isBlockNeedingBlankBefore(CodeLine line) {
        if (line.isBlockEnd) return false;
        return line.blockType.equals("sub") || line.blockType.equals("function") || line.blockType.equals("property");
    }

    private static boolean isBlockNeedingBlankAfter(CodeLine line) {
        return false; // 禁用结束后空行策略
    }

    public static String generateFormattedText(List<CodeLine> codeLines) {
        StringBuilder result = new StringBuilder();
        boolean insideSelect = false;
        boolean insideCaseBlock = false;
        int skippedHtmlLines = 0;
        int insertedBlankBefore = 0;

        for (int i = 0; i < codeLines.size(); i++) {
            CodeLine line = codeLines.get(i);
            String trimmedLower = line.content.trim().toLowerCase();

            // 非ASP区域：完全保留原始内容（含缩进与空白）
            if (!line.isAsp) {
                result.append(line.content);
                if (i < codeLines.size() - 1) result.append("\n");
                skippedHtmlLines++;
                continue;
            }

            if (trimmedLower.startsWith("select ")) { insideSelect = true; insideCaseBlock = false; }
            else if (trimmedLower.startsWith("case ")) { insideCaseBlock = true; }
            else if (trimmedLower.equals("end select")) { insideCaseBlock = false; insideSelect = false; }

            if (line.needBlankBefore) { result.append("\n"); insertedBlankBefore++; }

            int caseOffset = 0;
            if (insideCaseBlock) { caseOffset = 1; }

            if (!line.content.isEmpty()) {
                String indent = "    ".repeat(Math.max(0, line.indentLevel + caseOffset));
                result.append(indent).append(line.content.trim());
            }

            if (i < codeLines.size() - 1 || !line.content.isEmpty()) {
                result.append("\n");
            }

            // 已禁用“结束后空行”的策略
        }

        String formatted = result.toString();
        formatted = formatted.replaceAll("\n+$", "\n");
        if (skippedHtmlLines > 0) {
            System.out.println("[FORMAT][SkipHTML] non-ASP lines kept: " + skippedHtmlLines);
        }
        if (insertedBlankBefore > 0) {
            System.out.println("[FORMAT][BlankLines] inserted before blocks: " + insertedBlankBefore);
        }
        return formatted;
    }

    private static int findCommentIndentLevel(List<CodeLine> lines, int commentIndex) {
        for (int i = commentIndex + 1; i < lines.size(); i++) {
            CodeLine codeLine = lines.get(i);
            String trimmed = codeLine.content.trim();
            if (trimmed.isEmpty()) continue;
            if (trimmed.startsWith("'")) continue;
            if (trimmed.equals("%>")) return 0;
            if (trimmed.startsWith("<%")) continue;
            return Math.max(0, codeLine.indentLevel);
        }
        return 0;
    }
}