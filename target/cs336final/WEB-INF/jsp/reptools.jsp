<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1>Rep Portal</h1>
<%
ArrayList rs = (ArrayList)request.getAttribute("aircraftrs");
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>

<style>
table, th, td {
  border: 1px solid black;
}
</style>

<a href="./addaircraft">Add aircraft</a>
<br/>
<br/>
<table style="width: 25%; ">

<tr>
<th>id</th>
<th>edit</th>
<th>delete</th>
</tr>

<%

for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    %>
        <tr style='font-size: large;'>
        <td><%= row.get("id") %></td>
        <td>
        <a href = "./aircraftedit?selectedid=<%=row.get("id")%>">edit</a>
        </td>
        <td>
        <a href = "./aircraftdel?deleteid=<%=row.get("id")%>">delete</a>
        </td>
        </tr>

<% } %>
</table>

<br/>
<%
rs = (ArrayList)request.getAttribute("airportsrs");
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>

<style>
table, th, td {
  border: 1px solid black;
}
</style>

<a href="./airportsadd">Add airport</a>
<br/>
<br/>
<table style="width: 25%; ">

<tr>
<th>id</th>
<th>edit</th>
<th>delete</th>
</tr>

<%

for(int i = 0; i < rs.size(); i++) { 
    Dictionary row = (Hashtable)rs.get(i);
    %>
        <tr style='font-size: large;'>
        <td><%= row.get("id") %></td>
        <td>
        <a href = "./airportsedit?selectedid=<%=row.get("id")%>">edit</a>
        </td>
        <td>
        <a href = "./airportsdel?deleteid=<%=row.get("id")%>">delete</a>
        </td>
        </tr>

<% } %>
</table>

<br/>
<%
rs = (ArrayList)request.getAttribute("flightsrs");
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>

<style>
table, th, td {
  border: 1px solid black;
}
</style>

<a href="./flightsadd">Add flight</a>
<br/>
<br/>
<table style="width: 50%; ">

<tr>
<th>number</th>
<th>type</th>
<th>depart time</th>
<th>arrive time</th>
<th>1st class fare</th>
<th>economy fare</th>
<th>edit</th>
<th>delete</th>
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
        <td><%= row.get("fare_first") %></td>
        <td><%= row.get("fare_econ") %></td>
        <td>
        <a href = "./airportsedit?selectedid=<%=row.get("id")%>">edit</a>
        </td>
        <td>
        <a href = "./airportsdel?deleteid=<%=row.get("id")%>">delete</a>
        </td>
        </tr>

<% } %>
</table>







