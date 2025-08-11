<%@Language = "VBScript"%>
<%

Sub FixDate()
    Dim myDate
    myDate = #2/13/95#

    If myDate < Now Then myDate = Now
End Sub



Sub ReportValue(value)

    If value = 0 Then
        MsgBox value
        ElseIf value = 1 Then
        MsgBox value
        ElseIf value = 2 Then
        Msgbox value
        Else
        ""

    End If
End Sub
%>
