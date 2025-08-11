<%@Language="VBScript"%>
<html>
<head>
    <title>格式化测试</title>
</head>
<body>
    <h1>格式化测试</h1>
    <p>HTML区域不应该被格式化</p>

    <%
    ' ASP区域应该被格式化
    Dim userName, userAge
    userName = "TestUser"
    userAge = 25

    If userName <> "" Then
        Response.Write "用户名: " & userName
    End If

    For i = 1 To 5
        Response.Write "循环: " & i & "<br>"
    Next

    Function GetUserInfo(name)
        GetUserInfo = "用户: " & name
    End Function

    Sub ProcessUserData()
        Response.Write "处理用户数据"
    End Sub
    %>

    <p>HTML区域结束</p>
</body>
</html>