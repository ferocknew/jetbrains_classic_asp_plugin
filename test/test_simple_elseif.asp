<%@Language = "VBScript"%>
<%
Sub TestElseIf(value)
    If value = 0 Then
        Response.Write "Zero"
    ElseIf value = 1 Then
        Response.Write "One"
    Else
        Response.Write "Other"
    End If
End Sub
%>