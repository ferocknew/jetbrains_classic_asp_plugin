<%@Language = "VBScript"%>
<%
Dim testVar

if testVar = "" then
    Response.Write "Empty"
end if

for i = 1 to 10
    Response.Write i
next

while testVar<>""
Response.Write "Loop"
wend

do
Response.Write "Do Loop"
loop

function TestFunction()
    Response.Write "Function"
end function

sub TestSub()
    Response.Write "Sub"
end sub

select case testVar
case "test"
Response.Write "Test"
end select

property TestProperty
Response.Write "Property"
end property
%>