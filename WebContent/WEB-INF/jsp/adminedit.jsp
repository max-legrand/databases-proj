<%@ page import="java.io.*,java.util.*,java.sql.*"%>


<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
%>
<h1>Edit User</h1>

<h1><%=row.get("username")%></h1>
<h1><%=row.get("password")%></h1>