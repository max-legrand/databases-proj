<%-- MAX LEGRAND --%>
<%-- edit airport FORM --%>


<%@ page import="java.io.*,java.util.*,java.sql.*"%>


<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
%>
<h1>Edit Airport</h1>

<form action = "./airportseditconf" method = "POST">
<input type="text" name="id" placeholder="id" value="<%=row.get("id")%>">
<input type="hidden" name="previd" value="<%=row.get("id")%>">
<br/>
<br/>
<input type="submit">
</form>