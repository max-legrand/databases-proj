<%-- MAX LEGRAND --%>
<%-- ADD AIRCRAFT FORM --%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1>Add an aircraft</h1>
<%
ArrayList airline = (ArrayList)request.getAttribute("airline");
%>
<form action = "./addaircraftconf" method = "POST">
<input type="text" name="id" placeholder="id">
<br/>
<br/>
<h3>Airline:</h3>
<select name="airline">
<%

for(int i = 0; i < airline.size(); i++) { 
    Dictionary row = (Hashtable)airline.get(i);
    %>
        <option value="<%=row.get("id")%>"><%=row.get("id")%></option>

<% } %>


</select>
<input type="submit">
</form>