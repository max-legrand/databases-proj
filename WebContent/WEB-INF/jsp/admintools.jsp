<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1> Admin Portal </h1>
<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>

<style>
table, th, td {
  border: 1px solid black;
}
</style>

<a href="./adminadd">Add user</a>
<br/>
<br/>
<table style="width: 25%; ">

<tr>
<th>username</th>
<th>type</th>
<th>edit</th>
<th>delete</th>
</tr>

<%

for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    String name = (String)row.get("username");
    String type = (String)row.get("type");
    %>
        <tr style='font-size: large;'>
        <td><%= name %></td>
        <td><%= type %></td>
        <td>
        <a href = "./adminedit?selectedid=<%=row.get("id")%>">edit</a>
        </td>
        <td>
        <a href = "./admindelete?deleteid=<%=row.get("id")%>">delete</a>
        </td>
        </tr>

<% } %>
</table>



