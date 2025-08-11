<%@ Language="VBScript" %>
<%
' 这是一个测试文件，用于验证 Classic ASP 函数参数提示功能
' 在以下函数调用中输入 ( 应该会显示参数提示

' Response.Write 函数测试
Response.Write("Hello World")

' Len 函数测试
Dim strLength
strLength = Len("测试字符串")

' Mid 函数测试
Dim subStr

subStr = Mid("Hello World", 1, 5)

' Replace 函数测试
Dim newStr
newStr = Replace("Hello World", "World", "ASP")

' InStr 函数测试
Dim pos
pos = InStr("Hello World", "World")

' DateAdd 函数测试
Dim futureDate
futureDate = DateAdd("d", 7, Now())

' DateDiff 函数测试
Dim daysDiff
daysDiff = DateDiff("d", Now(), DateAdd("d", 30, Now()))

' Array 函数测试
Dim colors
colors = Array("Red", "Green", "Blue")

' Split 函数测试
Dim parts
parts = Split("apple,banana,orange", ",")

' Join 函数测试
Dim joined
joined = Join(Array("Hello", "World"), " ")

' UCase 和 LCase 函数测试
Dim upperStr, lowerStr
upperStr = UCase("hello world")
lowerStr = LCase("HELLO WORLD")

' Trim 函数测试
Dim trimmed
trimmed = Trim(" Hello World ")

' 类型转换函数测试
Dim numStr, numInt, numDate
numStr = CStr(123)
numInt = CInt("456")
numDate = CDate("2023-12-25")

' 判断函数测试
Dim isNullTest, isEmptyTest, isNumericTest
isNullTest = IsNull(Null)
isEmptyTest = IsEmpty(Empty)
isNumericTest = IsNumeric("123")

' Server.CreateObject 测试
Dim objConn
Set objConn = Server.CreateObject("ADODB.Connection")

' Request.Form 测试
Dim formValue
formValue = Request.Form("username")
%>
