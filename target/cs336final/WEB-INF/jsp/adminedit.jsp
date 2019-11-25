<%@ page import="java.io.*,java.util.*,java.sql.*"%>


<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
%>
<h1>Edit User</h1>

<form action = "./admineditconf" method = "POST">
<input type="text" name="username" placeholder="username" value="<%=row.get("username")%>">
<br/>
<br/>
<input type="password" name="password" placeholder="password">
<br/>
<br/>
<input type="password" name="confpassword" placeholder="confirm password">
<br/>
<br/>
<select name ="type">

<% if (row.get("type").equals("customer")){
%>
<option selected="selected">customer</option>
<%
}
else{
%>
<option>customer</option>
<%
}
%>
<% if (row.get("type").equals("rep")){
%>
<option selected="selected">rep</option>
<%
}
else{
%>
<option>rep</option>
<%
}
%>
</select>
<br/>
<br/>
<input type="hidden" name="admin" value="true">
<input type="hidden" name="id" value="<%= row.get("id") %>">
<input type="submit">
</form>