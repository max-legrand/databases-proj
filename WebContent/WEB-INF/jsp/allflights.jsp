<%-- Peter Marchese --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<% 
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>

<html>
   <head>
      <title>All Flights at Given Airport</title>
   </head>
   <body>
   <form action="./allflights" method="post">
   <input type = "text" name="airport">
   <br/>
   <br/>
   <input type = "submit" name = "submit">
   </form>
      
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>Flights</th>
    </tr>
    
    <%
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
                <%-- Gotta Fix --%>
            <td><%= row.get("Flights") %></td>
            <td>
            </td>
            </tr>
    
    <% } } %>
    
    </table>
   </body>
   </html>
   