package com.ferock.classicasp.parameterinfo;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ferock.classicasp.ClassicASPLanguage;

import java.util.ArrayList;
import java.util.List;

/**
 * Classic ASP 参数提示处理器
 * 提供函数参数信息和说明
 */
public class ClassicASPParameterInfoHandler {

    /**
     * 函数信息类
     */
    public static class FunctionInfo {
        private final String functionName;
        private final String description;
        private final List<Parameter> parameters;
        private final String returnType;

        public FunctionInfo(String functionName, String description, List<Parameter> parameters, String returnType) {
            this.functionName = functionName;
            this.description = description;
            this.parameters = parameters;
            this.returnType = returnType;
        }

        public String getFunctionName() { return functionName; }
        public String getDescription() { return description; }
        public List<Parameter> getParameters() { return parameters; }
        public String getReturnType() { return returnType; }
    }

    /**
     * 参数类
     */
    public static class Parameter {
        private final String name;
        private final String type;
        private final String description;
        private final boolean optional;

        public Parameter(String name, String type, String description, boolean optional) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.optional = optional;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public String getDescription() { return description; }
        public boolean isOptional() { return optional; }
    }

    /**
     * 显示参数提示
     */
    public void showParameterInfo(@NotNull PsiElement element, @NotNull Editor editor) {
        // 实现参数提示逻辑
        // System.out.println("=== Classic ASP 参数提示被触发 ===");
        // System.out.println("当前元素: " + element.getText());

        String functionName = getFunctionNameAtCursor(element);
        if (functionName != null) {
            FunctionInfo info = getFunctionInfo(functionName);
            if (info != null) {
                        // System.out.println("函数: " + info.getFunctionName());
        // System.out.println("描述: " + info.getDescription());
        // System.out.println("参数数量: " + info.getParameters().size());
            }
        }
    }

    /**
     * 更新参数提示
     */
    public void updateParameterInfo(@NotNull PsiElement element) {
        // 实现参数提示更新逻辑
        // System.out.println("=== Classic ASP 参数提示更新 ===");
    }

    /**
     * 获取参数分隔符
     */
    public String getParameterSeparator() {
        return ",";
    }

    /**
     * 获取参数列表开始字符
     */
    public String getParameterListStart() {
        return "(";
    }

    /**
     * 获取参数列表结束字符
     */
    public String getParameterListEnd() {
        return ")";
    }

    /**
     * 获取参数关闭字符
     */
    public String getParameterCloseChars() {
        return ")";
    }

    /**
     * 获取光标位置的函数名
     */
    private String getFunctionNameAtCursor(PsiElement element) {
        // 这里需要根据实际的语法分析来获取函数名
        // 简化实现，返回一个示例函数名
        String text = element.getText();
        // System.out.println("当前元素文本: " + text);

        // 简单的函数名检测
        if (text.contains("Response.Write")) return "Response.Write";
        if (text.contains("Request.Form")) return "Request.Form";
        if (text.contains("Server.CreateObject")) return "Server.CreateObject";
        if (text.contains("Len(")) return "Len";
        if (text.contains("Mid(")) return "Mid";
        if (text.contains("Replace(")) return "Replace";
        if (text.contains("InStr(")) return "InStr";
        if (text.contains("DateAdd(")) return "DateAdd";
        if (text.contains("DateDiff(")) return "DateDiff";
        if (text.contains("Array(")) return "Array";
        if (text.contains("Split(")) return "Split";
        if (text.contains("Join(")) return "Join";
        if (text.contains("UCase(")) return "UCase";
        if (text.contains("LCase(")) return "LCase";
        if (text.contains("Trim(")) return "Trim";
        if (text.contains("CStr(")) return "CStr";
        if (text.contains("CInt(")) return "CInt";
        if (text.contains("CDate(")) return "CDate";
        if (text.contains("IsNull(")) return "IsNull";
        if (text.contains("IsEmpty(")) return "IsEmpty";
        if (text.contains("IsNumeric(")) return "IsNumeric";

        return null;
    }

    /**
     * 根据函数名获取函数信息
     */
    private FunctionInfo getFunctionInfo(String functionName) {
        switch (functionName) {
            case "Response.Write":
                return createResponseWriteInfo();
            case "Request.Form":
                return createRequestFormInfo();
            case "Server.CreateObject":
                return createServerCreateObjectInfo();
            case "Len":
                return createLenInfo();
            case "Mid":
                return createMidInfo();
            case "Replace":
                return createReplaceInfo();
            case "InStr":
                return createInStrInfo();
            case "DateAdd":
                return createDateAddInfo();
            case "DateDiff":
                return createDateDiffInfo();
            case "Array":
                return createArrayInfo();
            case "Split":
                return createSplitInfo();
            case "Join":
                return createJoinInfo();
            case "UCase":
                return createUCaseInfo();
            case "LCase":
                return createLCaseInfo();
            case "Trim":
                return createTrimInfo();
            case "CStr":
                return createCStrInfo();
            case "CInt":
                return createCIntInfo();
            case "CDate":
                return createCDateInfo();
            case "IsNull":
                return createIsNullInfo();
            case "IsEmpty":
                return createIsEmptyInfo();
            case "IsNumeric":
                return createIsNumericInfo();
            default:
                return createDefaultInfo(functionName);
        }
    }

    // 各种函数的参数信息
    private FunctionInfo createResponseWriteInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("text", "String", "要输出的文本内容", false));
        return new FunctionInfo("Response.Write", "向客户端输出字符串", params, "Void");
    }

    private FunctionInfo createRequestFormInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("fieldName", "String", "表单字段名称", false));
        return new FunctionInfo("Request.Form", "获取 POST 表单数据", params, "String");
    }

    private FunctionInfo createServerCreateObjectInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("progID", "String", "COM 对象的程序 ID", false));
        return new FunctionInfo("Server.CreateObject", "创建 COM 对象", params, "Object");
    }

    private FunctionInfo createLenInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("string", "String", "要计算长度的字符串", false));
        return new FunctionInfo("Len", "返回字符串长度", params, "Integer");
    }

    private FunctionInfo createMidInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("string", "String", "源字符串", false));
        params.add(new Parameter("start", "Integer", "开始位置（从1开始）", false));
        params.add(new Parameter("length", "Integer", "要提取的字符数", true));
        return new FunctionInfo("Mid", "从字符串中提取子字符串", params, "String");
    }

    private FunctionInfo createReplaceInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("string", "String", "源字符串", false));
        params.add(new Parameter("find", "String", "要查找的字符串", false));
        params.add(new Parameter("replace", "String", "替换字符串", false));
        params.add(new Parameter("start", "Integer", "开始位置", true));
        params.add(new Parameter("count", "Integer", "替换次数", true));
        params.add(new Parameter("compare", "Integer", "比较方式", true));
        return new FunctionInfo("Replace", "替换字符串中的字符", params, "String");
    }

    private FunctionInfo createInStrInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("start", "Integer", "开始位置", true));
        params.add(new Parameter("string1", "String", "要搜索的字符串", false));
        params.add(new Parameter("string2", "String", "要查找的字符串", false));
        params.add(new Parameter("compare", "Integer", "比较方式", true));
        return new FunctionInfo("InStr", "查找子字符串位置", params, "Integer");
    }

    private FunctionInfo createDateAddInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("interval", "String", "时间间隔类型", false));
        params.add(new Parameter("number", "Integer", "间隔数量", false));
        params.add(new Parameter("date", "Date", "基准日期", false));
        return new FunctionInfo("DateAdd", "添加日期时间间隔", params, "Date");
    }

    private FunctionInfo createDateDiffInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("interval", "String", "时间间隔类型", false));
        params.add(new Parameter("date1", "Date", "第一个日期", false));
        params.add(new Parameter("date2", "Date", "第二个日期", false));
        params.add(new Parameter("firstdayofweek", "Integer", "一周的第一天", true));
        params.add(new Parameter("firstweekofyear", "Integer", "一年的第一周", true));
        return new FunctionInfo("DateDiff", "计算日期时间差", params, "Long");
    }

    private FunctionInfo createArrayInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("arg1", "Variant", "数组元素1", false));
        params.add(new Parameter("arg2", "Variant", "数组元素2", true));
        return new FunctionInfo("Array", "创建数组", params, "Variant()");
    }

    private FunctionInfo createSplitInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "String", "要分割的字符串", false));
        params.add(new Parameter("delimiter", "String", "分隔符", true));
        params.add(new Parameter("limit", "Integer", "分割数量限制", true));
        params.add(new Parameter("compare", "Integer", "比较方式", true));
        return new FunctionInfo("Split", "分割字符串为数组", params, "Variant()");
    }

    private FunctionInfo createJoinInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("sourceArray", "Variant()", "源数组", false));
        params.add(new Parameter("delimiter", "String", "分隔符", true));
        return new FunctionInfo("Join", "连接数组元素", params, "String");
    }

    private FunctionInfo createUCaseInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("string", "String", "要转换的字符串", false));
        return new FunctionInfo("UCase", "转换为大写", params, "String");
    }

    private FunctionInfo createLCaseInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("string", "String", "要转换的字符串", false));
        return new FunctionInfo("LCase", "转换为小写", params, "String");
    }

    private FunctionInfo createTrimInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("string", "String", "要处理的字符串", false));
        return new FunctionInfo("Trim", "移除首尾空格", params, "String");
    }

    private FunctionInfo createCStrInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "Variant", "要转换的表达式", false));
        return new FunctionInfo("CStr", "转换为字符串", params, "String");
    }

    private FunctionInfo createCIntInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "Variant", "要转换的表达式", false));
        return new FunctionInfo("CInt", "转换为整数", params, "Integer");
    }

    private FunctionInfo createCDateInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "Variant", "要转换的表达式", false));
        return new FunctionInfo("CDate", "转换为日期", params, "Date");
    }

    private FunctionInfo createIsNullInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "Variant", "要检查的表达式", false));
        return new FunctionInfo("IsNull", "判断是否为 Null", params, "Boolean");
    }

    private FunctionInfo createIsEmptyInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "Variant", "要检查的表达式", false));
        return new FunctionInfo("IsEmpty", "判断是否为空", params, "Boolean");
    }

    private FunctionInfo createIsNumericInfo() {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("expression", "Variant", "要检查的表达式", false));
        return new FunctionInfo("IsNumeric", "判断是否为数字", params, "Boolean");
    }

    private FunctionInfo createDefaultInfo(String functionName) {
        List<Parameter> params = new ArrayList<>();
        return new FunctionInfo(functionName, "Classic ASP 函数", params, "Variant");
    }
}