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
   <form action="./salesreport" method="post">
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
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            <td><%= row.get("id") %></td>
            <%
            // int value = Integer.parseInt(row.get("num_first_class"))*Integer.parseInt(row.get("fare_first")) +  Integer.parseInt(row.get("num_economy"))*Integer.parseInt(row.get("fare_econ"));
            int numfirst = Integer.valueOf((String) row.get("num_first_class"));
            int farefirst = Integer.valueOf((String) row.get("fare_first"));
            int numecon = Integer.valueOf((String) row.get("num_economy"));
            int fareecon = Integer.valueOf((String) row.get("fare_econ"));
            int value = numfirst*farefirst + numecon*fareecon;
            %>
            <td><%=value %></td>
            <td>
            </td>
            </tr>
    
    <% } } %>
    
    </table>
   </body>
   </html>
   