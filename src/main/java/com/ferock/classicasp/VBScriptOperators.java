package com.ferock.classicasp;

import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;
import com.intellij.psi.tree.IElementType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * VBScript 运算符统一管理类
 * 用于确保词法分析器、语法高亮器和格式化处理器使用一致的运算符列表
 */
public class VBScriptOperators {

    /**
     * 单字符运算符映射
     */
    private static volatile Map<Character, IElementType> SINGLE_CHAR_OPERATORS;

    /**
     * 复合运算符映射（字符串 -> Token类型）
     */
    private static volatile Map<String, IElementType> COMPOUND_OPERATORS;

    /**
     * 需要左右空格的运算符
     */
    private static volatile Set<IElementType> OPERATORS_WITH_SPACES;

    /**
     * 初始化运算符映射
     */
    private static synchronized void initializeOperators() {
        if (SINGLE_CHAR_OPERATORS != null) {
            return; // 已经初始化过了
        }

        SINGLE_CHAR_OPERATORS = new HashMap<>();
        COMPOUND_OPERATORS = new HashMap<>();
        OPERATORS_WITH_SPACES = new HashSet<>();

        // 算术运算符（单字符）
        SINGLE_CHAR_OPERATORS.put('^', ClassicASPTokenTypes.POWER);       // 求幂
        SINGLE_CHAR_OPERATORS.put('*', ClassicASPTokenTypes.MULTIPLY);    // 乘
        SINGLE_CHAR_OPERATORS.put('/', ClassicASPTokenTypes.DIVIDE);      // 除
        SINGLE_CHAR_OPERATORS.put('\\', ClassicASPTokenTypes.INT_DIVIDE); // 整除
        SINGLE_CHAR_OPERATORS.put('+', ClassicASPTokenTypes.PLUS);        // 加
        SINGLE_CHAR_OPERATORS.put('-', ClassicASPTokenTypes.MINUS);       // 减/负号

        // 字符串连接运算符
        SINGLE_CHAR_OPERATORS.put('&', ClassicASPTokenTypes.CONCATENATE); // 字符串连接

        // 比较运算符（单字符）
        SINGLE_CHAR_OPERATORS.put('=', ClassicASPTokenTypes.EQUALS);      // 等于
        SINGLE_CHAR_OPERATORS.put('<', ClassicASPTokenTypes.LESS_THAN);   // 小于
        SINGLE_CHAR_OPERATORS.put('>', ClassicASPTokenTypes.GREATER_THAN); // 大于

        // 括号
        SINGLE_CHAR_OPERATORS.put('(', ClassicASPTokenTypes.LPAREN);      // 左括号
        SINGLE_CHAR_OPERATORS.put(')', ClassicASPTokenTypes.RPAREN);      // 右括号
        SINGLE_CHAR_OPERATORS.put('[', ClassicASPTokenTypes.LBRACKET);    // 左方括号
        SINGLE_CHAR_OPERATORS.put(']', ClassicASPTokenTypes.RBRACKET);    // 右方括号

        // 标点符号
        SINGLE_CHAR_OPERATORS.put(',', ClassicASPTokenTypes.COMMA);       // 逗号
        SINGLE_CHAR_OPERATORS.put(';', ClassicASPTokenTypes.SEMICOLON);   // 分号
        SINGLE_CHAR_OPERATORS.put('.', ClassicASPTokenTypes.DOT);         // 点号(对象成员访问)

        // 其他符号
        SINGLE_CHAR_OPERATORS.put(':', ClassicASPTokenTypes.COLON);        // 冒号
        SINGLE_CHAR_OPERATORS.put('@', ClassicASPTokenTypes.ASP_DIRECTIVE); // ASP指令符号

        // 复合比较运算符
        COMPOUND_OPERATORS.put("<>", ClassicASPTokenTypes.NEQ);           // 不等于
        COMPOUND_OPERATORS.put("<=", ClassicASPTokenTypes.LESS_EQUAL);    // 小于等于
        COMPOUND_OPERATORS.put(">=", ClassicASPTokenTypes.GREATER_EQUAL); // 大于等于

        // 需要左右空格的运算符（算术、比较、连接运算符）
        // 算术运算符
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.POWER);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.MULTIPLY);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.DIVIDE);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.INT_DIVIDE);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.PLUS);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.MINUS);

        // 字符串连接运算符
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.CONCATENATE);

        // 比较运算符
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.EQUALS);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.NEQ);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.LESS_THAN);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.GREATER_THAN);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.LESS_EQUAL);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.GREATER_EQUAL);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.IS);

        // Mod运算符也需要空格
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.MOD);

        // 逻辑运算符需要空格
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.NOT);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.AND);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.OR);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.XOR);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.EQV);
        OPERATORS_WITH_SPACES.add(ClassicASPTokenTypes.IMP);
    }

    /**
     * 获取单字符运算符的token类型
     * @param c 字符
     * @return 对应的IElementType，如果不是运算符则返回null
     */
    public static IElementType getSingleCharOperator(char c) {
        if (SINGLE_CHAR_OPERATORS == null) {
            initializeOperators();
        }
        return SINGLE_CHAR_OPERATORS.get(c);
    }

    /**
     * 获取复合运算符的token类型
     * @param operator 运算符字符串
     * @return 对应的IElementType，如果不是复合运算符则返回null
     */
    public static IElementType getCompoundOperator(String operator) {
        if (COMPOUND_OPERATORS == null) {
            initializeOperators();
        }
        return COMPOUND_OPERATORS.get(operator);
    }

    /**
     * 检查是否为单字符运算符
     * @param c 字符
     * @return 是否为运算符
     */
    public static boolean isSingleCharOperator(char c) {
        if (SINGLE_CHAR_OPERATORS == null) {
            initializeOperators();
        }
        return SINGLE_CHAR_OPERATORS.containsKey(c);
    }

    /**
     * 检查是否为复合运算符
     * @param operator 运算符字符串
     * @return 是否为复合运算符
     */
    public static boolean isCompoundOperator(String operator) {
        if (COMPOUND_OPERATORS == null) {
            initializeOperators();
        }
        return COMPOUND_OPERATORS.containsKey(operator);
    }

    /**
     * 检查运算符是否需要左右空格
     * @param tokenType 运算符的token类型
     * @return 是否需要空格
     */
    public static boolean needsSpaces(IElementType tokenType) {
        if (OPERATORS_WITH_SPACES == null) {
            initializeOperators();
        }
        return OPERATORS_WITH_SPACES.contains(tokenType);
    }

    /**
     * 获取所有单字符运算符的映射
     * @return 单字符运算符映射的副本
     */
    public static Map<Character, IElementType> getSingleCharOperators() {
        if (SINGLE_CHAR_OPERATORS == null) {
            initializeOperators();
        }
        return new HashMap<>(SINGLE_CHAR_OPERATORS);
    }

    /**
     * 获取所有复合运算符的映射
     * @return 复合运算符映射的副本
     */
    public static Map<String, IElementType> getCompoundOperators() {
        if (COMPOUND_OPERATORS == null) {
            initializeOperators();
        }
        return new HashMap<>(COMPOUND_OPERATORS);
    }

    /**
     * 获取所有需要空格的运算符
     * @return 需要空格的运算符集合的副本
     */
    public static Set<IElementType> getOperatorsWithSpaces() {
        if (OPERATORS_WITH_SPACES == null) {
            initializeOperators();
        }
        return new HashSet<>(OPERATORS_WITH_SPACES);
    }

    /**
     * 通用的获取运算符token类型方法
     * @param operator 运算符字符串
     * @return 对应的IElementType，如果不是运算符则返回null
     */
    public static IElementType getTokenType(String operator) {
        if (operator == null || operator.isEmpty()) {
            return null;
        }

        if (COMPOUND_OPERATORS == null) {
            initializeOperators();
        }

        // 首先尝试复合运算符
        IElementType tokenType = COMPOUND_OPERATORS.get(operator);
        if (tokenType != null) {
            return tokenType;
        }

        // 然后尝试单字符运算符
        if (operator.length() == 1) {
            return SINGLE_CHAR_OPERATORS.get(operator.charAt(0));
        }

        return null;
    }

    /**
     * 尝试解析复合运算符
     * @param text 文本
     * @param position 当前位置
     * @return 复合运算符信息，如果没有找到则返回null
     */
    public static CompoundOperatorInfo parseCompoundOperator(CharSequence text, int position) {
        if (position >= text.length()) {
            return null;
        }

        if (COMPOUND_OPERATORS == null) {
            initializeOperators();
        }

        char currentChar = text.charAt(position);

        // 检查两字符复合运算符
        if (position + 1 < text.length()) {
            char nextChar = text.charAt(position + 1);
            String twoChar = "" + currentChar + nextChar;

            IElementType tokenType = COMPOUND_OPERATORS.get(twoChar);
            if (tokenType != null) {
                return new CompoundOperatorInfo(twoChar, tokenType, 2);
            }
        }

        return null;
    }

    /**
     * 复合运算符信息
     */
    public static class CompoundOperatorInfo {
        public final String operator;
        public final IElementType tokenType;
        public final int length;

        public CompoundOperatorInfo(String operator, IElementType tokenType, int length) {
            this.operator = operator;
            this.tokenType = tokenType;
            this.length = length;
        }
    }
}