<%@ Language="VBScript" %>
<%
' 这是一个注释
Dim userName, userAge
userName = "张三"
userAge = 25

' 使用内置对象
Response.Write "Hello, " & userName
Request.Form("username")
        Request.QueryString("id")
        
123
' 创建对象
Set objConn = Server.CreateObject("ADODB.Connection")

' 使用 Session 和 Application
Session("user") = userName
Application("visitorCount") = Application("visitorCount") + 1

' 数组操作
Dim arrColors(2)
arrColors(0) = "Red"
arrColors(1) = "Green"
arrColors(2) = "Blue"

' 循环结构
For Each color In arrColors
    Response.Write color & "<br>"
Next

' 条件语句
If userAge > 18 Then
    Response.Write "成年人"
Else
    Response.Write "未成年人"
End If

' While 循环
Dim i = 1
While i <= 5
    Response.Write "计数: " & i & "<br>"
    i = i + 1
Wend

' Do Loop 循环
Dim j = 1
Do
    Response.Write "Do Loop: " & j & "<br>"
    j = j + 1
Loop Until j > 3
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Classic ASP 混合语法测试</title>
    <style type="text/css">
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .highlight {
            color: red;
            font-weight: bold;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Classic ASP 混合语法测试</h1>

        <h2>用户信息</h2>
        <p><strong>用户名:</strong> <%= userName %></p>
        <p><strong>年龄:</strong> <%= userAge %></p>

        <%
        ' 在HTML中嵌入ASP代码
        Dim colors = Array("红色", "绿色", "蓝色", "黄色", "紫色")
        %>

        <h2>颜色列表</h2>
        <ul>
        <%
        For Each color In colors
            Response.Write "<li>" & color & "</li>"
        Next
        %>
        </ul>

        <h2>用户数据表格</h2>
        <table>
            <thead>
                <tr>
                    <th>字段</th>
                    <th>值</th>
                    <th>类型</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>用户名</td>
                    <td><%= userName %></td>
                    <td>字符串</td>
                </tr>
                <tr>
                    <td>年龄</td>
                    <td><%= userAge %></td>
                    <td>数字</td>
                </tr>
                <tr>
                    <td>Session用户</td>
                    <td><%= Session("user") %></td>
                    <td>Session变量</td>
                </tr>
            </tbody>
        </table>

        <div class="highlight">
            <h3>高亮区域</h3>
            <p>这是一个高亮的段落，用于测试HTML和ASP的混合语法高亮功能。</p>
            <%
            ' 在高亮区域中的ASP代码
            Response.Write "<p>当前时间: " & Now() & "</p>"
            %>
        </div>

        <%
        ' 更多的ASP逻辑
        Dim counter = 0
        For counter = 1 To 3
            Response.Write "<p>循环 " & counter & ": 这是第" & counter & "次输出</p>"
        Next
        %>

        <footer>
            <p><em>这是一个Classic ASP和HTML混合的测试页面</em></p>
        </footer>
    </div>
</body>
</html>