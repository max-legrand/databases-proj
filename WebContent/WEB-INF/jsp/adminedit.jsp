<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1>adminedit</h1>

<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
%>

<h1><%=row.get("username")%></h1>
<h1><%=row.get("password")%></h1>