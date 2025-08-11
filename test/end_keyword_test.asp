<%
Option Explicit
Dim testVar
testVar = "Hello"

If testVar <> "" Then
    Response.Write testVar
End If

For i = 1 To 5
    Response.Write i
Next

Function TestFunction()
    Response.Write "Test"
End Function

Sub TestSub()
    Response.Write "Test"
End Sub
%>