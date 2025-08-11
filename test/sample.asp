<%@ Language="VBScript" %>
<%
' 这是一个示例 Classic ASP 文件
Dim userName, userAge
userName = Request.Form("name")
userAge = Request.Form("age")

If userName <> "" Then
    Response.Write "Hello, " & userName & "!"
    Response.Write "<br>"
    Response.Write "Your age is: " & userAge
    Response.Write
    D
Else
    Response.Write "Please enter your name"
End If

' 定义一个函数
Function GetGreeting(name)
    GetGreeting = "Hello, " & name & "!"
End Function

' 使用函数
Dim greeting
greeting = GetGreeting("World")
Response.Write greeting
%>

<html>

<head>
    <title>Classic ASP Test</title>
</head>
<body>
    <h1>Classic ASP Plugin Test</h1>
    <form method="post">
        <label>Name: <input type="text" name="name"></label><br>
        <label>Age: <input type="text" name="age"></label><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>