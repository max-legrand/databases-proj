<%-- MAX LEGRAND --%>
<%-- form to add flight --%>
<h1>Add an flight</h1>

<form action = "./flightsaddconf" method = "POST">
<input type="text" name="number" placeholder="number">
<br/>
<br/>
<select name="type">
<option>One-Way</option>
<option>Round-Trip</option>
<option>Flexible Date/time</option>
</select>
<br/>
<br/>
<h3>depart time</h3>
<input type="datetime-local" name="depart">
<br/>
<br/>
<h3>arrive time</h3>
<input type="datetime-local" name="arrive">
<br/>
<br/>
<h3>fare: first class</h3>
<input type="number" name="farefirst" placeholder=0>
<br/>
<br/>
<h3>fare: economy class</h3>
<input type="number" name="fareecon" placeholder=0>
<br/>
<br/>
<input type="submit">
</form>