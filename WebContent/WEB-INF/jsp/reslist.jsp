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
   <input type = "number" name="flight number">
   <br/>
   <br/>
   <input type = "text" name="name">
   <br/><br/>
   <input type = "submit" name = "submit">
   </form>
      
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>reservationID</th>
    </tr>
    
    <%
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            <td><%= row.get("id") %></td>
            </tr>
    
    <% } } %>
    
    </table>
   </body>
   </html>
   