<%@Language="VBScript"%>
<%
'这是一个测试文件，故意写得很乱
Dim testVar
Dim anotherVar
Response.Write"Hello World"
Request.Form("test")
Len("test")
Mid("test",1,2)
Replace("test","t","T")
If testVar=""Then
Response.Write"Empty"
End If
For i=1 To 10
Response.Write i
Next
Dim arr(5)
arr(0)="test"
%>