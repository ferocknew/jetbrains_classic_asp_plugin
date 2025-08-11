<%@Language = "VBScript"%>
<%
Dim testVar
Response.Write "Hello"
if testVar = "" then
    Response.Write "Empty"
    end if
while testVar<>""
Response.Write "Loop"
wend
do
Response.Write "Do Loop"
loop
select case testVar
case "test"
Response.Write "Test"
end select
property TestProperty
Response.Write "Property"
end property
%>