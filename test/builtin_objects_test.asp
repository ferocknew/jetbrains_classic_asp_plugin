<%@ Language="VBScript" %>
<%
' 系统内置对象测试
' 验证ASP内置对象是否正确显示为粉红色

Option Explicit

' 变量（白色）
Dim userName, userAge, isActive

' 保留词（橘色）
If userName <> "" Then
    isActive = True
Else
    isActive = False
End If

' 系统内置对象（粉红色）
Response.Write "Testing built-in objects"
Response.Flush
Response.Redirect "test.asp"

Request.Form("userName")
Request.QueryString("id")
Request.Cookies("sessionId")

Server.MapPath("/")
Server.CreateObject("ADODB.Connection")
Server.HTMLEncode("<script>alert('test')</script>")
Server.URLEncode("test=value&id=123")

Session("userName") = userName
Session.Timeout = 30
Session.Abandon

Application("siteName") = "My Website"
Application.Lock
Application("visitorCount") = Application("visitorCount") + 1
Application.Unlock

' 自定义方法（蓝色）
GetUserInfo("test")
ProcessUserData()
ValidateInput()

' 内置函数（黄色）
userName = Trim("  Test User  ")
userAge = CInt("25")
isActive = CBool("True")
length = Len(userName)

' 函数定义（橘色关键字Function，蓝色方法名）
Function GetUserInfo(name)
    ' 函数体
    GetUserInfo = "User: " & name
End Function

' 子程序定义（橘色关键字Sub，蓝色方法名）
Sub ProcessUserData()
    ' 子程序体
    Response.Write "Processing..."
End Sub

' 异常处理（橘色）
On Error Resume Next
Dim result = 1 / 0
If Err.Number <> 0 Then
    Response.Write "Error: " & Err.Description
    Err.Clear
    Resume Next
End If

%>

<html>
<head>
    <title>系统内置对象测试</title>
</head>
<body>
    <h1>系统内置对象颜色测试</h1>
    <p>系统内置对象：粉红色</p>
    <p>变量：白色</p>
    <p>保留词：橘色</p>
    <p>自定义方法：蓝色</p>
    <p>内置函数：黄色</p>

    <%
    ' 测试内置对象
    Response.Write "Testing Response object"
    Request.Form("test")
    Server.MapPath("/")
    Session("test") = "value"
    Application("test") = "value"
    %>
</body>
</html>