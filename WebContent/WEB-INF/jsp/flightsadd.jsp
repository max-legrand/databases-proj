<%-- MAX LEGRAND --%>
<%-- form to add flight --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<h1>Add an flight</h1>
<%
ArrayList aircrafts = (ArrayList)request.getAttribute("aircrafts");
ArrayList airports = (ArrayList)request.getAttribute("airports");
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>
<form action = "./flightsaddconf" method = "POST">
<input type="text" name="number" placeholder="number">
<br/>
<br/>
<select name="type">
<option>One-Way</option>
<option>Round-Trip</option>
<option>Flexible Date/time</option>
</select>
<br/>
<br/>
<h3>depart time</h3>
<input type="datetime-local" name="depart">
<br/>
<br/>
<h3>arrive time</h3>
<input type="datetime-local" name="arrive">
<br/>
<br/>
<h3>aircraft</h3>
<select name="aircraft">
<%

for(int i = 0; i < aircrafts.size(); i++) { 
    Dictionary row = (Hashtable)aircrafts.get(i);
    %>
        <option value="<%=row.get("id")%>"><%=row.get("id")%> - <%=row.get("airline") %></option>

<% } %>


</select>

<h3>airport to:</h3>
<select name="airport_to">
<%

for(int i = 0; i < airports.size(); i++) { 
    Dictionary row = (Hashtable)airports.get(i);
    %>
        <option value="<%=row.get("id")%>"><%=row.get("id")%></option>

<% } %>

</select>

<h3>airport from:</h3>
<select name="airport_from">
<%

for(int i = 0; i < airports.size(); i++) { 
    Dictionary row = (Hashtable)airports.get(i);
    %>
        <option value="<%=row.get("id")%>"><%=row.get("id")%></option>

<% } %>

</select>

<h3>fare: first class</h3>
<input type="number" name="farefirst" placeholder=0>
<br/>
<br/>
<h3>fare: economy class</h3>
<input type="number" name="fareecon" placeholder=0>
<br/>
<br/>
<input type="submit">
</form>