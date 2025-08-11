<%
Option Explicit
Dim testVar, result, i
testVar = "Hello World"

' 测试基本语法
If testVar <> "" Then
    Response.Write testVar
    result = Mid(testVar, 1, 5)

    ' 测试循环
    For i = 1 To 10
        If i > 5 Then
            Response.Write i
        End If
    Next

    ' 测试其他内置函数
    Dim lenResult = Len(testVar)
    Dim leftResult = Left(testVar, 5)
    Dim rightResult = Right(testVar, 5)
    Dim upperResult = UCase(testVar)
    Dim lowerResult = LCase(testVar)
    Dim trimResult = Trim("  test  ")

    ' 测试对象方法
    Server.ScriptTimeOut = 30
    Response.Buffer = True
    Response.Charset = "UTF-8"
    Response.CodePage = 65001

    ' 测试数学函数
    Dim absResult = Abs(-10)
    Dim intResult = Int(3.7)
    Dim roundResult = Round(3.14159, 2)

    ' 测试日期函数
    Dim currentDate = Date()
    Dim currentTime = Time()
    Dim currentNow = Now()

    ' 测试转换函数
    Dim hexResult = Hex(255)
    Dim octResult = Oct(64)
    Dim ascResult = Asc("A")
    Dim chrResult = Chr(65)

    ' 测试类型函数
    Dim typeResult = TypeName(testVar)
    Dim varTypeResult = VarType(testVar)

    ' 测试字符串函数
    Dim replaceResult = Replace(testVar, "World", "ASP")
    Dim splitResult = Split(testVar, " ")
    Dim joinResult = Join(splitResult, "-")
    Dim instrResult = InStr(testVar, "World")

    ' 测试类型转换
    Dim cstrResult = CStr(123)
    Dim cintResult = CInt("456")
    Dim cdblResult = CDbl("789.123")
    Dim cboolResult = CBool(1)
    Dim cdateResult = CDate("2024-01-01")

    ' 测试特殊值
    Dim emptyVar = Empty
    Dim nothingVar = Nothing
    Dim nullVar = Null
    Dim trueVar = True
    Dim falseVar = False

    ' 测试判断函数
    Dim isEmptyResult = IsEmpty(emptyVar)
    Dim isNothingResult = IsNothing(nothingVar)
    Dim isNullResult = IsNull(nullVar)
    Dim isNumericResult = IsNumeric("123")
    Dim isObjectResult = IsObject(Response)
    Dim isDateResult = IsDate("2024-01-01")

    ' 测试对话框函数
    ' MsgBox "Hello World"
    ' Dim inputResult = InputBox("Enter your name:")

    ' 测试数组
    Dim myArray(5)
    For i = 0 To 5
        myArray(i) = i * 2
    Next

    ' 测试For Each循环
    Dim item
    For Each item In myArray
        Response.Write item & "<br>"
    Next

    ' 测试While循环
    Dim whileCounter = 0
    While whileCounter < 5
        Response.Write "While loop: " & whileCounter & "<br>"
        whileCounter = whileCounter + 1
    Wend

    ' 测试Do循环
    Dim doCounter = 0
    Do
        Response.Write "Do loop: " & doCounter & "<br>"
        doCounter = doCounter + 1
    Loop Until doCounter >= 3

    ' 测试Select Case
    Dim caseVar = 2
    Select Case caseVar
        Case 1
            Response.Write "Case 1"
        Case 2
            Response.Write "Case 2"
        Case Else
            Response.Write "Default case"
    End Select

    ' 测试函数定义
    Function AddNumbers(a, b)
        AddNumbers = a + b
    End Function

    ' 测试子程序
    Sub DisplayMessage(msg)
        Response.Write msg
    End Sub

    ' 测试类定义
    Class TestClass
        Private m_name

        Public Property Get Name()
            Name = m_name
        End Property

        Public Property Let Name(value)
            m_name = value
        End Property

        Public Sub Initialize(name)
            m_name = name
        End Sub
    End Class

    ' 测试日期字面量
    Dim dateLiteral = #2024-01-01#
    Dim timeLiteral = #12:30:45#
    Dim dateTimeLiteral = #2024-01-01 12:30:45#

End If
%>