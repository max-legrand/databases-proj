<%-- Peter Marchese --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<% 
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>

<html>
   <head>
      <title>Most Active Flights</title>
   </head>
   <body>
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>Flight</th>
    <th>Tickets Sold</th>
    </tr>
    
    <%
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            <td><%= row.get("Flight") %></td>
            <%-- Fix this --%>
            <td><%= row.get("revenue") %></td>
            </tr>
    
    <% } } %>
    
    </table>
   </body>
   </html>
   