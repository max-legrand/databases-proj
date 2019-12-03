<%-- MAX LEGRAND --%>
<%-- edit reservation FORM --%>

<%@ page import="java.io.*,java.util.*,java.sql.*"%>


<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
%>
<h1>Edit Reservation</h1>

<form action = "./reseditconf" method = "POST">
<h4>customer id</h4>
<input type="text" readonly name="cid" placeholder="id" value="<%=row.get("id")%>">
<br/>
<h4>flight number</h4>
<input type="text" readonly name="flightnum" placeholder="flight #" value="<%=row.get("flightnum")%>">
<br/>
<h4># first class</h4>
<input type="number" min=0 name="first_class" value="<%=row.get("num_first_class")%>">
<br/>
<h4># economy</h4>
<input type="number" min=0 name="economy" value="<%=row.get("num_economy")%>">
<input type="hidden" name="id" value="<%=row.get("id")%>">
<br/>
<br/>
<input type="submit">
</form>