<%-- MAX LEGRAND --%>
<%-- form to add reservation --%>
<h1>Add a reservation</h1>

<form action = "./addresconf" method = "POST">
<input type="text" name="cid" placeholder="customer id">
<br/>
<br/>
<input type="text" name="flightnum" placeholder="flight #">
<br/>
<br/>
<h4># first class:</h4>
<input type="number" name="firstclass" min = 0 value="0">
<br/>
<br/>
<h4># economy:</h4>
<input type="number" name="economy" min = 0 value="0">
<br/>
<br/>
<input type="submit">
</form>