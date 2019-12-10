<%-- MAX LEGRAND --%>
<%-- user feed --%>


<%@ page import="java.io.*,java.util.*,java.sql.*"%>

<%
ArrayList rs = (ArrayList)request.getAttribute("rs");
Dictionary row = (Hashtable)rs.get(0);
String name = (String)row.get("username");
String type = (String)row.get("type");
%>
<style>
button{
    font-size:24px;
}
</style>
<%-- <h1>${test}</h1> --%>
<h1>Hello, <%= name%></h1>

<%
if (type.equals("admin")){
%>

<a href="./admintools"><button>admin tools</button></a>
<a href="./salesreport"><button>sales report</button></a>
<a href="./reslist"><button>sales report</button></a>
<a href="./revgen"><button>sales report</button></a>
<a href="./customerrev"><button>sales report</button></a>
<a href="./allflights"><button>sales report</button></a>
<a href="./ticketssold"><button>sales report</button></a>
<%
}
else if (type.equals("rep")){
%>

<a href="./reptools"><button>rep tools</button></a>
<br/>
<br/>
<a href="./represerve"><button>reservations</button></a>
<br>
<br>
<a href="./repwaitinglist"><button>waiting lists</button></a>
<%
}
%>

<br>
<br>
<%-- <a href="./airline"><button>View Airlines</button></a> --%>
<%-- <a href="./tickets"><button>View My Tickets</button></a> --%>
<a href="./logout"><button>logout</button></a>