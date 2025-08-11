<%
' 测试内置函数识别
Dim testVar = "Hello World"
Dim result

' 字符串函数
result = Mid(testVar, 1, 5)
result = Len(testVar)
result = Left(testVar, 5)
result = Right(testVar, 5)
result = UCase(testVar)
result = LCase(testVar)
result = Trim("  test  ")

' 数学函数
result = Abs(-10)
result = Int(3.7)
result = Round(3.14159, 2)

' 日期函数
result = Date()
result = Time()
result = Now()

' 转换函数
result = Hex(255)
result = Oct(64)
result = Asc("A")
result = Chr(65)

' 类型函数
result = TypeName(testVar)
result = VarType(testVar)

' 字符串函数
result = Replace(testVar, "World", "ASP")
result = Split(testVar, " ")
result = Join(Split(testVar, " "), "-")
result = InStr(testVar, "World")

' 类型转换
result = CStr(123)
result = CInt("456")
result = CDbl("789.123")
result = CBool(1)
result = CDate("2024-01-01")

' 判断函数
result = IsEmpty(Empty)
result = IsNothing(Nothing)
result = IsNull(Null)
result = IsNumeric("123")
result = IsObject(Response)
result = IsDate("2024-01-01")
%>