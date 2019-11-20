<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1> admin </h1>
<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>
<h1><%=rs.size()%></h1>

<%
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    String name = (String)row.get("username");%>
        <a href = "./adminedit?selectedid=<%=row.get("id")%>"> 
              <%= name %>
        </a>
        <br/>

<% } %>

