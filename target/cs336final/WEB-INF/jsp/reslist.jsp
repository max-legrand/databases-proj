<%-- Peter Marchese --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<% 
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>

<html>
   <head>
      <title>Reservation List</title>
   </head>
   <body>
   <form action="./reslist" method="post">
   <input type = "number" name="flightNumber">
   <br/>
   <br/>
   <input type = "text" name="name">
   <br/><br/>
   <input type = "submit" name = "submit">
   </form>
      
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>iD</th>
    <th>cid</th>
    <th>username</th>
    <th>flightnum</th>
    <th>number_first_class</th>
    <th>num_economy</th>
    <th>date_made</th>
    </tr>
    
    <%
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            <td><%= row.get("id") %></td>
            <td><%= row.get("cid") %></td>
            <td><%= row.get("username") %></td>
            <td><%= row.get("flightnum") %></td>
            <td><%= row.get("num_first_class") %></td>
            <td><%= row.get("num_economy") %></td>
            <td><%= row.get("date_made") %></td>
            </tr>
    
    <% } } %>
    
    </table>
   </body>
   </html>
   