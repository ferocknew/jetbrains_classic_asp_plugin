package com.ferock.classicasp;

/**
 * Classic ASP 插件的安全限制配置
 * 防止死循环和其他可能导致 IDE 卡死的情况
 */
public final class SafetyLimits {

    /**
     * 通用循环的最大迭代次数
     * 适用于所有可能出现死循环的地方
     */
    public static final int MAX_LOOP_ITERATIONS = 1000;

    /**
     * AST 节点遍历的最大深度
     */
    public static final int MAX_AST_DEPTH = 500;

    /**
     * 词法分析器处理单个 token 的最大字符数
     */
    public static final int MAX_TOKEN_LENGTH = 10000;

    /**
     * 解析器处理单个语句的最大 token 数量
     */
    public static final int MAX_TOKENS_PER_STATEMENT = 1000;

    /**
     * 格式化器处理的最大文本长度（字符数）
     */
    public static final int MAX_FORMATTING_TEXT_LENGTH = 1000000; // 1MB

    /**
     * 单个文件的最大行数限制
     */
    public static final int MAX_FILE_LINES = 10000;

    /**
     * 私有构造函数，防止实例化
     */
    private SafetyLimits() {
        throw new UnsupportedOperationException("SafetyLimits is a utility class and should not be instantiated");
    }

    /**
     * 记录达到安全限制的警告信息
     *
     * @param location 发生位置的描述
     * @param limitType 限制类型
     * @param actualValue 实际达到的值
     */
    public static void logSafetyLimitReached(String location, String limitType, int actualValue) {
        System.err.println("WARNING: Safety limit reached in " + location +
                          " - " + limitType + ": " + actualValue);
    }

        /**
     * 检查循环是否超过安全限制
     *
     * @param iterations 当前迭代次数
     * @param location 循环位置描述
     * @return 是否超过限制
     */
    public static boolean checkLoopLimit(int iterations, String location) {
        if (iterations >= MAX_LOOP_ITERATIONS) {
            logSafetyLimitReached(location, "MAX_LOOP_ITERATIONS", iterations);
            return true;
        }
        return false;
    }

    /**
     * 安全执行代码块，捕获所有异常
     *
     * @param operation 要执行的操作
     * @param location 操作位置描述
     * @param defaultValue 出错时的默认返回值
     * @return 操作结果或默认值
     */
    public static <T> T safeExecute(java.util.function.Supplier<T> operation, String location, T defaultValue) {
        try {
            return operation.get();
        } catch (Exception e) {
            System.err.println("ERROR in " + location + ": " + e.getMessage());
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 安全执行无返回值的代码块
     *
     * @param operation 要执行的操作
     * @param location 操作位置描述
     */
    public static void safeExecute(Runnable operation, String location) {
        try {
            operation.run();
        } catch (Exception e) {
            System.err.println("ERROR in " + location + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 验证文档内容是否安全处理
     *
     * @param text 要验证的文本
     * @param location 验证位置描述
     * @return 是否安全
     */
    public static boolean isTextSafeToProcess(String text, String location) {
        if (text == null) {
            System.err.println("WARNING: Null text in " + location);
            return false;
        }

        if (text.length() > MAX_FORMATTING_TEXT_LENGTH) {
            System.err.println("WARNING: Text too long in " + location + ": " + text.length() + " chars");
            return false;
        }

        int lineCount = text.split("\n").length;
        if (lineCount > MAX_FILE_LINES) {
            System.err.println("WARNING: Too many lines in " + location + ": " + lineCount + " lines");
            return false;
        }

        return true;
    }
}