package com.ferock.classicasp.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Classic ASP 原生函数和对象管理器
 * 提供所有 Classic ASP 内置函数、对象、属性和方法的定义
 */
public class ClassicASPNativeFunctions {

    /**
     * 获取所有内置对象
     */
    @NotNull
    public static List<LookupElement> getBuiltInObjects() {
        List<LookupElement> objects = new ArrayList<>();

        // 核心对象
        objects.add(LookupElementBuilder.create("Request")
                .withTypeText("请求对象")
                .withTailText(" - 获取客户端提交的数据"));
        objects.add(LookupElementBuilder.create("Response")
                .withTypeText("响应对象")
                .withTailText(" - 向客户端输出数据"));
        objects.add(LookupElementBuilder.create("Server")
                .withTypeText("服务器对象")
                .withTailText(" - 服务器相关功能"));
        objects.add(LookupElementBuilder.create("Session")
                .withTypeText("会话对象")
                .withTailText(" - 存储用户会话信息"));
        objects.add(LookupElementBuilder.create("Application")
                .withTypeText("应用程序对象")
                .withTailText(" - 存储应用程序级变量"));
        objects.add(LookupElementBuilder.create("ObjectContext")
                .withTypeText("对象上下文")
                .withTailText(" - COM+ 事务对象"));

        return objects;
    }

    /**
     * 获取 Request 对象的方法和属性
     */
    @NotNull
    public static List<LookupElement> getRequestMethods() {
        List<LookupElement> methods = new ArrayList<>();

        // Request 集合
        methods.add(LookupElementBuilder.create("Request.Form")
                .withTypeText("表单数据集合")
                .withTailText(" - 获取 POST 表单数据"));
        methods.add(LookupElementBuilder.create("Request.QueryString")
                .withTypeText("查询字符串集合")
                .withTailText(" - 获取 URL 参数"));
        methods.add(LookupElementBuilder.create("Request.Cookies")
                .withTypeText("Cookie 集合")
                .withTailText(" - 获取客户端 Cookie"));
        methods.add(LookupElementBuilder.create("Request.ServerVariables")
                .withTypeText("服务器变量集合")
                .withTailText(" - 获取服务器环境变量"));
        methods.add(LookupElementBuilder.create("Request.ClientCertificate")
                .withTypeText("客户端证书集合")
                .withTailText(" - 获取客户端证书信息"));

        // Request 属性
        methods.add(LookupElementBuilder.create("Request.TotalBytes")
                .withTypeText("属性")
                .withTailText(" - 请求的总字节数"));
        methods.add(LookupElementBuilder.create("Request.BinaryRead")
                .withTypeText("方法")
                .withTailText(" - 读取二进制数据"));

        return methods;
    }

    /**
     * 获取 Response 对象的方法和属性
     */
    @NotNull
    public static List<LookupElement> getResponseMethods() {
        List<LookupElement> methods = new ArrayList<>();

        // Response 方法
        methods.add(LookupElementBuilder.create("Response.Write")
                .withTypeText("方法")
                .withTailText(" - 输出字符串到客户端"));
        methods.add(LookupElementBuilder.create("Response.BinaryWrite")
                .withTypeText("方法")
                .withTailText(" - 输出二进制数据"));
        methods.add(LookupElementBuilder.create("Response.Redirect")
                .withTypeText("方法")
                .withTailText(" - 重定向到其他页面"));
        methods.add(LookupElementBuilder.create("Response.End")
                .withTypeText("方法")
                .withTailText(" - 结束响应并停止执行"));
        methods.add(LookupElementBuilder.create("Response.Clear")
                .withTypeText("方法")
                .withTailText(" - 清除缓冲区"));
        methods.add(LookupElementBuilder.create("Response.Flush")
                .withTypeText("方法")
                .withTailText(" - 立即输出缓冲区内容"));
        methods.add(LookupElementBuilder.create("Response.AppendToLog")
                .withTypeText("方法")
                .withTailText(" - 添加日志条目"));

        // Response 属性
        methods.add(LookupElementBuilder.create("Response.Buffer")
                .withTypeText("属性")
                .withTailText(" - 是否缓冲输出"));
        methods.add(LookupElementBuilder.create("Response.CacheControl")
                .withTypeText("属性")
                .withTailText(" - 缓存控制"));
        methods.add(LookupElementBuilder.create("Response.CharSet")
                .withTypeText("属性")
                .withTailText(" - 字符集"));
        methods.add(LookupElementBuilder.create("Response.ContentType")
                .withTypeText("属性")
                .withTailText(" - 内容类型"));
        methods.add(LookupElementBuilder.create("Response.Expires")
                .withTypeText("属性")
                .withTailText(" - 页面过期时间"));
        methods.add(LookupElementBuilder.create("Response.ExpiresAbsolute")
                .withTypeText("属性")
                .withTailText(" - 绝对过期时间"));
        methods.add(LookupElementBuilder.create("Response.IsClientConnected")
                .withTypeText("属性")
                .withTailText(" - 客户端是否连接"));
        methods.add(LookupElementBuilder.create("Response.PICS")
                .withTypeText("属性")
                .withTailText(" - PICS 标签"));
        methods.add(LookupElementBuilder.create("Response.Status")
                .withTypeText("属性")
                .withTailText(" - HTTP 状态"));

        return methods;
    }

    /**
     * 获取 Server 对象的方法和属性
     */
    @NotNull
    public static List<LookupElement> getServerMethods() {
        List<LookupElement> methods = new ArrayList<>();

        // Server 方法
        methods.add(LookupElementBuilder.create("Server.CreateObject")
                .withTypeText("方法")
                .withTailText(" - 创建 COM 对象"));
        methods.add(LookupElementBuilder.create("Server.HTMLEncode")
                .withTypeText("方法")
                .withTailText(" - HTML 编码"));
        methods.add(LookupElementBuilder.create("Server.URLEncode")
                .withTypeText("方法")
                .withTailText(" - URL 编码"));
        methods.add(LookupElementBuilder.create("Server.MapPath")
                .withTypeText("方法")
                .withTailText(" - 将虚拟路径映射为物理路径"));
        methods.add(LookupElementBuilder.create("Server.Execute")
                .withTypeText("方法")
                .withTailText(" - 执行另一个 ASP 页面"));
        methods.add(LookupElementBuilder.create("Server.Transfer")
                .withTypeText("方法")
                .withTailText(" - 传输到另一个页面"));
        methods.add(LookupElementBuilder.create("Server.GetLastError")
                .withTypeText("方法")
                .withTailText(" - 获取最后一个错误"));

        // Server 属性
        methods.add(LookupElementBuilder.create("Server.ScriptTimeout")
                .withTypeText("属性")
                .withTailText(" - 脚本超时时间"));

        return methods;
    }

    /**
     * 获取 Session 对象的方法和属性
     */
    @NotNull
    public static List<LookupElement> getSessionMethods() {
        List<LookupElement> methods = new ArrayList<>();

        // Session 方法
        methods.add(LookupElementBuilder.create("Session.Abandon")
                .withTypeText("方法")
                .withTailText(" - 销毁会话"));
        methods.add(LookupElementBuilder.create("Session.Contents.Remove")
                .withTypeText("方法")
                .withTailText(" - 移除会话变量"));
        methods.add(LookupElementBuilder.create("Session.Contents.RemoveAll")
                .withTypeText("方法")
                .withTailText(" - 移除所有会话变量"));

        // Session 属性
        methods.add(LookupElementBuilder.create("Session.SessionID")
                .withTypeText("属性")
                .withTailText(" - 会话 ID"));
        methods.add(LookupElementBuilder.create("Session.Timeout")
                .withTypeText("属性")
                .withTailText(" - 会话超时时间"));
        methods.add(LookupElementBuilder.create("Session.CodePage")
                .withTypeText("属性")
                .withTailText(" - 代码页"));
        methods.add(LookupElementBuilder.create("Session.LCID")
                .withTypeText("属性")
                .withTailText(" - 区域设置 ID"));
        methods.add(LookupElementBuilder.create("Session.StaticObjects")
                .withTypeText("属性")
                .withTailText(" - 静态对象集合"));

        return methods;
    }

    /**
     * 获取 Application 对象的方法和属性
     */
    @NotNull
    public static List<LookupElement> getApplicationMethods() {
        List<LookupElement> methods = new ArrayList<>();

        // Application 方法
        methods.add(LookupElementBuilder.create("Application.Lock")
                .withTypeText("方法")
                .withTailText(" - 锁定应用程序"));
        methods.add(LookupElementBuilder.create("Application.UnLock")
                .withTypeText("方法")
                .withTailText(" - 解锁应用程序"));
        methods.add(LookupElementBuilder.create("Application.Contents.Remove")
                .withTypeText("方法")
                .withTailText(" - 移除应用程序变量"));
        methods.add(LookupElementBuilder.create("Application.Contents.RemoveAll")
                .withTypeText("方法")
                .withTailText(" - 移除所有应用程序变量"));

        // Application 属性
        methods.add(LookupElementBuilder.create("Application.StaticObjects")
                .withTypeText("属性")
                .withTailText(" - 静态对象集合"));

        return methods;
    }

    /**
     * 获取 VBScript 内置函数
     */
    @NotNull
    public static List<LookupElement> getVBScriptFunctions() {
        List<LookupElement> functions = new ArrayList<>();

        // 字符串函数
        functions.add(LookupElementBuilder.create("Len")
                .withTypeText("字符串函数")
                .withTailText(" - 返回字符串长度"));
        functions.add(LookupElementBuilder.create("Left")
                .withTypeText("字符串函数")
                .withTailText(" - 返回字符串左侧指定数量的字符"));
        functions.add(LookupElementBuilder.create("Right")
                .withTypeText("字符串函数")
                .withTailText(" - 返回字符串右侧指定数量的字符"));
        functions.add(LookupElementBuilder.create("Mid")
                .withTypeText("字符串函数")
                .withTailText(" - 返回字符串中指定位置的字符"));
        functions.add(LookupElementBuilder.create("UCase")
                .withTypeText("字符串函数")
                .withTailText(" - 转换为大写"));
        functions.add(LookupElementBuilder.create("LCase")
                .withTypeText("字符串函数")
                .withTailText(" - 转换为小写"));
        functions.add(LookupElementBuilder.create("Trim")
                .withTypeText("字符串函数")
                .withTailText(" - 移除首尾空格"));
        functions.add(LookupElementBuilder.create("LTrim")
                .withTypeText("字符串函数")
                .withTailText(" - 移除左侧空格"));
        functions.add(LookupElementBuilder.create("RTrim")
                .withTypeText("字符串函数")
                .withTailText(" - 移除右侧空格"));
        functions.add(LookupElementBuilder.create("Replace")
                .withTypeText("字符串函数")
                .withTailText(" - 替换字符串中的字符"));
        functions.add(LookupElementBuilder.create("InStr")
                .withTypeText("字符串函数")
                .withTailText(" - 查找子字符串位置"));
        functions.add(LookupElementBuilder.create("InStrRev")
                .withTypeText("字符串函数")
                .withTailText(" - 从右向左查找子字符串"));
        functions.add(LookupElementBuilder.create("StrComp")
                .withTypeText("字符串函数")
                .withTailText(" - 比较两个字符串"));
        functions.add(LookupElementBuilder.create("Space")
                .withTypeText("字符串函数")
                .withTailText(" - 创建指定数量的空格"));
        functions.add(LookupElementBuilder.create("String")
                .withTypeText("字符串函数")
                .withTailText(" - 创建重复字符的字符串"));

        // 数值函数
        functions.add(LookupElementBuilder.create("Abs")
                .withTypeText("数值函数")
                .withTailText(" - 返回绝对值"));
        functions.add(LookupElementBuilder.create("Int")
                .withTypeText("数值函数")
                .withTailText(" - 返回整数部分"));
        functions.add(LookupElementBuilder.create("Fix")
                .withTypeText("数值函数")
                .withTailText(" - 返回整数部分"));
        functions.add(LookupElementBuilder.create("Round")
                .withTypeText("数值函数")
                .withTailText(" - 四舍五入"));
        functions.add(LookupElementBuilder.create("Sqr")
                .withTypeText("数值函数")
                .withTailText(" - 返回平方根"));
        functions.add(LookupElementBuilder.create("Sin")
                .withTypeText("数值函数")
                .withTailText(" - 正弦函数"));
        functions.add(LookupElementBuilder.create("Cos")
                .withTypeText("数值函数")
                .withTailText(" - 余弦函数"));
        functions.add(LookupElementBuilder.create("Tan")
                .withTypeText("数值函数")
                .withTailText(" - 正切函数"));
        functions.add(LookupElementBuilder.create("Atn")
                .withTypeText("数值函数")
                .withTailText(" - 反正切函数"));
        functions.add(LookupElementBuilder.create("Log")
                .withTypeText("数值函数")
                .withTailText(" - 自然对数"));
        functions.add(LookupElementBuilder.create("Exp")
                .withTypeText("数值函数")
                .withTailText(" - 指数函数"));
        functions.add(LookupElementBuilder.create("Rnd")
                .withTypeText("数值函数")
                .withTailText(" - 随机数"));
        functions.add(LookupElementBuilder.create("Randomize")
                .withTypeText("数值函数")
                .withTailText(" - 初始化随机数生成器"));

        // 类型转换函数
        functions.add(LookupElementBuilder.create("CStr")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为字符串"));
        functions.add(LookupElementBuilder.create("CInt")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为整数"));
        functions.add(LookupElementBuilder.create("CLng")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为长整数"));
        functions.add(LookupElementBuilder.create("CSng")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为单精度浮点数"));
        functions.add(LookupElementBuilder.create("CDbl")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为双精度浮点数"));
        functions.add(LookupElementBuilder.create("CBool")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为布尔值"));
        functions.add(LookupElementBuilder.create("CDate")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为日期"));
        functions.add(LookupElementBuilder.create("CCur")
                .withTypeText("类型转换函数")
                .withTailText(" - 转换为货币"));

        // 日期时间函数
        functions.add(LookupElementBuilder.create("Now")
                .withTypeText("日期时间函数")
                .withTailText(" - 当前日期和时间"));
        functions.add(LookupElementBuilder.create("Date")
                .withTypeText("日期时间函数")
                .withTailText(" - 当前日期"));
        functions.add(LookupElementBuilder.create("Time")
                .withTypeText("日期时间函数")
                .withTailText(" - 当前时间"));
        functions.add(LookupElementBuilder.create("Year")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回年份"));
        functions.add(LookupElementBuilder.create("Month")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回月份"));
        functions.add(LookupElementBuilder.create("Day")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回日期"));
        functions.add(LookupElementBuilder.create("Hour")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回小时"));
        functions.add(LookupElementBuilder.create("Minute")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回分钟"));
        functions.add(LookupElementBuilder.create("Second")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回秒数"));
        functions.add(LookupElementBuilder.create("Weekday")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回星期几"));
        functions.add(LookupElementBuilder.create("DateAdd")
                .withTypeText("日期时间函数")
                .withTailText(" - 添加日期时间间隔"));
        functions.add(LookupElementBuilder.create("DateDiff")
                .withTypeText("日期时间函数")
                .withTailText(" - 计算日期时间差"));
        functions.add(LookupElementBuilder.create("DatePart")
                .withTypeText("日期时间函数")
                .withTailText(" - 返回日期的指定部分"));
        functions.add(LookupElementBuilder.create("DateSerial")
                .withTypeText("日期时间函数")
                .withTailText(" - 创建日期值"));
        functions.add(LookupElementBuilder.create("TimeSerial")
                .withTypeText("日期时间函数")
                .withTailText(" - 创建时间值"));
        functions.add(LookupElementBuilder.create("DateValue")
                .withTypeText("日期时间函数")
                .withTailText(" - 转换为日期值"));
        functions.add(LookupElementBuilder.create("TimeValue")
                .withTypeText("日期时间函数")
                .withTailText(" - 转换为时间值"));

        // 数组函数
        functions.add(LookupElementBuilder.create("Array")
                .withTypeText("数组函数")
                .withTailText(" - 创建数组"));
        functions.add(LookupElementBuilder.create("UBound")
                .withTypeText("数组函数")
                .withTailText(" - 返回数组上界"));
        functions.add(LookupElementBuilder.create("LBound")
                .withTypeText("数组函数")
                .withTailText(" - 返回数组下界"));
        functions.add(LookupElementBuilder.create("Join")
                .withTypeText("数组函数")
                .withTailText(" - 连接数组元素"));
        functions.add(LookupElementBuilder.create("Split")
                .withTypeText("数组函数")
                .withTailText(" - 分割字符串为数组"));
        functions.add(LookupElementBuilder.create("Filter")
                .withTypeText("数组函数")
                .withTailText(" - 过滤数组"));

        // 其他函数
        functions.add(LookupElementBuilder.create("IsNull")
                .withTypeText("判断函数")
                .withTailText(" - 判断是否为 Null"));
        functions.add(LookupElementBuilder.create("IsEmpty")
                .withTypeText("判断函数")
                .withTailText(" - 判断是否为空"));
        functions.add(LookupElementBuilder.create("IsNumeric")
                .withTypeText("判断函数")
                .withTailText(" - 判断是否为数字"));
        functions.add(LookupElementBuilder.create("IsDate")
                .withTypeText("判断函数")
                .withTailText(" - 判断是否为日期"));
        functions.add(LookupElementBuilder.create("IsArray")
                .withTypeText("判断函数")
                .withTailText(" - 判断是否为数组"));
        functions.add(LookupElementBuilder.create("IsObject")
                .withTypeText("判断函数")
                .withTailText(" - 判断是否为对象"));
        functions.add(LookupElementBuilder.create("TypeName")
                .withTypeText("类型函数")
                .withTailText(" - 返回变量类型名称"));
        functions.add(LookupElementBuilder.create("VarType")
                .withTypeText("类型函数")
                .withTailText(" - 返回变量类型"));
        functions.add(LookupElementBuilder.create("MsgBox")
                .withTypeText("对话框函数")
                .withTailText(" - 显示消息框"));
        functions.add(LookupElementBuilder.create("InputBox")
                .withTypeText("对话框函数")
                .withTailText(" - 显示输入框"));

        return functions;
    }

    /**
     * 获取 VBScript 关键字
     */
    @NotNull
    public static List<LookupElement> getVBScriptKeywords() {
        List<LookupElement> keywords = new ArrayList<>();

        // 声明关键字
        keywords.add(LookupElementBuilder.create("Dim")
                .withTypeText("声明关键字")
                .withTailText(" - 声明变量"));
        keywords.add(LookupElementBuilder.create("ReDim")
                .withTypeText("声明关键字")
                .withTailText(" - 重新声明数组"));
        keywords.add(LookupElementBuilder.create("Set")
                .withTypeText("声明关键字")
                .withTailText(" - 设置对象引用"));
        keywords.add(LookupElementBuilder.create("Let")
                .withTypeText("声明关键字")
                .withTailText(" - 赋值（可选）"));
        keywords.add(LookupElementBuilder.create("Const")
                .withTypeText("声明关键字")
                .withTailText(" - 声明常量"));

        // 控制流关键字
        keywords.add(LookupElementBuilder.create("If")
                .withTypeText("控制流关键字")
                .withTailText(" - 条件语句"));
        keywords.add(LookupElementBuilder.create("Then")
                .withTypeText("控制流关键字")
                .withTailText(" - If 语句的一部分"));
        keywords.add(LookupElementBuilder.create("Else")
                .withTypeText("控制流关键字")
                .withTailText(" - Else 分支"));
        keywords.add(LookupElementBuilder.create("ElseIf")
                .withTypeText("控制流关键字")
                .withTailText(" - Else If 分支"));
        keywords.add(LookupElementBuilder.create("End If")
                .withTypeText("控制流关键字")
                .withTailText(" - 结束 If 语句"));
        keywords.add(LookupElementBuilder.create("Select Case")
                .withTypeText("控制流关键字")
                .withTailText(" - 选择语句"));
        keywords.add(LookupElementBuilder.create("Case")
                .withTypeText("控制流关键字")
                .withTailText(" - Case 分支"));
        keywords.add(LookupElementBuilder.create("Case Else")
                .withTypeText("控制流关键字")
                .withTailText(" - 默认 Case 分支"));
        keywords.add(LookupElementBuilder.create("End Select")
                .withTypeText("控制流关键字")
                .withTailText(" - 结束 Select 语句"));

        // 循环关键字
        keywords.add(LookupElementBuilder.create("For")
                .withTypeText("循环关键字")
                .withTailText(" - For 循环"));
        keywords.add(LookupElementBuilder.create("To")
                .withTypeText("循环关键字")
                .withTailText(" - For 循环范围"));
        keywords.add(LookupElementBuilder.create("Step")
                .withTypeText("循环关键字")
                .withTailText(" - For 循环步长"));
        keywords.add(LookupElementBuilder.create("Next")
                .withTypeText("循环关键字")
                .withTailText(" - 结束 For 循环"));
        keywords.add(LookupElementBuilder.create("For Each")
                .withTypeText("循环关键字")
                .withTailText(" - For Each 循环"));
        keywords.add(LookupElementBuilder.create("In")
                .withTypeText("循环关键字")
                .withTailText(" - For Each 循环中的 In"));
        keywords.add(LookupElementBuilder.create("While")
                .withTypeText("循环关键字")
                .withTailText(" - While 循环"));
        keywords.add(LookupElementBuilder.create("Wend")
                .withTypeText("循环关键字")
                .withTailText(" - 结束 While 循环"));
        keywords.add(LookupElementBuilder.create("Do")
                .withTypeText("循环关键字")
                .withTailText(" - Do 循环"));
        keywords.add(LookupElementBuilder.create("Loop")
                .withTypeText("循环关键字")
                .withTailText(" - 结束 Do 循环"));
        keywords.add(LookupElementBuilder.create("Until")
                .withTypeText("循环关键字")
                .withTailText(" - Do Until 循环"));
        keywords.add(LookupElementBuilder.create("Exit For")
                .withTypeText("循环关键字")
                .withTailText(" - 退出 For 循环"));
        keywords.add(LookupElementBuilder.create("Exit Do")
                .withTypeText("循环关键字")
                .withTailText(" - 退出 Do 循环"));
        keywords.add(LookupElementBuilder.create("Exit While")
                .withTypeText("循环关键字")
                .withTailText(" - 退出 While 循环"));

        // 函数和子程序关键字
        keywords.add(LookupElementBuilder.create("Function")
                .withTypeText("函数关键字")
                .withTailText(" - 定义函数"));
        keywords.add(LookupElementBuilder.create("Sub")
                .withTypeText("函数关键字")
                .withTailText(" - 定义子程序"));
        keywords.add(LookupElementBuilder.create("End Function")
                .withTypeText("函数关键字")
                .withTailText(" - 结束函数"));
        keywords.add(LookupElementBuilder.create("End Sub")
                .withTypeText("函数关键字")
                .withTailText(" - 结束子程序"));
        keywords.add(LookupElementBuilder.create("Exit Function")
                .withTypeText("函数关键字")
                .withTailText(" - 退出函数"));
        keywords.add(LookupElementBuilder.create("Exit Sub")
                .withTypeText("函数关键字")
                .withTailText(" - 退出子程序"));
        keywords.add(LookupElementBuilder.create("Call")
                .withTypeText("函数关键字")
                .withTailText(" - 调用子程序"));
        keywords.add(LookupElementBuilder.create("ByVal")
                .withTypeText("函数关键字")
                .withTailText(" - 按值传递参数"));
        keywords.add(LookupElementBuilder.create("ByRef")
                .withTypeText("函数关键字")
                .withTailText(" - 按引用传递参数"));
        keywords.add(LookupElementBuilder.create("Optional")
                .withTypeText("函数关键字")
                .withTailText(" - 可选参数"));
        keywords.add(LookupElementBuilder.create("ParamArray")
                .withTypeText("函数关键字")
                .withTailText(" - 参数数组"));

        // 错误处理关键字
        keywords.add(LookupElementBuilder.create("On Error")
                .withTypeText("错误处理关键字")
                .withTailText(" - 错误处理"));
        keywords.add(LookupElementBuilder.create("Resume")
                .withTypeText("错误处理关键字")
                .withTailText(" - 恢复执行"));
        keywords.add(LookupElementBuilder.create("Resume Next")
                .withTypeText("错误处理关键字")
                .withTailText(" - 恢复下一行"));

        // 其他关键字
        keywords.add(LookupElementBuilder.create("Class")
                .withTypeText("类关键字")
                .withTailText(" - 定义类"));
        keywords.add(LookupElementBuilder.create("End Class")
                .withTypeText("类关键字")
                .withTailText(" - 结束类"));
        keywords.add(LookupElementBuilder.create("Property Get")
                .withTypeText("属性关键字")
                .withTailText(" - 获取属性"));
        keywords.add(LookupElementBuilder.create("Property Let")
                .withTypeText("属性关键字")
                .withTailText(" - 设置属性"));
        keywords.add(LookupElementBuilder.create("Property Set")
                .withTypeText("属性关键字")
                .withTailText(" - 设置对象属性"));
        keywords.add(LookupElementBuilder.create("End Property")
                .withTypeText("属性关键字")
                .withTailText(" - 结束属性"));
        keywords.add(LookupElementBuilder.create("Private")
                .withTypeText("访问修饰符")
                .withTailText(" - 私有访问"));
        keywords.add(LookupElementBuilder.create("Public")
                .withTypeText("访问修饰符")
                .withTailText(" - 公共访问"));
        keywords.add(LookupElementBuilder.create("New")
                .withTypeText("对象关键字")
                .withTailText(" - 创建新对象"));
        keywords.add(LookupElementBuilder.create("Nothing")
                .withTypeText("对象关键字")
                .withTailText(" - 空对象引用"));
        keywords.add(LookupElementBuilder.create("Null")
                .withTypeText("值关键字")
                .withTailText(" - 空值"));
        keywords.add(LookupElementBuilder.create("Empty")
                .withTypeText("值关键字")
                .withTailText(" - 空值"));
        keywords.add(LookupElementBuilder.create("True")
                .withTypeText("布尔值")
                .withTailText(" - 真值"));
        keywords.add(LookupElementBuilder.create("False")
                .withTypeText("布尔值")
                .withTailText(" - 假值"));
        keywords.add(LookupElementBuilder.create("And")
                .withTypeText("逻辑运算符")
                .withTailText(" - 逻辑与"));
        keywords.add(LookupElementBuilder.create("Or")
                .withTypeText("逻辑运算符")
                .withTailText(" - 逻辑或"));
        keywords.add(LookupElementBuilder.create("Not")
                .withTypeText("逻辑运算符")
                .withTailText(" - 逻辑非"));
        keywords.add(LookupElementBuilder.create("Xor")
                .withTypeText("逻辑运算符")
                .withTailText(" - 逻辑异或"));
        keywords.add(LookupElementBuilder.create("Eqv")
                .withTypeText("逻辑运算符")
                .withTailText(" - 逻辑等价"));
        keywords.add(LookupElementBuilder.create("Imp")
                .withTypeText("逻辑运算符")
                .withTailText(" - 逻辑蕴含"));
        keywords.add(LookupElementBuilder.create("Mod")
                .withTypeText("算术运算符")
                .withTailText(" - 取模运算"));
        keywords.add(LookupElementBuilder.create("Is")
                .withTypeText("比较运算符")
                .withTailText(" - 对象比较"));
        keywords.add(LookupElementBuilder.create("Like")
                .withTypeText("比较运算符")
                .withTailText(" - 模式匹配"));

        return keywords;
    }

    /**
     * 获取所有提示元素
     */
    @NotNull
    public static List<LookupElement> getAllCompletions() {
        List<LookupElement> completions = new ArrayList<>();

        // 添加 VBScript 关键词
        completions.addAll(getVBScriptKeywords());

        // 添加内置函数
        completions.addAll(getVBScriptFunctions());

        // 添加内置对象
        completions.addAll(getBuiltInObjects());

        // System.out.println("添加了测试关键词");

        return completions;
    }
}