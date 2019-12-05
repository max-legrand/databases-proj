<%-- MAX LEGRAND --%>
<%-- edit flight FORM --%>

<%@ page language="java" %>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%-- <%!
public String formattime(String time){
    String final = "";
    String final = time.substring(0,9)+"T"+time.substring(11,15);
    return final;
}
%> --%>

<%
ArrayList aircrafts = (ArrayList)request.getAttribute("aircrafts");
ArrayList airports = (ArrayList)request.getAttribute("airports");
//Dictionary row = (Hashtable)rs.get(0);
//String name = (String)row.get("username");
//String admin = (String)row.get("admin");
%>

<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
%>

<% 
String depart_time = row.get("depart_time").toString();
String depart = depart_time.substring(0,10)+"T"+depart_time.substring(11,16);
String arrive_time = row.get("arrive_time").toString();
String arrive = arrive_time.substring(0,10)+"T"+arrive_time.substring(11,16); 
%>
<h1>Edit Flight</h1>
<form action = "./flightseditconf" method = "POST">
<input type="text" name="number" placeholder="number" value="<%=row.get("number") %>">
<br/>
<br/>
<select name="type">
<% if (row.get("type").equals("One-Way")){
 %>
 <option selected="selected">One-Way</option>
 <%   
}else{
%>
<option>One-Way</option>
<%    
}
%>

<% if (row.get("type").equals("Round-Trip")){
 %>
 <option selected="selected">Round-Trip</option>
 <%   
}else{
%>
<option>Round-Trip</option>
<%    
}
%>

<% if (row.get("type").equals("Flexible Date/time")){
 %>
 <option selected="selected">Flexible Date/time</option>
 <%   
}else{
%>
<option>Flexible Date/time</option>
<%    
}
%>

</select>
<br/>
<br/>
<h3>depart time</h3>
<input type="datetime-local" name="depart" value="<%=depart%>">
<br/>
<br/>
<h3>arrive time</h3>
<input type="datetime-local" name="arrive" value="<%= arrive%>">
<br/>
<br/>

<h3>aircraft</h3>
<select name="aircraft">
<%

for(int i = 0; i < aircrafts.size(); i++) { 
    Dictionary row2 = (Hashtable)aircrafts.get(i);
    if (row.get("aircraft").equals(row2.get("id"))){
        %>
        <option selected="selected" value="<%=row2.get("id")%>"><%=row2.get("id")%> - <%=row2.get("airline") %></option>
   <% 
    }else{
  %>      
  <option value="<%=row2.get("id")%>"><%=row2.get("id")%> - <%=row2.get("airline") %></option>
  
  <%
    }
%>
        

<% } %>


</select>

<h3>airport to:</h3>
<select name="airport_to">
<%

for(int i = 0; i < airports.size(); i++) { 
    Dictionary row2 = (Hashtable)airports.get(i);
    if (row.get("airport_to").equals(row2.get("id"))){
        %>
        <option selected="selected" value="<%=row2.get("id")%>"><%=row2.get("id")%></option>
   <% 
    }else{
  %>      
  <option value="<%=row2.get("id")%>"><%=row2.get("id")%></option>
  
  <%
    }
%>

<% } %>

</select>

<h3>airport from:</h3>
<select name="airport_from">
<%

for(int i = 0; i < airports.size(); i++) { 
    Dictionary row2 = (Hashtable)airports.get(i);
    if (row.get("airport_from").equals(row2.get("id"))){
        %>
        <option selected="selected" value="<%=row2.get("id")%>"><%=row2.get("id")%></option>
   <% 
    }else{
  %>      
  <option value="<%=row2.get("id")%>"><%=row2.get("id")%></option>
  
  <%
    }
%>

<% } %>

</select>

<h3>fare: first class</h3>
<input type="number" name="farefirst" placeholder=0 value="<%= row.get("fare_first") %>">
<br/>
<br/>
<h3>fare: economy class</h3>
<input type="number" name="fareecon" placeholder=0 value="<%= row.get("fare_econ") %>">
<input type="hidden" name="prevnumber" value="<%= row.get("number") %>">
<br/>
<br/>
<input type="submit">
</form>