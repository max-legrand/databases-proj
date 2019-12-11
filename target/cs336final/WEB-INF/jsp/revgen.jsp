<%-- Peter Marchese --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<% 
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>

<html>
   <head>
      <title>Revenue Generated</title>
   </head>
   <body>
   <form action="./revgen" method="post">
   <input type = "text" name="flight">
   <br/>
   <br/>
   <input type = "text" name="airline">
   <br/><br/>
   <input type = "text" name = customer>
   <br/><br/>
   <input type = "submit" name = "submit">
   </form>
      
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>Source</th>
    <th>totalSales</th>
    </tr>
    
    <%
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            <%-- Gotta fix this --%>
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