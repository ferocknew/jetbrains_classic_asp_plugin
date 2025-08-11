<%@Language = "VBScript"%>
<%
' 测试新增关键词和运算符

Class TestClass
    Public Name
    Private Age

    Public Function GetInfo()
        GetInfo = Name & ":" & Age
    End Function

    Private Sub SetAge(value)
        Age = value
    End Sub
End Class

Server.CreateObject("ADODB.Connection")
%>