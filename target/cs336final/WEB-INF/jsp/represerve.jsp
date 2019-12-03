<%-- MAX LEGRAND --%>
<%-- Reservation tools --%>


<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1>Reservations</h1>
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

<a href="./addres">Add Reservation</a>
<br/>
<br/>
<table style="width: 50%; ">

<tr>
<th>id</th>
<th>customer id</th>
<th>flight num</th>
<th># first class</th>
<th># economy</th>
<th>edit</th>
<th>delete</th>
</tr>

<%

for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    %>
        <tr style='font-size: large;'>
        <td><%= row.get("id") %></td>
        <td><%= row.get("cid") %></td>
        <td><%= row.get("flightnum") %></td>
        <td><%= row.get("num_first_class") %></td>
        <td><%= row.get("num_economy") %></td>
        <td>
        <a href = "./resedit?selectedid=<%=row.get("id")%>">edit</a>
        </td>
        <td>
        <a href = "./resdel?deleteid=<%=row.get("id")%>">delete</a>
        </td>
        </tr>

<% } %>
</table>
