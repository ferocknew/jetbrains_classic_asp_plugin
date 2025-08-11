<%@Language="VBScript" %>
<%
' 全面的字符串高亮测试
Dim simpleString, complexString, htmlString
simpleString = "简单字符串"
complexString = "包含 '单引号' 和 ""双引号"" 的字符串"
htmlString = "<div class='test'>HTML内容</div>"

' 测试不同类型的字符串赋值
Response.Write "Hello World"
Response.Write simpleString
Response.Write complexString
Response.Write htmlString

' 测试字符串连接
Dim firstName, lastName
firstName = "张"
lastName = "三"
Response.Write "姓名: " & firstName & " " & lastName

' 测试HTML中的字符串
%>
<html>
<head>
    <title>字符串高亮全面测试</title>
    <meta name="description" content="这是一个测试页面">
</head>
<body>
    <h1>字符串高亮测试</h1>
    <p>这是一个段落，包含 "引号内的文本" 和 '单引号文本'</p>
    <div class="test-class" data-value="test-data">
        <span>嵌套的HTML内容</span>
    </div>
    <script>
        // JavaScript中的字符串也应该高亮
        var jsString = "JavaScript字符串";
        console.log(jsString);
    </script>
</body>
</html>
