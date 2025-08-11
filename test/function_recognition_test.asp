<%@ Language="VBScript" %>
<%
' 函数识别测试
' 验证内置函数显示为黄色，自定义方法显示为蓝色

Option Explicit

' 变量（白色）
Dim userName, userAge, isActive

' 保留词（橘色）
If userName <> "" Then
    isActive = True
Else
    isActive = False
End If

' 系统内置对象（粉红色）
Response.Write "Testing function recognition"
Request.Form("test")
Server.MapPath("/")

' 内置函数（黄色）- 应该正确识别
userName = Trim("  Test User  ")
userAge = CInt("25")
isActive = CBool("True")
length = Len(userName)
upperName = UCase(userName)
lowerName = LCase(userName)
replacedText = Replace(userName, "Test", "New")
leftPart = Left(userName, 4)
rightPart = Right(userName, 4)
midPart = Mid(userName, 2, 3)
instrResult = InStr(userName, "User")
splitArray = Split(userName, " ")
joinedText = Join(splitArray, "-")

' 数学函数（黄色）
absValue = Abs(-10)
intValue = Int(3.7)
fixValue = Fix(3.7)
roundValue = Round(3.7)
randomValue = Rnd()
sqrtValue = Sqr(16)
sinValue = Sin(1.57)
cosValue = Cos(1.57)
tanValue = Tan(1.57)

' 类型转换函数（黄色）
strValue = CStr(123)
doubleValue = CDbl("123.45")
boolValue = CBool("True")
dateValue = CDate("2024-01-01")

' 类型检查函数（黄色）
isEmptyResult = IsEmpty(userName)
isNullResult = IsNull(userName)
isNumericResult = IsNumeric("123")
isObjectResult = IsObject(userName)
isDateResult = IsDate("2024-01-01")

' 日期时间函数（黄色）
currentDate = Date()
currentTime = Time()
currentNow = Now()
dayValue = Day(currentDate)
monthValue = Month(currentDate)
yearValue = Year(currentDate)
hourValue = Hour(currentTime)
minuteValue = Minute(currentTime)
secondValue = Second(currentTime)

' 自定义方法（蓝色）- 应该正确识别
GetUserInfo("test")
ProcessUserData()
ValidateInput()
CalculateTotal()
FormatOutput()
Class_Initialize()
Process_Data()
Get_User_Info()
Validate_Input()

' 函数定义（橘色关键字Function，蓝色方法名）
Function GetUserInfo(name)
    ' 函数体
    GetUserInfo = "User: " & name
End Function

' 子程序定义（橘色关键字Sub，蓝色方法名）
Sub ProcessUserData()
    ' 子程序体
    Response.Write "Processing..."
End Sub

' 异常处理（橘色）
On Error Resume Next
Dim result = 1 / 0
If Err.Number <> 0 Then
    Response.Write "Error: " & Err.Description
    Err.Clear
    Resume Next
End If

%>

<html>
<head>
    <title>函数识别测试</title>
</head>
<body>
    <h1>函数识别颜色测试</h1>
    <p>内置函数：黄色</p>
    <p>自定义方法：蓝色</p>
    <p>变量：白色</p>
    <p>保留词：橘色</p>
    <p>系统内置对象：粉红色</p>

    <%
    ' 测试函数调用
    Call ProcessUserData()
    Dim info = GetUserInfo("TestUser")
    Response.Write info
    %>
</body>
</html>