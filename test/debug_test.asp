<%@ Language = "VBScript" %>
<%
' 这是一个调试测试文件
' 用于验证 Classic ASP 代码补全功能

' 测试变量声明
Dim testVar

' 测试对象使用
Response.Write "Hello World"
Response.Write("123")
' 测试函数调用
Len("test")

' 测试关键字
If testVar = "" Then
    Response.Write "Empty"
End If

' 测试数组
Dim arr(5)
%>