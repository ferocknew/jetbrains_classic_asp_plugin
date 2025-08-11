<%@ Language="VBScript" %>
<%
' 更新后的Java风格配色方案测试
' 保留词用橘色，变量用白色，function用蓝色，内置函数用黄色

Option Explicit

' 变量声明（白色）
Dim userName, userAge, isActive
Dim response, request, server
Dim EasyASP, MyClass ' 自定义类名也是白色

' 保留词（橘色）
If userName <> "" Then
    isActive = True
    Else
    isActive = False
End If

' 循环语句（橘色）
For i = 1 To 10

    If i Mod 2 = 0 Then
                        ' 内置函数（黄色）
        userName = Trim("  Test User  ")
        userAge = CInt("25")
        isActive = CBool("True")
    End If
Next

' 函数定义（蓝色关键字）

Function GetUserInfo(name)
            ' 内置函数（黄色）
    Dim lenName
    lenName = Len(name)

            ' 字符串处理函数（黄色）
    Dim upperName, lowerName
    upperName = UCase(name)
    lowerName = LCase(name)

            ' 数学函数（黄色）
    Dim randomNum
    randomNum = Rnd()

            ' 日期函数（黄色）
    Dim currentDate, currentTime
    currentDate = Date()
    currentTime = Time()

    GetUserInfo = "User: " & name & ", Length: " & lenName
End Function

' 子程序定义（橘色关键字Sub，方法名需要进一步区分）

Sub ProcessUserData()
            ' 内置对象方法（黄色）
    Response.Write "Processing user data..."
    Response.Flush

            ' 请求对象（黄色）
    Dim formData, queryData
    formData = Request.Form("userName")
    queryData = Request.QueryString("id")

            ' 服务器对象（黄色）
    Dim serverPath
    serverPath = Server.MapPath("/")

            ' 会话对象（黄色）
    Session("userName") = userName
    Session.Timeout = 30
End Sub

' 类定义（橘色关键字Class，类名是白色）
Class EasyASP
    ' 公共成员（橘色关键字Public）
    Public Lang, Error, Str, Var, Console, Date, Db, Encrypt, Json, List, Fso, Http, Tpl, Upload, Cache

    ' 私有成员（橘色关键字Private）
    Private version, s_basePath, s_pluginPath, s_cores, s_defaultPageName, s_charset, b_debug, i_timer, i_newId

   ' 构造函数（橘色关键字Private Sub，方法名需要进一步区分）
    Private Sub Class_Initialize()
        version = "3.1.2"
        s_basePath = "/easyasp/"
        s_pluginPath = s_basePath & "plugin/"
        b_debug = False
        s_defaultPageName = "index.asp"
        s_charset = "UTF-8"
        Response.Charset = s_charset
        Session.CodePage = 65001
    End Sub

End Class

' 选择语句（橘色）
Select Case userAge
    Case 18 To 25
    Response.Write "Young adult"
    Case 26 To 40
    Response.Write "Adult"
    Case Else
    Response.Write "Senior"
End Select

' 错误处理（橘色）
On Error Resume Next
Dim result
result = 10 / 0

If Err.Number <> 0 Then
    Response.Write "Error: " & Err.Description
    Err.Clear
End If

' 数组操作（橘色）
Dim userArray
userArray = Array("John", "Jane", "Bob")

' 遍历数组（橘色）
For Each user In userArray
    Response.Write "User: " & user & "<br>"
Next

' 字符串函数（黄色）
Dim testString, resultString
testString = "Hello World"
resultString = Mid(testString, 1, 5)
resultString = Left(testString, 3)
resultString = Right(testString, 5)
resultString = Replace(testString, "World", "ASP")

' 类型检查函数（黄色）
If IsEmpty(userName) Then
    Response.Write "Username is empty"
End If

If IsNumeric(userAge) Then
    Response.Write "Age is numeric"
End If

If IsDate("2024-01-01") Then
    Response.Write "Valid date"
End If

' 数学函数（黄色）
Dim absValue, roundValue, sqrtValue
absValue = Abs(-10)
roundValue = Round(3.14159, 2)
sqrtValue = Sqr(16)

' 转换函数（黄色）
Dim hexValue, octValue, ascValue, chrValue
hexValue = Hex(255)
octValue = Oct(64)
ascValue = Asc("A")
chrValue = Chr(65)

' 颜色函数（黄色）
Dim colorValue
colorValue = RGB(255, 0, 0)

%>

<html>
<head>
    <title>更新后的Java风格Classic ASP语法高亮测试</title>
</head>
<body>
    <h1>更新后的配色方案测试</h1>
    <p>保留词：橘色</p>
    <p>变量：白色</p>
    <p>函数定义：蓝色</p>
    <p>内置函数：黄色</p>

    <%
    ' 调用函数
Dim info
info = GetUserInfo("TestUser")
Response.Write info

    ' 调用子程序
Call ProcessUserData()
    %>
</body>
</html>
