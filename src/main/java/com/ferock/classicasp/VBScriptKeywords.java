package com.ferock.classicasp;

import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;
import com.intellij.psi.tree.IElementType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * VBScript 关键词分类管理类
 * 按功能对关键词进行分类管理，避免硬编码，提高可维护性
 */
public class VBScriptKeywords {

    /**
     * 所有VBScript关键词到对应token类型的映射
     */
    private static volatile Map<String, IElementType> KEYWORD_MAP;

    /**
     * 格式化时的关键词大小写映射（小写 -> 正确大小写）
     */
    private static volatile Map<String, String> CASE_MAP;

    // === 关键词分类集合 ===

    /** 1. 内置函数 */
    private static volatile Set<String> BUILTIN_FUNCTIONS;

    /** 2. 执行过程：function、sub */
    private static volatile Set<String> PROCEDURE_KEYWORDS;

    /** 3. 循环语句 */
    private static volatile Set<String> LOOP_KEYWORDS;

    /** 4. 条件语句 */
    private static volatile Set<String> CONDITION_KEYWORDS;

    /** 5. 异常处理语句 */
    private static volatile Set<String> EXCEPTION_KEYWORDS;

    /** 6. 内置对象 */
    private static volatile Set<String> BUILTIN_OBJECTS;

    /** 7. 数据类型和声明 */
    private static volatile Set<String> DECLARATION_KEYWORDS;

    /** 8. 选择语句 */
    private static volatile Set<String> SELECTION_KEYWORDS;

    /**
     * 初始化关键词映射
     */
    private static synchronized void initializeKeywords() {
        if (KEYWORD_MAP != null) {
            return; // 已经初始化过了
        }

        KEYWORD_MAP = new HashMap<>();
        CASE_MAP = new HashMap<>();
        BUILTIN_FUNCTIONS = new HashSet<>();
        PROCEDURE_KEYWORDS = new HashSet<>();
        LOOP_KEYWORDS = new HashSet<>();
        CONDITION_KEYWORDS = new HashSet<>();
        EXCEPTION_KEYWORDS = new HashSet<>();
        BUILTIN_OBJECTS = new HashSet<>();
        DECLARATION_KEYWORDS = new HashSet<>();
        SELECTION_KEYWORDS = new HashSet<>();

        // === 1. 数据类型和声明 ===
        addKeywordToCategory("dim", ClassicASPTokenTypes.DIM, "Dim", DECLARATION_KEYWORDS);
        addKeywordToCategory("set", ClassicASPTokenTypes.SET, "Set", DECLARATION_KEYWORDS);

        // === 2. 执行过程：function、sub ===
        addKeywordToCategory("function", ClassicASPTokenTypes.FUNCTION, "Function", PROCEDURE_KEYWORDS);
        addKeywordToCategory("sub", ClassicASPTokenTypes.SUB, "Sub", PROCEDURE_KEYWORDS);
        addKeywordToCategory("end function", ClassicASPTokenTypes.END_FUNCTION, "End Function", PROCEDURE_KEYWORDS);
        addKeywordToCategory("end sub", ClassicASPTokenTypes.END_SUB, "End Sub", PROCEDURE_KEYWORDS);

        // === 3. 条件语句 ===
        addKeywordToCategory("if", ClassicASPTokenTypes.IF, "If", CONDITION_KEYWORDS);
        addKeywordToCategory("then", ClassicASPTokenTypes.THEN, "Then", CONDITION_KEYWORDS);
        addKeywordToCategory("else", ClassicASPTokenTypes.ELSE, "Else", CONDITION_KEYWORDS);
        addKeywordToCategory("elseif", ClassicASPTokenTypes.ELSEIF, "ElseIf", CONDITION_KEYWORDS);
        addKeywordToCategory("end if", ClassicASPTokenTypes.END_IF, "End If", CONDITION_KEYWORDS);
        addKeywordToCategory("end", ClassicASPTokenTypes.END, "End", CONDITION_KEYWORDS);

        // === 4. 循环语句 ===
        addKeywordToCategory("for", ClassicASPTokenTypes.FOR, "For", LOOP_KEYWORDS);
        addKeywordToCategory("next", ClassicASPTokenTypes.NEXT, "Next", LOOP_KEYWORDS);
        addKeywordToCategory("while", ClassicASPTokenTypes.WHILE, "While", LOOP_KEYWORDS);
        addKeywordToCategory("wend", ClassicASPTokenTypes.WEND, "Wend", LOOP_KEYWORDS);
        addKeywordToCategory("do", ClassicASPTokenTypes.DO, "Do", LOOP_KEYWORDS);
        addKeywordToCategory("loop", ClassicASPTokenTypes.LOOP, "Loop", LOOP_KEYWORDS);
        addKeywordToCategory("each", ClassicASPTokenTypes.EACH, "Each", LOOP_KEYWORDS);
        addKeywordToCategory("in", ClassicASPTokenTypes.IN, "In", LOOP_KEYWORDS);

        // === 5. 选择语句 ===
        addKeywordToCategory("select", ClassicASPTokenTypes.SELECT, "Select", SELECTION_KEYWORDS);
        addKeywordToCategory("case", ClassicASPTokenTypes.CASE, "Case", SELECTION_KEYWORDS);
        addKeywordToCategory("end select", ClassicASPTokenTypes.END_SELECT, "End Select", SELECTION_KEYWORDS);

        // === 6. 属性（加入过程类别） ===
        addKeywordToCategory("property", ClassicASPTokenTypes.PROPERTY, "Property", PROCEDURE_KEYWORDS);
        addKeywordToCategory("end property", ClassicASPTokenTypes.END_PROPERTY, "End Property", PROCEDURE_KEYWORDS);

        // 比较运算符
        addKeyword("is", ClassicASPTokenTypes.IS, "Is");

        // 逻辑运算符关键字（根据VBScript官方文档）
        addKeyword("not", ClassicASPTokenTypes.NOT, "Not");
        addKeyword("and", ClassicASPTokenTypes.AND, "And");
        addKeyword("or", ClassicASPTokenTypes.OR, "Or");
        addKeyword("xor", ClassicASPTokenTypes.XOR, "Xor");
        addKeyword("eqv", ClassicASPTokenTypes.EQV, "Eqv");
        addKeyword("imp", ClassicASPTokenTypes.IMP, "Imp");

        // Mod运算符关键字
        addKeyword("mod", ClassicASPTokenTypes.MOD, "Mod");

        // VBScript特殊值和关键字（根据菜鸟教程补充）
        addKeyword("empty", ClassicASPTokenTypes.EMPTY, "Empty");
        addKeyword("nothing", ClassicASPTokenTypes.NOTHING, "Nothing");
        addKeyword("null", ClassicASPTokenTypes.NULL, "Null");
        addKeyword("true", ClassicASPTokenTypes.TRUE, "True");
        addKeyword("false", ClassicASPTokenTypes.FALSE, "False");

        // === 7. 异常处理语句 ===
        // 参考：https://mrslowblog.blogspot.com/2012/11/vb-script-try-and-catch.html
        addKeywordToCategory("on", ClassicASPTokenTypes.ON, "On", EXCEPTION_KEYWORDS);
        addKeywordToCategory("resume", ClassicASPTokenTypes.RESUME, "Resume", EXCEPTION_KEYWORDS);
        addKeywordToCategory("on error resume next", ClassicASPTokenTypes.IDENTIFIER, "On Error Resume Next", EXCEPTION_KEYWORDS);
        addKeywordToCategory("on error goto", ClassicASPTokenTypes.IDENTIFIER, "On Error GoTo", EXCEPTION_KEYWORDS);
        addKeywordToCategory("err", ClassicASPTokenTypes.ERR, "Err", EXCEPTION_KEYWORDS);
        addKeywordToCategory("error", ClassicASPTokenTypes.ERROR, "Error", EXCEPTION_KEYWORDS);

        // === 8. 内置对象 ===
        // ASP六大内置对象：Application, Request, Response, Session, Server, Err
        addKeywordToCategory("response", ClassicASPTokenTypes.RESPONSE, "Response", BUILTIN_OBJECTS);
        addKeywordToCategory("request", ClassicASPTokenTypes.REQUEST, "Request", BUILTIN_OBJECTS);
        addKeywordToCategory("server", ClassicASPTokenTypes.SERVER, "Server", BUILTIN_OBJECTS);
        addKeywordToCategory("session", ClassicASPTokenTypes.SESSION, "Session", BUILTIN_OBJECTS);
        addKeywordToCategory("application", ClassicASPTokenTypes.APPLICATION, "Application", BUILTIN_OBJECTS);
        addKeywordToCategory("err", ClassicASPTokenTypes.ERR, "Err", BUILTIN_OBJECTS);

        // === 新增关键词 ===
        addKeywordToCategory("class", ClassicASPTokenTypes.CLASS, "Class", DECLARATION_KEYWORDS);
        addKeywordToCategory("end class", ClassicASPTokenTypes.END_CLASS, "End Class", DECLARATION_KEYWORDS);
        addKeywordToCategory("public", ClassicASPTokenTypes.PUBLIC, "Public", DECLARATION_KEYWORDS);
        addKeywordToCategory("private", ClassicASPTokenTypes.PRIVATE, "Private", DECLARATION_KEYWORDS);

        // === ASP指令关键词 ===
        addKeywordToCategory("option", ClassicASPTokenTypes.OPTION, "Option", DECLARATION_KEYWORDS);
        addKeywordToCategory("explicit", ClassicASPTokenTypes.EXPLICIT, "Explicit", DECLARATION_KEYWORDS);
        addKeywordToCategory("language", ClassicASPTokenTypes.LANGUAGE, "LANGUAGE", DECLARATION_KEYWORDS);
        addKeywordToCategory("codepage", ClassicASPTokenTypes.CODEPAGE, "CODEPAGE", DECLARATION_KEYWORDS);

        // === 对象方法调用（显示为蓝色） ===
        // Response 对象方法
        addKeywordToCategory("write", ClassicASPTokenTypes.OBJECT_METHOD, "Write", BUILTIN_FUNCTIONS);
        addKeywordToCategory("charset", ClassicASPTokenTypes.OBJECT_METHOD, "Charset", BUILTIN_FUNCTIONS);
        addKeywordToCategory("contenttype", ClassicASPTokenTypes.OBJECT_METHOD, "ContentType", BUILTIN_FUNCTIONS);
        addKeywordToCategory("buffer", ClassicASPTokenTypes.OBJECT_METHOD, "Buffer", BUILTIN_FUNCTIONS);
        addKeywordToCategory("codepage", ClassicASPTokenTypes.OBJECT_METHOD, "CodePage", BUILTIN_FUNCTIONS);

        addKeywordToCategory("flush", ClassicASPTokenTypes.OBJECT_METHOD, "Flush", BUILTIN_FUNCTIONS);
        addKeywordToCategory("redirect", ClassicASPTokenTypes.OBJECT_METHOD, "Redirect", BUILTIN_FUNCTIONS);

        // Server 对象方法
        addKeywordToCategory("createobject", ClassicASPTokenTypes.OBJECT_METHOD, "CreateObject", BUILTIN_FUNCTIONS);
        addKeywordToCategory("mappath", ClassicASPTokenTypes.OBJECT_METHOD, "MapPath", BUILTIN_FUNCTIONS);
        addKeywordToCategory("scripttimeout", ClassicASPTokenTypes.OBJECT_METHOD, "ScriptTimeOut", BUILTIN_FUNCTIONS);
        addKeywordToCategory("htmlencode", ClassicASPTokenTypes.OBJECT_METHOD, "HTMLEncode", BUILTIN_FUNCTIONS);
        addKeywordToCategory("urlencode", ClassicASPTokenTypes.OBJECT_METHOD, "URLEncode", BUILTIN_FUNCTIONS);

        // Request 对象方法
        addKeywordToCategory("form", ClassicASPTokenTypes.OBJECT_METHOD, "Form", BUILTIN_FUNCTIONS);
        addKeywordToCategory("querystring", ClassicASPTokenTypes.OBJECT_METHOD, "QueryString", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cookies", ClassicASPTokenTypes.OBJECT_METHOD, "Cookies", BUILTIN_FUNCTIONS);
        addKeywordToCategory("servervariables", ClassicASPTokenTypes.OBJECT_METHOD, "ServerVariables", BUILTIN_FUNCTIONS);

        // Session 对象方法
        addKeywordToCategory("abandon", ClassicASPTokenTypes.OBJECT_METHOD, "Abandon", BUILTIN_FUNCTIONS);
        addKeywordToCategory("timeout", ClassicASPTokenTypes.OBJECT_METHOD, "Timeout", BUILTIN_FUNCTIONS);

        // Application 对象方法
        addKeywordToCategory("lock", ClassicASPTokenTypes.OBJECT_METHOD, "Lock", BUILTIN_FUNCTIONS);
        addKeywordToCategory("unlock", ClassicASPTokenTypes.OBJECT_METHOD, "Unlock", BUILTIN_FUNCTIONS);

        // Err 对象方法
        addKeywordToCategory("clear", ClassicASPTokenTypes.OBJECT_METHOD, "Clear", BUILTIN_FUNCTIONS);
        addKeywordToCategory("raise", ClassicASPTokenTypes.OBJECT_METHOD, "Raise", BUILTIN_FUNCTIONS);
        addKeywordToCategory("number", ClassicASPTokenTypes.OBJECT_METHOD, "Number", BUILTIN_FUNCTIONS);
        addKeywordToCategory("description", ClassicASPTokenTypes.OBJECT_METHOD, "Description", BUILTIN_FUNCTIONS);
        addKeywordToCategory("source", ClassicASPTokenTypes.OBJECT_METHOD, "Source", BUILTIN_FUNCTIONS);

        // === 9. 内置函数 ===
        addKeywordToCategory("isempty", ClassicASPTokenTypes.ISEMPTY, "IsEmpty", BUILTIN_FUNCTIONS);
        addKeywordToCategory("isnothing", ClassicASPTokenTypes.ISNOTHING, "IsNothing", BUILTIN_FUNCTIONS);
        addKeywordToCategory("isnull", ClassicASPTokenTypes.ISNULL, "IsNull", BUILTIN_FUNCTIONS);
        addKeywordToCategory("isnumeric", ClassicASPTokenTypes.ISNUMERIC, "IsNumeric", BUILTIN_FUNCTIONS);
        addKeywordToCategory("isobject", ClassicASPTokenTypes.ISOBJECT, "IsObject", BUILTIN_FUNCTIONS);
        addKeywordToCategory("isdate", ClassicASPTokenTypes.ISDATE, "IsDate", BUILTIN_FUNCTIONS);
        addKeywordToCategory("msgbox", ClassicASPTokenTypes.MSGBOX, "MsgBox", BUILTIN_FUNCTIONS);
        addKeywordToCategory("inputbox", ClassicASPTokenTypes.INPUTBOX, "InputBox", BUILTIN_FUNCTIONS);
        addKeywordToCategory("len", ClassicASPTokenTypes.LEN, "Len", BUILTIN_FUNCTIONS);
        addKeywordToCategory("left", ClassicASPTokenTypes.LEFT, "Left", BUILTIN_FUNCTIONS);
        addKeywordToCategory("right", ClassicASPTokenTypes.RIGHT, "Right", BUILTIN_FUNCTIONS);
        addKeywordToCategory("mid", ClassicASPTokenTypes.MID, "Mid", BUILTIN_FUNCTIONS);
        addKeywordToCategory("ucase", ClassicASPTokenTypes.UCASE, "UCase", BUILTIN_FUNCTIONS);
        addKeywordToCategory("lcase", ClassicASPTokenTypes.LCASE, "LCase", BUILTIN_FUNCTIONS);
        addKeywordToCategory("trim", ClassicASPTokenTypes.TRIM, "Trim", BUILTIN_FUNCTIONS);
        addKeywordToCategory("replace", ClassicASPTokenTypes.REPLACE, "Replace", BUILTIN_FUNCTIONS);
        addKeywordToCategory("split", ClassicASPTokenTypes.SPLIT, "Split", BUILTIN_FUNCTIONS);
        addKeywordToCategory("join", ClassicASPTokenTypes.JOIN, "Join", BUILTIN_FUNCTIONS);
        addKeywordToCategory("instr", ClassicASPTokenTypes.INSTR, "InStr", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cstr", ClassicASPTokenTypes.CSTR, "CStr", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cint", ClassicASPTokenTypes.CINT, "CInt", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cdbl", ClassicASPTokenTypes.CDBL, "CDbl", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cbool", ClassicASPTokenTypes.CBOOL, "CBool", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cdate", ClassicASPTokenTypes.CDATE, "CDate", BUILTIN_FUNCTIONS);

        // 根据菜鸟教程添加更多重要内置函数
        addKeywordToCategory("abs", ClassicASPTokenTypes.ABS, "Abs", BUILTIN_FUNCTIONS);
        addKeywordToCategory("int", ClassicASPTokenTypes.INT, "Int", BUILTIN_FUNCTIONS);
        addKeywordToCategory("fix", ClassicASPTokenTypes.FIX, "Fix", BUILTIN_FUNCTIONS);
        addKeywordToCategory("round", ClassicASPTokenTypes.ROUND, "Round", BUILTIN_FUNCTIONS);
        addKeywordToCategory("rnd", ClassicASPTokenTypes.RND, "Rnd", BUILTIN_FUNCTIONS);
        addKeywordToCategory("sqr", ClassicASPTokenTypes.SQR, "Sqr", BUILTIN_FUNCTIONS);
        addKeywordToCategory("sin", ClassicASPTokenTypes.SIN, "Sin", BUILTIN_FUNCTIONS);
        addKeywordToCategory("cos", ClassicASPTokenTypes.COS, "Cos", BUILTIN_FUNCTIONS);
        addKeywordToCategory("tan", ClassicASPTokenTypes.TAN, "Tan", BUILTIN_FUNCTIONS);
        addKeywordToCategory("atn", ClassicASPTokenTypes.ATN, "Atn", BUILTIN_FUNCTIONS);
        addKeywordToCategory("exp", ClassicASPTokenTypes.EXP, "Exp", BUILTIN_FUNCTIONS);
        addKeywordToCategory("log", ClassicASPTokenTypes.LOG, "Log", BUILTIN_FUNCTIONS);
        addKeywordToCategory("hex", ClassicASPTokenTypes.HEX, "Hex", BUILTIN_FUNCTIONS);
        addKeywordToCategory("oct", ClassicASPTokenTypes.OCT, "Oct", BUILTIN_FUNCTIONS);
        addKeywordToCategory("asc", ClassicASPTokenTypes.ASC, "Asc", BUILTIN_FUNCTIONS);
        addKeywordToCategory("chr", ClassicASPTokenTypes.CHR, "Chr", BUILTIN_FUNCTIONS);
        addKeywordToCategory("date", ClassicASPTokenTypes.DATE, "Date", BUILTIN_FUNCTIONS);
        addKeywordToCategory("time", ClassicASPTokenTypes.TIME, "Time", BUILTIN_FUNCTIONS);
        addKeywordToCategory("now", ClassicASPTokenTypes.NOW, "Now", BUILTIN_FUNCTIONS);
        addKeywordToCategory("day", ClassicASPTokenTypes.DAY, "Day", BUILTIN_FUNCTIONS);
        addKeywordToCategory("month", ClassicASPTokenTypes.MONTH, "Month", BUILTIN_FUNCTIONS);
        addKeywordToCategory("year", ClassicASPTokenTypes.YEAR, "Year", BUILTIN_FUNCTIONS);
        addKeywordToCategory("hour", ClassicASPTokenTypes.HOUR, "Hour", BUILTIN_FUNCTIONS);
        addKeywordToCategory("minute", ClassicASPTokenTypes.MINUTE, "Minute", BUILTIN_FUNCTIONS);
        addKeywordToCategory("second", ClassicASPTokenTypes.SECOND, "Second", BUILTIN_FUNCTIONS);
        addKeywordToCategory("weekday", ClassicASPTokenTypes.WEEKDAY, "Weekday", BUILTIN_FUNCTIONS);
        addKeywordToCategory("typename", ClassicASPTokenTypes.TYPENAME, "TypeName", BUILTIN_FUNCTIONS);
        addKeywordToCategory("vartype", ClassicASPTokenTypes.VARTYPE, "VarType", BUILTIN_FUNCTIONS);
        addKeywordToCategory("eval", ClassicASPTokenTypes.EVAL, "Eval", BUILTIN_FUNCTIONS);
        addKeywordToCategory("rgb", ClassicASPTokenTypes.RGB, "RGB", BUILTIN_FUNCTIONS);

        // ASP内置对象
        addKeyword("response", ClassicASPTokenTypes.RESPONSE, "Response");
        addKeyword("request", ClassicASPTokenTypes.REQUEST, "Request");
        addKeyword("server", ClassicASPTokenTypes.SERVER, "Server");
        addKeyword("session", ClassicASPTokenTypes.SESSION, "Session");
        addKeyword("application", ClassicASPTokenTypes.APPLICATION, "Application");
        addKeyword("array", ClassicASPTokenTypes.ARRAY, "Array");

        // 复合关键词 - 需要特殊处理
        CASE_MAP.put("end if", "End If");
        CASE_MAP.put("end function", "End Function");
        CASE_MAP.put("end sub", "End Sub");
        CASE_MAP.put("end for", "End For");
        CASE_MAP.put("end while", "End While");
        CASE_MAP.put("end do", "End Do");
        CASE_MAP.put("end loop", "End Loop");
        CASE_MAP.put("end select", "End Select");
        CASE_MAP.put("end property", "End Property");

        // 内置函数和方法 - 这些通常以复合形式出现
        KEYWORD_MAP.put("response.write", ClassicASPTokenTypes.RESPONSE_WRITE);
        KEYWORD_MAP.put("request.form", ClassicASPTokenTypes.REQUEST_FORM);
        KEYWORD_MAP.put("request.querystring", ClassicASPTokenTypes.REQUEST_QUERYSTRING);
        KEYWORD_MAP.put("server.createobject", ClassicASPTokenTypes.SERVER_CREATEOBJECT);
    }

    /**
     * 添加关键词到映射表
     */
    private static void addKeyword(String keyword, IElementType tokenType, String properCase) {
        KEYWORD_MAP.put(keyword.toLowerCase(), tokenType);
        CASE_MAP.put(keyword.toLowerCase(), properCase);
    }

    /**
     * 添加关键词到指定分类
     */
    private static void addKeywordToCategory(String keyword, IElementType tokenType, String properCase, Set<String> category) {
        addKeyword(keyword, tokenType, properCase);
        category.add(keyword.toLowerCase());
    }

    /**
     * 获取关键词对应的token类型
     * @param word 关键词（不区分大小写）
     * @return 对应的IElementType，如果不是关键词则返回null
     */
    public static IElementType getTokenType(String word) {
        if (KEYWORD_MAP == null) {
            initializeKeywords();
        }
        return KEYWORD_MAP.get(word.toLowerCase());
    }

    /**
     * 检查是否为关键词
     * @param word 要检查的词
     * @return 是否为关键词
     */
    public static boolean isKeyword(String word) {
        if (KEYWORD_MAP == null) {
            initializeKeywords();
        }
        return KEYWORD_MAP.containsKey(word.toLowerCase());
    }

    /**
     * 获取所有关键词的大小写映射（用于格式化）
     * @return 关键词大小写映射表
     */
    public static Map<String, String> getCaseMap() {
        if (CASE_MAP == null) {
            initializeKeywords();
        }
        return new HashMap<>(CASE_MAP);
    }

    /**
     * 获取关键词的正确大小写形式
     * @param word 关键词（任意大小写）
     * @return 正确的大小写形式，如果不是关键词则返回原词
     */
    public static String getProperCase(String word) {
        if (CASE_MAP == null) {
            initializeKeywords();
        }
        String lowerWord = word.toLowerCase();
        return CASE_MAP.getOrDefault(lowerWord, word);
    }

    /**
     * 获取所有关键词到token类型的映射
     * @return 完整的关键词映射表
     */
    public static Map<String, IElementType> getKeywordMap() {
        if (KEYWORD_MAP == null) {
            initializeKeywords();
        }
        return new HashMap<>(KEYWORD_MAP);
    }

    // === 分类访问器方法 ===

    /**
     * 获取内置函数关键词
     */
    public static Set<String> getBuiltinFunctions() {
        if (BUILTIN_FUNCTIONS == null) {
            initializeKeywords();
        }
        return new HashSet<>(BUILTIN_FUNCTIONS);
    }

    /**
     * 获取过程关键词（function、sub、property）
     */
    public static Set<String> getProcedureKeywords() {
        if (PROCEDURE_KEYWORDS == null) {
            initializeKeywords();
        }
        return new HashSet<>(PROCEDURE_KEYWORDS);
    }

    /**
     * 获取循环语句关键词
     */
    public static Set<String> getLoopKeywords() {
        if (LOOP_KEYWORDS == null) {
            initializeKeywords();
        }
        return new HashSet<>(LOOP_KEYWORDS);
    }

    /**
     * 获取条件语句关键词
     */
    public static Set<String> getConditionKeywords() {
        if (CONDITION_KEYWORDS == null) {
            initializeKeywords();
        }
        return new HashSet<>(CONDITION_KEYWORDS);
    }

    /**
     * 获取异常处理关键词
     */
    public static Set<String> getExceptionKeywords() {
        if (EXCEPTION_KEYWORDS == null) {
            initializeKeywords();
        }
        return new HashSet<>(EXCEPTION_KEYWORDS);
    }

    /**
     * 获取内置对象关键词
     */
    public static Set<String> getBuiltinObjects() {
        if (BUILTIN_OBJECTS == null) {
            initializeKeywords();
        }
        return new HashSet<>(BUILTIN_OBJECTS);
    }

    /**
     * 获取声明关键词
     */
    public static Set<String> getDeclarationKeywords() {
        if (DECLARATION_KEYWORDS == null) {
            initializeKeywords();
        }
        return new HashSet<>(DECLARATION_KEYWORDS);
    }

    /**
     * 获取选择语句关键词
     */
    public static Set<String> getSelectionKeywords() {
        if (SELECTION_KEYWORDS == null) {
            initializeKeywords();
        }
        return new HashSet<>(SELECTION_KEYWORDS);
    }

    /**
     * 检查关键词是否属于指定分类
     */
    public static boolean isKeywordInCategory(String word, String category) {
        if (KEYWORD_MAP == null) {
            initializeKeywords();
        }
        String lowerWord = word.toLowerCase();
        switch (category.toLowerCase()) {
            case "builtin_functions": return BUILTIN_FUNCTIONS.contains(lowerWord);
            case "procedure": return PROCEDURE_KEYWORDS.contains(lowerWord);
            case "loop": return LOOP_KEYWORDS.contains(lowerWord);
            case "condition": return CONDITION_KEYWORDS.contains(lowerWord);
            case "exception": return EXCEPTION_KEYWORDS.contains(lowerWord);
            case "builtin_objects": return BUILTIN_OBJECTS.contains(lowerWord);
            case "declaration": return DECLARATION_KEYWORDS.contains(lowerWord);
            case "selection": return SELECTION_KEYWORDS.contains(lowerWord);
            default: return false;
        }
    }
}