<%@ Language="VBScript" %>
<%
' 测试字符串高亮功能
Dim testString, anotherString
testString = "这是一个测试字符串"
anotherString = "VBScript"

' 测试不同类型的字符串
Response.Write "Hello World"
Response.Write testString
Response.Write "Language: " & anotherString

' 测试HTML中的字符串
%>
<html>
<head>
    <title>字符串高亮测试</title>
</head>
<body>
    <h1>字符串高亮测试页面</h1>
    <p>这是一个段落，包含 "引号内的文本"</p>
    <div class="test-class">内容</div>
</body>
</html>
