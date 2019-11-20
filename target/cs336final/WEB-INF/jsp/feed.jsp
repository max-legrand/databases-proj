<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
String name = (String)row.get("username");
String admin = (String)row.get("admin");
%>
<style>
button{
    font-size:24px;
}
</style>
<%-- <h1>${test}</h1> --%>
<h1>Hello, <%= name%></h1>

<%
if (Integer.parseInt(admin) == 1){
%>

<a href="./admintools"><button>admin tools</button></a>
<%
}
%>

<br>
<br>
<%-- <a href="./airline"><button>View Airlines</button></a> --%>
<%-- <a href="./tickets"><button>View My Tickets</button></a> --%>
<a href="./logout"><button>logout</button></a>