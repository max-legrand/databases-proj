<%-- MAX LEGRAND --%>
<%-- waitinglist --%>


<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1>Waiting List</h1>
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
<h3>Flights</h3>
<table style="width: 65%; ">
<tr>
<tr>
<th>number</th>
<th>type</th>
<th>depart time</th>
<th>arrive time</th>
<th>aircraft</th>
<th>airline</th>
<th>To</th>
<th>From</th>
<th>view waiting list</th>
</tr>

<%

for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    %>
        <tr style='font-size: large;'>
        <td><%= row.get("number") %></td>
        <td><%= row.get("type") %></td>
        <td><%= row.get("depart_time") %></td>
        <td><%= row.get("arrive_time") %></td>
        <td><%= row.get("aircraft") %></td>
        <td><%= row.get("airline") %></td>
        <td><%= row.get("airport_to") %></td>
        <td><%= row.get("airport_from") %></td>
        <td>
        <a href = "./waitinglist?number=<%= row.get("number") %>">view waiting list</a>
        </td>
        </tr>

<% } %>
</table>
