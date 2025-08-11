package com.ferock.classicasp.highlighter;

import com.intellij.psi.tree.IElementType;
import com.ferock.classicasp.ClassicASPLanguage;

public class ClassicASPTokenTypes {
    public static final IElementType RESPONSE_WRITE = new IElementType("RESPONSE_WRITE", ClassicASPLanguage.INSTANCE);
    public static final IElementType REQUEST_FORM = new IElementType("REQUEST_FORM", ClassicASPLanguage.INSTANCE);
    public static final IElementType REQUEST_QUERYSTRING = new IElementType("REQUEST_QUERYSTRING", ClassicASPLanguage.INSTANCE);

    // ASP 内置对象和函数
    public static final IElementType SERVER_CREATEOBJECT = new IElementType("SERVER_CREATEOBJECT", ClassicASPLanguage.INSTANCE);
    public static final IElementType SESSION = new IElementType("SESSION", ClassicASPLanguage.INSTANCE);
    public static final IElementType APPLICATION = new IElementType("APPLICATION", ClassicASPLanguage.INSTANCE);
    public static final IElementType ERR = new IElementType("ERR", ClassicASPLanguage.INSTANCE);
    public static final IElementType ERROR = new IElementType("ERROR", ClassicASPLanguage.INSTANCE);
    public static final IElementType ARRAY = new IElementType("ARRAY", ClassicASPLanguage.INSTANCE);
    public static final IElementType EACH = new IElementType("EACH", ClassicASPLanguage.INSTANCE);
    public static final IElementType IN = new IElementType("IN", ClassicASPLanguage.INSTANCE);
    public static final IElementType RESPONSE = new IElementType("RESPONSE", ClassicASPLanguage.INSTANCE);
    public static final IElementType REQUEST = new IElementType("REQUEST", ClassicASPLanguage.INSTANCE);
    public static final IElementType SERVER = new IElementType("SERVER", ClassicASPLanguage.INSTANCE);

    // 新增关键词
    public static final IElementType CLASS = new IElementType("CLASS", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_CLASS = new IElementType("END_CLASS", ClassicASPLanguage.INSTANCE);
    public static final IElementType PUBLIC = new IElementType("PUBLIC", ClassicASPLanguage.INSTANCE);
    public static final IElementType PRIVATE = new IElementType("PRIVATE", ClassicASPLanguage.INSTANCE);

    // ASP指令关键字
    public static final IElementType OPTION = new IElementType("OPTION", ClassicASPLanguage.INSTANCE);
    public static final IElementType EXPLICIT = new IElementType("EXPLICIT", ClassicASPLanguage.INSTANCE);
    public static final IElementType LANGUAGE = new IElementType("LANGUAGE", ClassicASPLanguage.INSTANCE);
    public static final IElementType CODEPAGE = new IElementType("CODEPAGE", ClassicASPLanguage.INSTANCE);

    public static final IElementType ASP_START = new IElementType("ASP_START", ClassicASPLanguage.INSTANCE);
    public static final IElementType ASP_END = new IElementType("ASP_END", ClassicASPLanguage.INSTANCE);
    public static final IElementType ASP_DIRECTIVE = new IElementType("ASP_DIRECTIVE", ClassicASPLanguage.INSTANCE);  // @ 符号

    public static final IElementType DIM = new IElementType("DIM", ClassicASPLanguage.INSTANCE);
    public static final IElementType SET = new IElementType("SET", ClassicASPLanguage.INSTANCE);
    public static final IElementType IF = new IElementType("IF", ClassicASPLanguage.INSTANCE);
    public static final IElementType THEN = new IElementType("THEN", ClassicASPLanguage.INSTANCE);
    public static final IElementType ELSE = new IElementType("ELSE", ClassicASPLanguage.INSTANCE);
    public static final IElementType ELSEIF = new IElementType("ELSEIF", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_IF = new IElementType("END_IF", ClassicASPLanguage.INSTANCE);
    public static final IElementType END = new IElementType("END", ClassicASPLanguage.INSTANCE);
    public static final IElementType FOR = new IElementType("FOR", ClassicASPLanguage.INSTANCE);
    public static final IElementType NEXT = new IElementType("NEXT", ClassicASPLanguage.INSTANCE);
    public static final IElementType WHILE = new IElementType("WHILE", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_WHILE = new IElementType("END_WHILE", ClassicASPLanguage.INSTANCE);
    public static final IElementType WEND = new IElementType("WEND", ClassicASPLanguage.INSTANCE);
    public static final IElementType DO = new IElementType("DO", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_DO = new IElementType("END_DO", ClassicASPLanguage.INSTANCE);
    public static final IElementType LOOP = new IElementType("LOOP", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_LOOP = new IElementType("END_LOOP", ClassicASPLanguage.INSTANCE);
    public static final IElementType FUNCTION = new IElementType("FUNCTION", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_FUNCTION = new IElementType("END_FUNCTION", ClassicASPLanguage.INSTANCE);
    public static final IElementType SUB = new IElementType("SUB", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_SUB = new IElementType("END_SUB", ClassicASPLanguage.INSTANCE);

    // 异常处理关键词
    public static final IElementType ON = new IElementType("ON", ClassicASPLanguage.INSTANCE);
    public static final IElementType RESUME = new IElementType("RESUME", ClassicASPLanguage.INSTANCE);
    public static final IElementType GOTO = new IElementType("GOTO", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_FOR = new IElementType("END_FOR", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_SELECT = new IElementType("END_SELECT", ClassicASPLanguage.INSTANCE);
    public static final IElementType END_PROPERTY = new IElementType("END_PROPERTY", ClassicASPLanguage.INSTANCE);
    public static final IElementType SELECT = new IElementType("SELECT", ClassicASPLanguage.INSTANCE);
    public static final IElementType CASE = new IElementType("CASE", ClassicASPLanguage.INSTANCE);
    public static final IElementType PROPERTY = new IElementType("PROPERTY", ClassicASPLanguage.INSTANCE);

    // 算术运算符
    public static final IElementType POWER = new IElementType("POWER", ClassicASPLanguage.INSTANCE);
    public static final IElementType MULTIPLY = new IElementType("MULTIPLY", ClassicASPLanguage.INSTANCE);
    public static final IElementType DIVIDE = new IElementType("DIVIDE", ClassicASPLanguage.INSTANCE);
    public static final IElementType INT_DIVIDE = new IElementType("INT_DIVIDE", ClassicASPLanguage.INSTANCE);
    public static final IElementType MOD = new IElementType("MOD", ClassicASPLanguage.INSTANCE);
    public static final IElementType PLUS = new IElementType("PLUS", ClassicASPLanguage.INSTANCE);
    public static final IElementType MINUS = new IElementType("MINUS", ClassicASPLanguage.INSTANCE);

    // 字符串连接运算符
    public static final IElementType CONCATENATE = new IElementType("CONCATENATE", ClassicASPLanguage.INSTANCE);

    // 比较运算符
    public static final IElementType EQUALS = new IElementType("EQUALS", ClassicASPLanguage.INSTANCE);
    public static final IElementType NEQ = new IElementType("NEQ", ClassicASPLanguage.INSTANCE);
    public static final IElementType LESS_THAN = new IElementType("LESS_THAN", ClassicASPLanguage.INSTANCE);
    public static final IElementType GREATER_THAN = new IElementType("GREATER_THAN", ClassicASPLanguage.INSTANCE);
    public static final IElementType LESS_EQUAL = new IElementType("LESS_EQUAL", ClassicASPLanguage.INSTANCE);
    public static final IElementType GREATER_EQUAL = new IElementType("GREATER_EQUAL", ClassicASPLanguage.INSTANCE);
    public static final IElementType IS = new IElementType("IS", ClassicASPLanguage.INSTANCE);

    // 逻辑运算符关键字
    public static final IElementType NOT = new IElementType("NOT", ClassicASPLanguage.INSTANCE);
    public static final IElementType AND = new IElementType("AND", ClassicASPLanguage.INSTANCE);
    public static final IElementType OR = new IElementType("OR", ClassicASPLanguage.INSTANCE);
    public static final IElementType XOR = new IElementType("XOR", ClassicASPLanguage.INSTANCE);
    public static final IElementType EQV = new IElementType("EQV", ClassicASPLanguage.INSTANCE);
    public static final IElementType IMP = new IElementType("IMP", ClassicASPLanguage.INSTANCE);

    public static final IElementType LPAREN = new IElementType("LPAREN", ClassicASPLanguage.INSTANCE);
    public static final IElementType RPAREN = new IElementType("RPAREN", ClassicASPLanguage.INSTANCE);
    public static final IElementType LBRACKET = new IElementType("LBRACKET", ClassicASPLanguage.INSTANCE);
    public static final IElementType RBRACKET = new IElementType("RBRACKET", ClassicASPLanguage.INSTANCE);
    public static final IElementType DOT = new IElementType("DOT", ClassicASPLanguage.INSTANCE);
    public static final IElementType COLON = new IElementType("COLON", ClassicASPLanguage.INSTANCE);
    public static final IElementType HASH = new IElementType("HASH", ClassicASPLanguage.INSTANCE);
    public static final IElementType COMMA = new IElementType("COMMA", ClassicASPLanguage.INSTANCE);
    public static final IElementType SEMICOLON = new IElementType("SEMICOLON", ClassicASPLanguage.INSTANCE);

    public static final IElementType STRING_START = new IElementType("STRING_START", ClassicASPLanguage.INSTANCE);
    public static final IElementType STRING_CONTENT = new IElementType("STRING_CONTENT", ClassicASPLanguage.INSTANCE);
    public static final IElementType STRING_END = new IElementType("STRING_END", ClassicASPLanguage.INSTANCE);
    public static final IElementType DATE_LITERAL = new IElementType("DATE_LITERAL", ClassicASPLanguage.INSTANCE);

    public static final IElementType IDENTIFIER = new IElementType("IDENTIFIER", ClassicASPLanguage.INSTANCE);
    public static final IElementType NUMBER = new IElementType("NUMBER", ClassicASPLanguage.INSTANCE);

    // VBScript特殊值和内置函数
    public static final IElementType EMPTY = new IElementType("EMPTY", ClassicASPLanguage.INSTANCE);
    public static final IElementType NOTHING = new IElementType("NOTHING", ClassicASPLanguage.INSTANCE);
    public static final IElementType NULL = new IElementType("NULL", ClassicASPLanguage.INSTANCE);
    public static final IElementType TRUE = new IElementType("TRUE", ClassicASPLanguage.INSTANCE);
    public static final IElementType FALSE = new IElementType("FALSE", ClassicASPLanguage.INSTANCE);

    // VBScript内置函数
    public static final IElementType ISEMPTY = new IElementType("ISEMPTY", ClassicASPLanguage.INSTANCE);
    public static final IElementType ISNOTHING = new IElementType("ISNOTHING", ClassicASPLanguage.INSTANCE);
    public static final IElementType ISNULL = new IElementType("ISNULL", ClassicASPLanguage.INSTANCE);
    public static final IElementType ISNUMERIC = new IElementType("ISNUMERIC", ClassicASPLanguage.INSTANCE);
    public static final IElementType ISOBJECT = new IElementType("ISOBJECT", ClassicASPLanguage.INSTANCE);
    public static final IElementType ISDATE = new IElementType("ISDATE", ClassicASPLanguage.INSTANCE);

    // 对象方法调用
    public static final IElementType OBJECT_METHOD = new IElementType("OBJECT_METHOD", ClassicASPLanguage.INSTANCE);
    // 通用关键字（来自 YAML 的扩展关键字，统一按关键字颜色高亮）
    public static final IElementType KEYWORD_GENERIC = new IElementType("KEYWORD_GENERIC", ClassicASPLanguage.INSTANCE);

    // 常用VBScript函数
    public static final IElementType MSGBOX = new IElementType("MSGBOX", ClassicASPLanguage.INSTANCE);
    public static final IElementType INPUTBOX = new IElementType("INPUTBOX", ClassicASPLanguage.INSTANCE);
    public static final IElementType LEN = new IElementType("LEN", ClassicASPLanguage.INSTANCE);

    // 数学函数
    public static final IElementType ABS = new IElementType("ABS", ClassicASPLanguage.INSTANCE);
    public static final IElementType INT = new IElementType("INT", ClassicASPLanguage.INSTANCE);
    public static final IElementType FIX = new IElementType("FIX", ClassicASPLanguage.INSTANCE);
    public static final IElementType ROUND = new IElementType("ROUND", ClassicASPLanguage.INSTANCE);
    public static final IElementType RND = new IElementType("RND", ClassicASPLanguage.INSTANCE);
    public static final IElementType SQR = new IElementType("SQR", ClassicASPLanguage.INSTANCE);
    public static final IElementType SIN = new IElementType("SIN", ClassicASPLanguage.INSTANCE);
    public static final IElementType COS = new IElementType("COS", ClassicASPLanguage.INSTANCE);
    public static final IElementType TAN = new IElementType("TAN", ClassicASPLanguage.INSTANCE);
    public static final IElementType ATN = new IElementType("ATN", ClassicASPLanguage.INSTANCE);
    public static final IElementType EXP = new IElementType("EXP", ClassicASPLanguage.INSTANCE);
    public static final IElementType LOG = new IElementType("LOG", ClassicASPLanguage.INSTANCE);

    // 转换函数
    public static final IElementType HEX = new IElementType("HEX", ClassicASPLanguage.INSTANCE);
    public static final IElementType OCT = new IElementType("OCT", ClassicASPLanguage.INSTANCE);
    public static final IElementType ASC = new IElementType("ASC", ClassicASPLanguage.INSTANCE);
    public static final IElementType CHR = new IElementType("CHR", ClassicASPLanguage.INSTANCE);

    // 日期时间函数
    public static final IElementType DATE = new IElementType("DATE", ClassicASPLanguage.INSTANCE);
    public static final IElementType TIME = new IElementType("TIME", ClassicASPLanguage.INSTANCE);
    public static final IElementType NOW = new IElementType("NOW", ClassicASPLanguage.INSTANCE);
    public static final IElementType DAY = new IElementType("DAY", ClassicASPLanguage.INSTANCE);
    public static final IElementType MONTH = new IElementType("MONTH", ClassicASPLanguage.INSTANCE);
    public static final IElementType YEAR = new IElementType("YEAR", ClassicASPLanguage.INSTANCE);
    public static final IElementType HOUR = new IElementType("HOUR", ClassicASPLanguage.INSTANCE);
    public static final IElementType MINUTE = new IElementType("MINUTE", ClassicASPLanguage.INSTANCE);
    public static final IElementType SECOND = new IElementType("SECOND", ClassicASPLanguage.INSTANCE);
    public static final IElementType WEEKDAY = new IElementType("WEEKDAY", ClassicASPLanguage.INSTANCE);

    // 其他函数
    public static final IElementType TYPENAME = new IElementType("TYPENAME", ClassicASPLanguage.INSTANCE);
    public static final IElementType VARTYPE = new IElementType("VARTYPE", ClassicASPLanguage.INSTANCE);
    public static final IElementType EVAL = new IElementType("EVAL", ClassicASPLanguage.INSTANCE);
    public static final IElementType RGB = new IElementType("RGB", ClassicASPLanguage.INSTANCE);
    public static final IElementType LEFT = new IElementType("LEFT", ClassicASPLanguage.INSTANCE);
    public static final IElementType RIGHT = new IElementType("RIGHT", ClassicASPLanguage.INSTANCE);
    public static final IElementType MID = new IElementType("MID", ClassicASPLanguage.INSTANCE);
    public static final IElementType UCASE = new IElementType("UCASE", ClassicASPLanguage.INSTANCE);
    public static final IElementType LCASE = new IElementType("LCASE", ClassicASPLanguage.INSTANCE);
    public static final IElementType TRIM = new IElementType("TRIM", ClassicASPLanguage.INSTANCE);
    public static final IElementType REPLACE = new IElementType("REPLACE", ClassicASPLanguage.INSTANCE);
    public static final IElementType SPLIT = new IElementType("SPLIT", ClassicASPLanguage.INSTANCE);
    public static final IElementType JOIN = new IElementType("JOIN", ClassicASPLanguage.INSTANCE);
    public static final IElementType INSTR = new IElementType("INSTR", ClassicASPLanguage.INSTANCE);
    public static final IElementType CSTR = new IElementType("CSTR", ClassicASPLanguage.INSTANCE);
    public static final IElementType CINT = new IElementType("CINT", ClassicASPLanguage.INSTANCE);
    public static final IElementType CDBL = new IElementType("CDBL", ClassicASPLanguage.INSTANCE);
    public static final IElementType CBOOL = new IElementType("CBOOL", ClassicASPLanguage.INSTANCE);
    public static final IElementType CDATE = new IElementType("CDATE", ClassicASPLanguage.INSTANCE);

    // 基础token类型
    public static final IElementType WHITE_SPACE = new IElementType("WHITE_SPACE", ClassicASPLanguage.INSTANCE);
    public static final IElementType STRING_LITERAL = new IElementType("STRING_LITERAL", ClassicASPLanguage.INSTANCE);
    public static final IElementType NUMBER_LITERAL = new IElementType("NUMBER_LITERAL", ClassicASPLanguage.INSTANCE);
    public static final IElementType BAD_CHARACTER = new IElementType("BAD_CHARACTER", ClassicASPLanguage.INSTANCE);

    // 注释 token 类型
    public static final IElementType COMMENT = new IElementType("COMMENT", ClassicASPLanguage.INSTANCE);

    // @ 符号 token 类型
    public static final IElementType AT_SYMBOL = new IElementType("AT_SYMBOL", ClassicASPLanguage.INSTANCE);

    // HTML 相关 token 类型
    public static final IElementType HTML_TAG = new IElementType("HTML_TAG", ClassicASPLanguage.INSTANCE);
    public static final IElementType HTML_TAG_END = new IElementType("HTML_TAG_END", ClassicASPLanguage.INSTANCE);
    public static final IElementType HTML_ATTRIBUTE = new IElementType("HTML_ATTRIBUTE", ClassicASPLanguage.INSTANCE);
    public static final IElementType HTML_ATTRIBUTE_VALUE = new IElementType("HTML_ATTRIBUTE_VALUE", ClassicASPLanguage.INSTANCE);
    public static final IElementType HTML_TEXT = new IElementType("HTML_TEXT", ClassicASPLanguage.INSTANCE);

    // 自定义方法名
    public static final IElementType CUSTOM_METHOD = new IElementType("CUSTOM_METHOD", ClassicASPLanguage.INSTANCE);
}