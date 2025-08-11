<%
Option Explicit
Dim testVar, result

testVar = "Hello World"
result = Mid(testVar, 1, 5)

Response.Write result
Response.Write Mid("Test String", 1, 4)
%>