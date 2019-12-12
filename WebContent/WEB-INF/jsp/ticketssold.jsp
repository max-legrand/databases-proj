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
    Map<String, Integer> map = new HashMap<>();
    if (rs != null){
       for(int i = 0; i < rs.size(); i++) { 
        Dictionary row = (Hashtable)rs.get(i);
        int numfirst = Integer.valueOf((String) row.get("num_first_class"));
        int numecon = Integer.valueOf((String) row.get("num_economy"));
        int tickets = numfirst + numecon;
        if (map.isEmpty()){
           map.put(row.get("flightnum").toString(), tickets);
        }
        else if(map.containsKey(row.get("flightnum").toString())){
            map.put(row.get("flightnum").toString(), map.get(row.get("flightnum").toString())+tickets );
        } else {
            map.put(row.get("flightnum").toString(), tickets);
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
   