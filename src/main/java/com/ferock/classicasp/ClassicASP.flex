package com.ferock.classicasp;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.ferock.classicasp.psi.ClassicASTypes.DIM;
import static com.ferock.classicasp.psi.ClassicASTypes.SET;
import static com.ferock.classicasp.psi.ClassicASTypes.RESPONSE;
import static com.ferock.classicasp.psi.ClassicASTypes.REQUEST;
import static com.ferock.classicasp.psi.ClassicASTypes.SERVER;
import static com.ferock.classicasp.psi.ClassicASTypes.SESSION;
import static com.ferock.classicasp.psi.ClassicASTypes.APPLICATION;
import static com.ferock.classicasp.psi.ClassicASTypes.WRITE;
import static com.ferock.classicasp.psi.ClassicASTypes.IF;
import static com.ferock.classicasp.psi.ClassicASTypes.THEN;
import static com.ferock.classicasp.psi.ClassicASTypes.ELSE;
import static com.ferock.classicasp.psi.ClassicASTypes.END_IF;
import static com.ferock.classicasp.psi.ClassicASTypes.FOR;
import static com.ferock.classicasp.psi.ClassicASTypes.NEXT;
import static com.ferock.classicasp.psi.ClassicASTypes.WHILE;
import static com.ferock.classicasp.psi.ClassicASTypes.WEND;
import static com.ferock.classicasp.psi.ClassicASTypes.DO;
import static com.ferock.classicasp.psi.ClassicASTypes.LOOP;
import static com.ferock.classicasp.psi.ClassicASTypes.FUNCTION;
import static com.ferock.classicasp.psi.ClassicASTypes.SUB;
import static com.ferock.classicasp.psi.ClassicASTypes.END_FUNCTION;
import static com.ferock.classicasp.psi.ClassicASTypes.END_SUB;
import static com.ferock.classicasp.psi.ClassicASTypes.SELECT;
import static com.ferock.classicasp.psi.ClassicASTypes.CASE;
import static com.ferock.classicasp.psi.ClassicASTypes.END_SELECT;
import static com.ferock.classicasp.psi.ClassicASTypes.IDENTIFIER;
import static com.ferock.classicasp.psi.ClassicASTypes.ASP_OPEN;
import static com.ferock.classicasp.psi.ClassicASTypes.ASP_CLOSE;
import static com.ferock.classicasp.psi.ClassicASTypes.EQUALS;
import static com.ferock.classicasp.psi.ClassicASTypes.LESS_THAN;
import static com.ferock.classicasp.psi.ClassicASTypes.GREATER_THAN;
import static com.ferock.classicasp.psi.ClassicASTypes.COMMA;
import static com.ferock.classicasp.psi.ClassicASTypes.LPAREN;
import static com.ferock.classicasp.psi.ClassicASTypes.RPAREN;
import static com.ferock.classicasp.psi.ClassicASTypes.DOT;
import static com.ferock.classicasp.psi.ClassicASTypes.QUOTE;
import static com.ferock.classicasp.psi.ClassicASTypes.NEQ;
import static com.ferock.classicasp.psi.ClassicASTypes.LESS_EQUAL;
import static com.ferock.classicasp.psi.ClassicASTypes.GREATER_EQUAL;
import static com.ferock.classicasp.psi.ClassicASTypes.WHITE_SPACE;

%%
%public
%class ClassicASPLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%ignorecase

CRLF=[\r\n]
WHITE_SPACE=[ \t\n\x0B\f\r]+
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
COMMENT='[^\r\n]*
STRING_LITERAL=\"[^\"]*\"
NUMBER_LITERAL=[0-9]+(\.[0-9]+)?

%%

<YYINITIAL> {
    {WHITE_SPACE}   {
//        System.out.println("词法分析器: 识别到空白字符，长度: " + yylength());
        return WHITE_SPACE;
    }
    {COMMENT}       { return com.ferock.classicasp.psi.ClassicASTypes.COMMENT; }
    {STRING_LITERAL} { return com.ferock.classicasp.psi.ClassicASTypes.STRING_LITERAL; }
    {NUMBER_LITERAL} { return com.ferock.classicasp.psi.ClassicASTypes.NUMBER_LITERAL; }
        "<%@"
        {
//            System.out.println("词法分析器: 识别到ASP指令开始标签 <%@");
            return com.ferock.classicasp.psi.ClassicASTypes.ASP_DIRECTIVE_START;
        }
    "<%"            {
//        System.out.println("词法分析器: 识别到ASP开始标签，当前位置: " + yylength());
        return ASP_OPEN;
    }
    "%>"            {
//        System.out.println("词法分析器: 识别到ASP结束标签，当前位置: " + yylength());
        return ASP_CLOSE;
    }
    // 日期字面量：#...#（内部不含#）
    #[^#\r\n]*# { return com.ferock.classicasp.psi.ClassicASTypes.NUMBER_LITERAL; }

    "Dim"           { return DIM; }
    "Set"           { return SET; }
    "Response"      { return RESPONSE; }
    "Request"       { return REQUEST; }
    "Server"        { return SERVER; }
    "Session"       { return SESSION; }
    "Application"   { return APPLICATION; }
    "Write"         { return WRITE; }
    "If"            { return IF; }
    "Then"          { return THEN; }
    "Else"          { return ELSE; }
    "ElseIf"        { return com.ferock.classicasp.psi.ClassicASTypes.ELSEIF; }
    "End If"        { return END_IF; }
    "For"           { return FOR; }
    "Next"          { return NEXT; }
    "While"         { return WHILE; }
    "Wend"          { return WEND; }
    "Do"            { return DO; }
    "Loop"          { return LOOP; }
    "Function"      { return FUNCTION; }
    "Sub"           { return SUB; }
    "End Function"  { return END_FUNCTION; }
    "End Sub"       { return END_SUB; }
    "Class"         { return com.ferock.classicasp.psi.ClassicASTypes.CLASS; }
    "End Class"     { return com.ferock.classicasp.psi.ClassicASTypes.END_CLASS; }
    "Public"        { return com.ferock.classicasp.psi.ClassicASTypes.PUBLIC; }
    "Private"       { return com.ferock.classicasp.psi.ClassicASTypes.PRIVATE; }
    "Select"        { return SELECT; }
    "Case"          { return CASE; }
    "End Select"    { return END_SELECT; }
    "Language"      {
//        System.out.println("词法分析器: 识别到Language关键词");
        return com.ferock.classicasp.psi.ClassicASTypes.LANGUAGE_KEYWORD;
    }

    // 内置函数
    "Len"           { return IDENTIFIER; }
    "Mid"           { return IDENTIFIER; }
    "Left"          { return IDENTIFIER; }
    "Right"         { return IDENTIFIER; }
    "UCase"         { return IDENTIFIER; }
    "LCase"         { return IDENTIFIER; }
    "Trim"          { return IDENTIFIER; }
    "Replace"       { return IDENTIFIER; }
    "Split"         { return IDENTIFIER; }
    "Join"          { return IDENTIFIER; }
    "InStr"         { return IDENTIFIER; }
    "IsEmpty"       { return IDENTIFIER; }
    "IsNull"        { return IDENTIFIER; }
    "IsNumeric"     { return IDENTIFIER; }
    "IsDate"        { return IDENTIFIER; }
    "CStr"          { return IDENTIFIER; }
    "CInt"          { return IDENTIFIER; }
    "CDbl"          { return IDENTIFIER; }
    "CBool"         { return IDENTIFIER; }
    "CDate"         { return IDENTIFIER; }
    "MsgBox"        { return IDENTIFIER; }
    "InputBox"      { return IDENTIFIER; }

    // 运算符
    "="             { return EQUALS; }
    "<"             { return LESS_THAN; }
    ">"             { return GREATER_THAN; }
    "<>"            { return NEQ; }
    "<="            { return LESS_EQUAL; }
    ">="            { return GREATER_EQUAL; }

    // 标点符号
    ","             { return COMMA; }
    "."             { return DOT; }
    "("             { return LPAREN; }
    ")"             { return RPAREN; }
    "\""            { return QUOTE; }

    // 标识符 - 必须放在所有关键词之后
    {IDENTIFIER}    { return IDENTIFIER; }

    // 其他字符
    [^]             { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}
