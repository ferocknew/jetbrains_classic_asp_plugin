package com.ferock.classicasp;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 统一语义分析管理器
 * 整合代码区域检测、代码块分析和层级管理功能
 * 为语法高亮和代码格式化提供统一的语义分析服务
 */
public class SemanticAnalyzer {

    // 缓存管理器
    private static final Map<String, AnalysisResult> analysisCache = new ConcurrentHashMap<>();
    private static final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private static final long CACHE_TIMEOUT = 30000; // 30秒缓存超时

    /**
     * 语义分析结果类
     */
    public static class AnalysisResult {
        private String content;
        private List<CodeBlockAnalyzer.CodeBlock> codeBlocks;
        private int[][] aspSections;
        private String blockStatistics;
        private String hierarchyStatistics;
        private long analysisTimestamp;

        public AnalysisResult(String content, List<CodeBlockAnalyzer.CodeBlock> codeBlocks,
                           int[][] aspSections, String blockStatistics, String hierarchyStatistics) {
            this.content = content;
            this.codeBlocks = codeBlocks;
            this.aspSections = aspSections;
            this.blockStatistics = blockStatistics;
            this.hierarchyStatistics = hierarchyStatistics;
            this.analysisTimestamp = System.currentTimeMillis();
        }

        // Getters
        public String getContent() { return content; }
        public List<CodeBlockAnalyzer.CodeBlock> getCodeBlocks() { return codeBlocks; }
        public int[][] getAspSections() { return aspSections; }
        public String getBlockStatistics() { return blockStatistics; }
        public String getHierarchyStatistics() { return hierarchyStatistics; }
        public long getAnalysisTimestamp() { return analysisTimestamp; }
    }

    /**
     * 执行完整的语义分析（带缓存）
     * @param content 文件内容
     * @param filePath 文件路径（用于缓存键）
     * @return 语义分析结果
     */
    public static AnalysisResult analyze(String content, String filePath) {
        if (content == null || content.trim().isEmpty()) {
            return new AnalysisResult(content, null, new int[0][2], "无内容", "无层级信息");
        }

        // 生成缓存键
        String cacheKey = generateCacheKey(content, filePath);

        // 检查缓存
        AnalysisResult cachedResult = getCachedResult(cacheKey);
        if (cachedResult != null) {
            // System.out.println("🔍 [CACHE] 使用缓存的分析结果，文件: " + filePath);
            return cachedResult;
        }

        // System.out.println("🔍 [SEMANTIC] 开始语义分析，内容长度: " + content.length() + "，文件: " + filePath);

        // 执行完整分析
        AnalysisResult result = performFullAnalysis(content);

        // 缓存结果
        cacheResult(cacheKey, result);

        return result;
    }

    /**
     * 执行完整的语义分析（无缓存）
     * @param content 文件内容
     * @return 语义分析结果
     */
    public static AnalysisResult analyze(String content) {
        return analyze(content, "unknown");
    }

    /**
     * 执行完整的语义分析
     */
    private static AnalysisResult performFullAnalysis(String content) {
        // 第一步：代码区域检测
        int[][] aspSections = ASPSectionDetector.getASPSections(content);
        // System.out.println("🔍 [SEMANTIC] ASP区域检测完成，找到 " + aspSections.length + " 个ASP区域");

        // 第二步：代码块分析
        List<CodeBlockAnalyzer.CodeBlock> codeBlocks = CodeBlockAnalyzer.analyzeCodeBlocks(content);
        // System.out.println("🔍 [SEMANTIC] 代码块分析完成，找到 " + codeBlocks.size() + " 个顶级代码块");

        // 第三步：生成统计信息
        String blockStatistics = CodeBlockAnalyzer.getBlockStatistics(codeBlocks);
        String hierarchyStatistics = CodeHierarchyManager.getHierarchyStatistics(codeBlocks);

        // System.out.println("🔍 [SEMANTIC] 语义分析完成");
        // System.out.println(blockStatistics);
        // System.out.println(hierarchyStatistics);

        return new AnalysisResult(content, codeBlocks, aspSections, blockStatistics, hierarchyStatistics);
    }

    /**
     * 生成缓存键
     */
    private static String generateCacheKey(String content, String filePath) {
        // 使用内容哈希和文件路径作为缓存键
        int contentHash = content.hashCode();
        return filePath + ":" + contentHash;
    }

    /**
     * 获取缓存的分析结果
     */
    private static AnalysisResult getCachedResult(String cacheKey) {
        AnalysisResult cached = analysisCache.get(cacheKey);
        if (cached == null) {
            return null;
        }

        // 检查缓存是否过期
        Long timestamp = cacheTimestamps.get(cacheKey);
        if (timestamp == null || System.currentTimeMillis() - timestamp > CACHE_TIMEOUT) {
            // System.out.println("🔍 [CACHE] 缓存已过期，重新分析");
            invalidateSpecificCache(cacheKey);
            return null;
        }

        return cached;
    }

    /**
     * 缓存分析结果
     */
    private static void cacheResult(String cacheKey, AnalysisResult result) {
        analysisCache.put(cacheKey, result);
        cacheTimestamps.put(cacheKey, System.currentTimeMillis());
                    // System.out.println("🔍 [CACHE] 分析结果已缓存，键: " + cacheKey);
    }

    /**
     * 使缓存失效
     * @param filePath 文件路径
     */
    public static void invalidateCache(String filePath) {
        // 移除所有与该文件相关的缓存
        analysisCache.entrySet().removeIf(entry -> entry.getKey().startsWith(filePath + ":"));
        cacheTimestamps.entrySet().removeIf(entry -> entry.getKey().startsWith(filePath + ":"));
        // System.out.println("🔍 [CACHE] 已清除文件缓存: " + filePath);
    }

    /**
     * 使特定缓存失效
     * @param cacheKey 缓存键
     */
    private static void invalidateSpecificCache(String cacheKey) {
        analysisCache.remove(cacheKey);
        cacheTimestamps.remove(cacheKey);
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache() {
        analysisCache.clear();
        cacheTimestamps.clear();
        // System.out.println("🔍 [CACHE] 已清除所有缓存");
    }

    /**
     * 获取缓存统计信息
     */
    public static String getCacheStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("缓存统计信息:\n");
        stats.append("- 缓存条目数: ").append(analysisCache.size()).append("\n");
        stats.append("- 缓存超时时间: ").append(CACHE_TIMEOUT / 1000).append(" 秒\n");

        long totalMemory = 0;
        for (AnalysisResult result : analysisCache.values()) {
            if (result.getContent() != null) {
                totalMemory += result.getContent().length() * 2; // 估算内存使用
            }
        }
        stats.append("- 估算内存使用: ").append(totalMemory / 1024).append(" KB\n");

        return stats.toString();
    }

    /**
     * 获取指定位置的语义信息（带缓存）
     */
    public static PositionInfo getPositionInfo(AnalysisResult result, int position) {
        if (result == null || result.getContent() == null) {
            return new PositionInfo(false, null, 0);
        }

        // 检查是否在ASP区域内
        boolean inASPSection = ASPSectionDetector.isInASPSection(result.getContent(), position);

        // 计算行号
        int lineNumber = calculateLineNumber(result.getContent(), position);

        // 获取代码块信息
        CodeBlockAnalyzer.CodeBlock block = null;
        if (result.getCodeBlocks() != null) {
            block = CodeBlockAnalyzer.getBlockAtLine(result.getCodeBlocks(), lineNumber);
        }

        return new PositionInfo(inASPSection, block, lineNumber);
    }

    /**
     * 获取指定行的语义信息（带缓存）
     */
    public static LineInfo getLineInfo(AnalysisResult result, int lineNumber) {
        if (result == null || result.getContent() == null) {
            return new LineInfo(false, null, null);
        }

        // 检查是否在ASP区域内
        boolean inASPSection = ASPSectionDetector.isLineInASPSection(result.getContent(), lineNumber);

        // 获取代码块信息
        CodeBlockAnalyzer.CodeBlock block = null;
        if (result.getCodeBlocks() != null) {
            block = CodeBlockAnalyzer.getBlockAtLine(result.getCodeBlocks(), lineNumber);
        }

        // 获取层级信息
        CodeHierarchyManager.HierarchyInfo hierarchy = null;
        if (result.getCodeBlocks() != null) {
            hierarchy = CodeHierarchyManager.getHierarchyInfo(result.getCodeBlocks(), lineNumber);
        }

        return new LineInfo(inASPSection, block, hierarchy);
    }

    /**
     * 应用格式化规则（带缓存失效）
     * @param result 语义分析结果
     * @param filePath 文件路径
     * @return 格式化后的内容
     */
    public static String applyFormatting(AnalysisResult result, String filePath) {
        if (result == null || result.getContent() == null) {
            return "";
        }

        // System.out.println("🔍 [SEMANTIC] 开始应用格式化规则，文件: " + filePath);

        // 使用层级管理器应用格式化规则
        String formattedContent = CodeHierarchyManager.applyFormattingRules(
            result.getContent(), result.getCodeBlocks());

        // System.out.println("🔍 [SEMANTIC] 格式化规则应用完成");

        // 格式化完成后，清除该文件的缓存，因为内容已经改变
        invalidateCache(filePath);
        // System.out.println("🔍 [CACHE] 格式化完成，已清除文件缓存: " + filePath);

        return formattedContent;
    }

    /**
     * 应用格式化规则（无缓存失效）
     */
    public static String applyFormatting(AnalysisResult result) {
        return applyFormatting(result, "unknown");
    }

    /**
     * 计算行号
     */
    private static int calculateLineNumber(String content, int position) {
        if (position < 0 || position >= content.length()) {
            return 0;
        }

        int lineNumber = 0;
        for (int i = 0; i < position; i++) {
            if (content.charAt(i) == '\n') {
                lineNumber++;
            }
        }
        return lineNumber;
    }

    /**
     * 位置信息类
     */
    public static class PositionInfo {
        private boolean inASPSection;
        private CodeBlockAnalyzer.CodeBlock codeBlock;
        private int lineNumber;

        public PositionInfo(boolean inASPSection, CodeBlockAnalyzer.CodeBlock codeBlock, int lineNumber) {
            this.inASPSection = inASPSection;
            this.codeBlock = codeBlock;
            this.lineNumber = lineNumber;
        }

        // Getters
        public boolean isInASPSection() { return inASPSection; }
        public CodeBlockAnalyzer.CodeBlock getCodeBlock() { return codeBlock; }
        public int getLineNumber() { return lineNumber; }
    }

    /**
     * 行信息类
     */
    public static class LineInfo {
        private boolean inASPSection;
        private CodeBlockAnalyzer.CodeBlock codeBlock;
        private CodeHierarchyManager.HierarchyInfo hierarchy;

        public LineInfo(boolean inASPSection, CodeBlockAnalyzer.CodeBlock codeBlock,
                       CodeHierarchyManager.HierarchyInfo hierarchy) {
            this.inASPSection = inASPSection;
            this.codeBlock = codeBlock;
            this.hierarchy = hierarchy;
        }

        // Getters
        public boolean isInASPSection() { return inASPSection; }
        public CodeBlockAnalyzer.CodeBlock getCodeBlock() { return codeBlock; }
        public CodeHierarchyManager.HierarchyInfo getHierarchy() { return hierarchy; }
    }

    /**
     * 生成语义分析报告
     * @param result 语义分析结果
     * @return 分析报告
     */
    public static String generateReport(AnalysisResult result) {
        if (result == null) {
            return "无分析结果";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== 语义分析报告 ===\n");
        report.append("内容长度: ").append(result.getContent().length()).append(" 字符\n");
        report.append("ASP区域数量: ").append(result.getAspSections().length).append("\n");
        report.append("代码块数量: ").append(result.getCodeBlocks() != null ? result.getCodeBlocks().size() : 0).append("\n");
        report.append("分析时间: ").append(result.getAnalysisTimestamp()).append("\n");
        report.append("\n");
        report.append(result.getBlockStatistics());
        report.append("\n");
        report.append(result.getHierarchyStatistics());
        report.append("\n");
        report.append(getCacheStatistics());

        return report.toString();
    }
}