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
    HashMap<String, Integer> map = new HashMap<>();
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        if(map.containsKey("rows.get("fligthnum")){
            map.put(rows.get("flightnum"), Integer.valueOf((String) map.get("rows.get("flightnum"))+1 );
        } else {
            map.put(rows.get("flightnum"), 1);
        }
      
       } 
    }
    int max = 0;
    String flightMax = "";
    for(Map.Entry<String, Integer> entry : map.entrySet()){
      if(entry.getValue() > max){
         max = entry.getValue();
         flightMax = entry.getKey();
      }
    }
    %>
      <tr style='font-size: large;'>
      <td><%= flightMax %></td>
      <td><%= max %></td>
      </tr>
    
    </table>
   </body>
   </html>
   