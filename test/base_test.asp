<%@ Language = "VBScript" %>
<%
' 测试内置函数高亮
Dim result
result = Len("test")
result = Left("test", 2)
result = Right("test", 2)
result = Mid("test", 1, 2)

' 测试对象方法高亮
Response.Write "Hello"
Response.ContentType = "text/html"
Response.CharSet = "utf-8"

Server.MapPath("../test")
Server.CreateObject("ADODB.Connection")

' 测试关键词高亮
If result = "" Then
    Response.Write "Empty"
End If

' 测试错误处理
On Error Resume Next
If Err.Number <> 0 Then
    Response.Write "Error: " & Err.Description
End If
%>