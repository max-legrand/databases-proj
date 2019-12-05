<%-- MAX LEGRAND --%>
<%-- edit AIRCRAFT FORM --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>


<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
ArrayList airline = (ArrayList)request.getAttribute("airline");
Dictionary row = (Hashtable)rs.get(0);
%>
<h1>Edit Aircraft</h1>

<form action = "./aircrafteditconf" method = "POST">
<input type="text" name="id" placeholder="id" value="<%=row.get("id")%>">
<br/>
<h3>airline</h3>
<select name="airline">
<%

for(int i = 0; i < airline.size(); i++) { 
    Dictionary row2 = (Hashtable)airline.get(i);
    if (row.get("airline").equals(row2.get("id"))){
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
<input type="hidden" name="previd" value="<%=row.get("id")%>">
<br/>
<br/>
<input type="submit">
</form>