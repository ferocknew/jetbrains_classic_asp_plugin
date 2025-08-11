package com.ferock.classicasp.formatting;

import com.ferock.classicasp.SpecRegistry;
import com.ferock.classicasp.VBScriptKeywords;
import com.ferock.classicasp.SafetyLimits;
import com.ferock.classicasp.ASPSectionDetector;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

/**
 * 简单的ASP格式化器
 */
public class SimpleAspFormatter {

    public static String format(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "";
        }
        if (!SafetyLimits.isTextSafeToProcess(content, "SimpleAspFormatter")) {
            return content;
        }
        try {
            String[] lines = content.split("\n", -1);
            var codeLines = AspIndentProcessor.processIndentation(lines);
            for (int i = 0; i < codeLines.size(); i++) {
                var codeLine = codeLines.get(i);
                String line = codeLine.content;
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("<%") || trimmedLine.endsWith("%>")) {
                    continue;
                }
                boolean inASPSection = ASPSectionDetector.isLineInASPSection(content, i);
                if (inASPSection) {
                    codeLine.content = formatASPLine(line);
                }
            }
            return AspIndentProcessor.generateFormattedText(codeLines);
        } catch (Exception e) {
            System.err.println("SimpleAspFormatter error: " + e.getMessage());
            return content;
        }
    }

    private static String formatASPLine(String line) {
        if (line.trim().isEmpty()) return line;
        if (line.trim().startsWith("'")) return line;

        List<String> strings = new ArrayList<>();
        String protectedLine = protectStrings(line, strings);

        String codePart = protectedLine;
        String commentPart = "";
        int commentIdx = protectedLine.indexOf('\'');
        if (commentIdx >= 0) {
            codePart = protectedLine.substring(0, commentIdx);
            commentPart = protectedLine.substring(commentIdx);
        }

        String formatted = formatKeywords(codePart);
        formatted = formatObjectMethodCase(formatted);
        formatted = formatOperatorsByYaml(formatted);
        formatted = fixUnaryMinus(formatted); // 修复一元负号
        formatted = formatted.replaceAll("\\s+", " ").trim();
        formatted = restoreStrings(formatted, strings);
        return formatted + commentPart;
    }

    private static String formatKeywords(String line) {
        Map<String, String> caseMap = SpecRegistry.getCaseMap();
        if (caseMap == null || caseMap.isEmpty()) {
            caseMap = VBScriptKeywords.getCaseMap();
        }
        String result = line;
        for (Map.Entry<String, String> e : caseMap.entrySet()) {
            String lower = e.getKey();
            String proper = e.getValue();
            if (lower == null || proper == null) continue;
            String pattern = "\\b" + Pattern.quote(lower) + "\\b";
            result = result.replaceAll("(?i)" + pattern, proper);
        }
        return result;
    }

    private static String formatObjectMethodCase(String line) {
        Map<String, Map<String, String>> objectMethodCase = SpecRegistry.getObjectMethodCase();
        if (objectMethodCase == null || objectMethodCase.isEmpty()) return line;
        String result = line;
        for (Map.Entry<String, Map<String, String>> entry : objectMethodCase.entrySet()) {
            String objectLower = entry.getKey();
            Map<String, String> methodCase = entry.getValue();
            if (methodCase == null || methodCase.isEmpty()) continue;
            for (Map.Entry<String, String> mc : methodCase.entrySet()) {
                String methodLower = mc.getKey();
                String properMethod = mc.getValue();
                String pattern = "(?i)(\\b" + Pattern.quote(objectLower) + "\\s*\\.\\s*)(" + Pattern.quote(methodLower) + ")\\b";
                String replacement = "$1" + properMethod;
                result = result.replaceAll(pattern, replacement);
            }
        }
        return result;
    }

    /**
     * 由 YAML 的 formatting 段驱动的运算符空格策略
     */
    private static String formatOperatorsByYaml(String line) {
        String result = line;
        List<String> symbols = new ArrayList<>(SpecRegistry.getOperatorSymbolsWithSpaces() == null
                ? Collections.emptyList()
                : SpecRegistry.getOperatorSymbolsWithSpaces());
        Set<String> kwOps = SpecRegistry.getKeywordOpsWithSpaces();
        boolean usedYaml = !symbols.isEmpty() || (kwOps != null && !kwOps.isEmpty());

        if (usedYaml) {
            // 先按长度降序处理，确保 <>、<=、>= 优先
            symbols.sort(Comparator.comparingInt(String::length).reversed());
            for (String sym : symbols) {
                String quoted = Pattern.quote(sym);
                if ("<>".equals(sym)) {
                    result = result.replaceAll("\\s*<\\s*>\\s*", " <> ");
                } else if ("<=".equals(sym)) {
                    result = result.replaceAll("\\s*<\\s*=\\s*", " <= ");
                } else if (">=".equals(sym)) {
                    result = result.replaceAll("\\s*>\\s*=\\s*", " >= ");
                } else if ("<".equals(sym) || ">".equals(sym)) {
                    // 先跳过，后用安全规则
                } else {
                    result = result.replaceAll("\\s*" + quoted + "\\s*", " " + sym + " ");
                }
            }
            // 针对 < 与 > 使用安全规则，避免破坏 <>、<=、>=
            result = result.replaceAll("(?<!<)\\s*<\\s*(?![=>])", " < ");
            result = result.replaceAll("(?<!<)\\s*>\\s*(?!>)", " > ");

            if (kwOps != null && !kwOps.isEmpty()) {
                for (String kw : kwOps) {
                    String pattern = "(?i)\\b" + Pattern.quote(kw) + "\\b";
                    result = result.replaceAll("\\s*" + pattern + "\\s*", " " + kw + " ");
                }
            }
        } else {
            // 兜底旧策略
            result = result.replaceAll("\\s*<\\s*>\\s*", " <> ");
            result = result.replaceAll("\\s*<=\\s*", " <= ");
            result = result.replaceAll("\\s*>=\\s*", " >= ");
            result = result.replaceAll("\\s*=\\s*", " = ");
            result = result.replaceAll("(?<!<)\\s*<\\s*(?![=>])", " < ");
            result = result.replaceAll("(?<!<)\\s*>\\s*(?!>)", " > ");
            result = result.replaceAll("\\s*&\\s*", " & ");
        }
        return result;
    }

    /**
     * 修复一元负号：将 "- 10" 恢复为 "-10"（仅当前面是行首或运算符/括号/逗号/空白，且后面是数字）。
     */
    private static String fixUnaryMinus(String line) {
        String result = line;
        // 移除一元负号两侧的多余空格：(^|[符号])\s*-\s+(?=\d) -> $1-
        Pattern p = Pattern.compile("(?m)(^|[\\(=,\\+\\*\\^/\\\\<>])\\s*-\\s+(?=\\d)");
        Matcher m = p.matcher(result);
        if (m.find()) {
            result = m.replaceAll("$1-");
            System.out.println("[FORMAT][UnaryMinus] compacted unary minus around numbers");
        }
        return result;
    }

    private static String protectStrings(String line, List<String> strings) {
        String result = line;
        int stringIndex = 0;
        int startQuote = result.indexOf('"');
        while (startQuote != -1) {
            int endQuote = result.indexOf('"', startQuote + 1);
            if (endQuote != -1) {
                String stringContent = result.substring(startQuote, endQuote + 1);
                String placeholder = "___STRING_" + stringIndex + "___";
                strings.add(stringContent);
                result = result.substring(0, startQuote) + placeholder + result.substring(endQuote + 1);
                stringIndex++;
                startQuote = result.indexOf('"', startQuote + placeholder.length());
            } else { break; }
        }
        return result;
    }

    private static String restoreStrings(String line, List<String> strings) {
        String result = line;
        for (int i = 0; i < strings.size(); i++) {
            String placeholder = "___STRING_" + i + "___";
            String stringContent = strings.get(i);
            result = result.replace(placeholder, stringContent);
        }
        return result;
    }
}