<%-- Peter Marchese --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<% 
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>

<html>
   <head>
      <title>Sales Report</title>
   </head>
   <body>
   <form action="./salesreport.jsp" method="post">
   <input type = "number" name="month", min=1, max=12, value=1>
   <br/>
   <br/>
   <input type = "number" name="year", min=2018, max=3000, value=2019>
   <br/><br/>
   <input type = "submit" name = "submit">
   </form>
      
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>reservationID</th>
    <th>totalSales</th>
    </tr>
    
    <%
    
    for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            <td><%= row.get("id") %></td>
            <td><%= (int)row.get("num_first_class")*(int)row.get("fare_first") +  (int)row.get("num_economy")*(int)row.get("fare_econ")%></td>
            <td>
            </td>
            </tr>
    
    <% } %>
    </table>
   </body>
   </html>
   