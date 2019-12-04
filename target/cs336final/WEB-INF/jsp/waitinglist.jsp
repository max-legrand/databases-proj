<%-- MAX LEGRAND --%>
<%-- waitinglist for flight --%>


<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
//Dictionary row = (Hashtable)rs.get(0))
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>

<style>
table, th, td {
  border: 1px solid black;
}
</style>
<h1>Waiting List for Flight <%=request.getAttribute("num")%></h1>
<br>
<table style="width: 50%; ">
<tr>
<th>user id</th>
<th>username</th>
</tr>

<%

for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    %>
        <tr style='font-size: large;'>
        <td><%= row.get("cid") %></td>
        <td><%= row.get("username") %></td>
        </tr>

<% } %>
</table>
