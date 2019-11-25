<h1>Add a user</h1>

<form action = "./signupconf" method = "POST">
<input type="text" name="username" placeholder="username">
<br/>
<br/>
<input type="password" name="password" placeholder="password">
<br/>
<br/>
<input type="password" name="confpassword" placeholder="confirm password">
<br/>
<br/>
<select name ="type">
<option>customer</option>
<option>rep</option>
</select>
<br/>
<br/>
<input type="hidden" name="admin" value="true">
<input type="submit">
</form>