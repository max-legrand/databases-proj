<%-- Peter Marchese --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<% 
ArrayList rs = (ArrayList)request.getAttribute("rs");
%>

<html>
   <head>
      <title>Customer that generated most revenue</title>
   </head>
   <body>
   <table style="width: 65%; ">
    <tr>
    <tr>
    <th>Customer</th>
    <th>revenue</th>
    </tr>
    
    <%
    if (rs != null){
       String maxName = "";
       int maxRev = 0;
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        %>
            <tr style='font-size: large;'>
            int numfirst = Integer.valueOf((String) row.get("num_first_class"));
            int farefirst = Integer.valueOf((String) row.get("fare_first"));
            int numecon = Integer.valueOf((String) row.get("num_economy"));
            int fareecon = Integer.valueOf((String) row.get("fare_econ"));
            int total = numfirst*farefirst + numecon*fareecon;
            if(total > maxRev){
               maxName = row.get("username");
               maxRev = total;
            }
            </tr>
    
    <% } <td><%= maxName %></td>
    <td><%= maxRev %></td>} %>
    
    </table>
   </body>
   </html>
   