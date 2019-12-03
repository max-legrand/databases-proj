//ec2-3-18-112-162.us-east-2.compute.amazonaws.com

package com.flighttracker.controller;

import java.io.*;
import java.math.BigInteger;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;
import java.util.Properties;
import java.io.InputStream;


import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView; 



@Controller
public class FlightTracker {


    //MAX LEGRAND
    // gets url from textfile
    public String geturl() throws FileNotFoundException, IOException{
        // String fileName = "data.txt";
        // File file = new File(fileName);
        // FileReader fr = new FileReader(file);
        InputStream inputStream = getClass().getClassLoader()
                         .getResourceAsStream("/data.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        // BufferedReader br = new BufferedReader(fr);

        String line;
        line = br.readLine();
        br.close();
        return line;
    }

    //MAX LEGRAND
    // gets user from textfile
    public String getuser() throws FileNotFoundException, IOException{
        InputStream inputStream = getClass().getClassLoader()
                         .getResourceAsStream("/data.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        line = br.readLine();
        line = br.readLine();
        br.close();
        return line;
    }

    //MAX LEGRAND
    // gets password from textfile
    public String getpass() throws FileNotFoundException, IOException{
        InputStream inputStream = getClass().getClassLoader()
                         .getResourceAsStream("/data.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        line = br.readLine();
        line = br.readLine();
        line = br.readLine();
        br.close();
        return line;
    }

    //not needed for final version of project
    public static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); // hex encoding
    }

    //MAX LEGRAND
    // routes to login jsp
    @RequestMapping("/login")
    public ModelAndView login(HttpServletResponse response) {
        ModelAndView model = new ModelAndView("login");
        return model;
    }

    //MAX LEGRAND
    // routes to invalid login jsp
    @RequestMapping("/invalidlogin")
    public ModelAndView invalidlogin() {
        ModelAndView model = new ModelAndView("invalidlogin");
        return model;
    }

    //MAX LEGRAND
    // login confirmation / logistics
    @RequestMapping("/loginconf")
    public ModelAndView loginconf(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            @RequestParam("password") String password, @RequestParam("username") String username)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
            NoSuchAlgorithmException, SocketException, UnknownHostException, FileNotFoundException, IOException {
        

        String connectionURL = geturl();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        ModelAndView model;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, getuser(), getpass());
        statement = connection.createStatement();
        rs = statement.executeQuery("SELECT * FROM users where username=\""+username+"\"");
        if (!rs.next()){
            model =  new ModelAndView("redirect:invalidlogin");
            connection.close();
            return model;
        }
        if (!rs.getString("password").equals(password)){
            model =  new ModelAndView("redirect:invalidlogin");
       }
       else{
            String id = rs.getString("id");
                session.setAttribute("ID", id);
            model = new ModelAndView("redirect:/");
       }
        connection.close();
        return model;
    }

    //MAX LEGRAND
    // signup confirmation / logistics
    @RequestMapping("/signupconf")
    public ModelAndView signupconf(@RequestParam("password") String password,
            @RequestParam("confpassword") String confpassword,
            @RequestParam("username") String username, 
            @RequestParam(name="type", defaultValue = "customer") String type,
            @RequestParam(name="admin", defaultValue = "false") String admin
            )
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
            NoSuchAlgorithmException, FileNotFoundException, IOException {
        String connectionURL = geturl();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        ModelAndView model;
        if (!confpassword.equals(password) || password.equals("") || password == null){
            model =  new ModelAndView("redirect:invalid");
        }
        else{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM users where username=\""+username+"\"");
            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();
            if (rows > 0){
                if (admin.equals("true")){
                    model =  new ModelAndView("redirect:existsadmin");
                }
                else{
                    model =  new ModelAndView("redirect:exists");
                }
            }
            else{
                statement.executeUpdate("INSERT into users(username, password, type) VALUES(\""+username+"\", \""+password+"\",\""+type+"\");");
                if (admin.equals("true")){
                    model =  new ModelAndView("redirect:admintools");
                }
                else{
                    model =  new ModelAndView("redirect:/");
                }
            }
            
        }
        connection.close();
		return model;
    }
    
    //MAX LEGRAND
    // routes to signup jsp
    @RequestMapping("/signup")
	public ModelAndView signup() {
		ModelAndView model =  new ModelAndView("signup");
		return model;
    }

    //MAX LEGRAND
    // routes to existsadmin jsp
    @RequestMapping("/existsadmin")
	public ModelAndView existsadmin() {
		ModelAndView model =  new ModelAndView("existsadmin");
		return model;
    }

    //MAX LEGRAND
    // routes to invalid jsp
    @RequestMapping("/invalid")
	public ModelAndView invalid() {
		ModelAndView model =  new ModelAndView("invalid");
		return model;
    }

    //MAX LEGRAND
    // routes to exists jsp
    @RequestMapping("/exists")
	public ModelAndView exists() {
		ModelAndView model =  new ModelAndView("exists");
		return model;
    }
    
    //MAX LEGRAND
    // logout confirmation / logistics
    @RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpSession session, HttpServletResponse response)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, FileNotFoundException, IOException {

        if (session.getAttribute("ID")!=null){
            session.removeAttribute("ID");
        }

        ModelAndView model;
		model =  new ModelAndView("redirect:/");
		return model;
    }
	
    //MAX LEGRAND
    // root page / login redirect to feed
	@RequestMapping("/")
	public ModelAndView index(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{

        String userid = (String)session.getAttribute("ID");
        if (userid != null){
            String connectionURL = geturl();
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from users where id=\""+userid+"\"");
            rs.last();
            int rows = rs.getRow();
            rs.first();

            if (rows > 0){

                ArrayList Rows = new ArrayList();
                for (int i = 0; i < rows; i++){
                 Dictionary row = new Hashtable();
                 ResultSetMetaData rsmd = rs.getMetaData();
                 int columnsNumber = rsmd.getColumnCount();
                 
                 for (i = 1; i <= columnsNumber; i++){
                     row.put(rsmd.getColumnName(i), rs.getString(i));
                 }
                 Rows.add(row);
                 rs.next();
                }
                connection.close();
                    ModelAndView model = new ModelAndView("feed", "rs", Rows);

                    return model;
            }
            connection.close();

        }
        ModelAndView model =  new ModelAndView("index");
        model.addObject("conn", geturl());
		return model;
	}
	
    //MAX LEGRAND
    // determine if account is admin
    public boolean isadmin(String userid) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        String connectionURL = geturl();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
        statement = connection.createStatement();
        rs = statement.executeQuery("select * from users where id=\""+userid+"\"");
        rs.last();
        int rows = rs.getRow();
        rs.first();
        if (rs.getString("type").equals("admin")){
            connection.close();
            return true;
        }
        else{
            return false;
        }
    }

    //MAX LEGRAND
    // determine if account is rep
    public boolean isrep(String userid) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        String connectionURL = geturl();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
        statement = connection.createStatement();
        rs = statement.executeQuery("select * from users where id=\""+userid+"\"");
        rs.last();
        int rows = rs.getRow();
        rs.first();
        if (rs.getString("type").equals("rep")){
            connection.close();
            return true;
        }
        else{
            return false;
        }
    }

    //MAX LEGRAND
    // routes to admintools jsp
    @RequestMapping("/admintools")
	public ModelAndView admintools(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{

        String userid = (String)session.getAttribute("ID");
        if (userid != null){
            if (isadmin(userid)){
                    String connectionURL = geturl();
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();

                    rs = statement.executeQuery("select * from users where type='customer' or type='rep'");
                    rs.beforeFirst();
                    ArrayList Rows = new ArrayList();
                    while(rs.next()){
                        Dictionary row = new Hashtable();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        for (int i = 1; i <= columnsNumber; i++){
                            row.put(rsmd.getColumnName(i), rs.getString(i));
                        }
                        Rows.add(row);
                    }
                    connection.close();
                    ModelAndView model = new ModelAndView("admintools", "rs", Rows);
                    return model;
                }
                   
                   
                

                
                ModelAndView model = new ModelAndView("redirect:/");

                return model;
            }


        
        ModelAndView model =  new ModelAndView("index");
		return model;
	}

    //MAX LEGRAND
    // routes to adminedit jsp
    @RequestMapping("/adminedit")
	public ModelAndView adminedit(@RequestParam(name="selectedid") String selectedid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        if (selectedid == null || selectedid.equals("") || selectedid.equals(" ")){
            ModelAndView model =  new ModelAndView("redirect:admintools");
            return model;
        }



        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isadmin(userid)){
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        Connection connection = null;
                        Statement statement = null;
                        ResultSet rs = null;
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("select * from users where id="+selectedid);
                        rs.last();
                        int rows = rs.getRow();
                        rs.first();
                        if (rows > 0){
                            if (rs.getString("type").equals("admin")){
                                ModelAndView model =  new ModelAndView("redirect:admintools");
                                connection.close();
                                return model;
                            }
                            ArrayList Rows = new ArrayList();
                       
                            Dictionary row = new Hashtable();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            
                            for (int i = 1; i <= columnsNumber; i++){
                                row.put(rsmd.getColumnName(i), rs.getString(i));
                            }
                            Rows.add(row);
                            connection.close();
                            ModelAndView model = new ModelAndView("adminedit", "rs", Rows);
                            return model;
                        }
                        
                    }
                   
                    
                    ModelAndView model =  new ModelAndView("redirect:admintools");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
	}
	
    //MAX LEGRAND
    // routes to adminadd jsp
    @RequestMapping("/adminadd")
	public ModelAndView adminadd(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isadmin(userid)){
                        ModelAndView model =  new ModelAndView("adminadd");
                        return model;
                }
                   
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }
    
    //MAX LEGRAND
    // delete user from admin page logistics
    @RequestMapping("/admindelete")
    public ModelAndView admindelete(@RequestParam("deleteid") String delid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isadmin(userid)){
                Connection connection = null;
                Statement statement = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                statement.executeUpdate("delete from users where id=\""+delid+"\"");
                connection.close();
                ModelAndView model =  new ModelAndView("redirect:/admintools");
                return model;
            }
                
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  

//MAX LEGRAND
// routes to duplicate jsp
@RequestMapping("/duplicate")
public ModelAndView duplicate(){
    return new ModelAndView("duplicate");
}

//MAX LEGRAND
// admin edit confrimation 
@RequestMapping("/admineditconf")
public ModelAndView admineditconf(
    @RequestParam("username") String username, @RequestParam("prevusername") String prevusername, @RequestParam("type") String type, @RequestParam("id") String uid,
    @RequestParam("password") String password, @RequestParam("confpassword") String confpassword, @RequestParam("admin") String admin,
HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isadmin(userid)){
                ModelAndView model = null;
                if (!confpassword.equals(password) || password.equals("") || password == null){
                    return new ModelAndView("passmatch");
                }
                else{
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();
                    if (!prevusername.equals(username)){
                        rs = statement.executeQuery("SELECT * FROM users where username=\""+username+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:duplicate");
                        }
                        else{
                            
                            if (admin.equals("true")){
                                model =  new ModelAndView("redirect:admintools");
                                statement.executeUpdate("UPDATE users set username=\""+username+"\", password=\""+password+"\", type=\""+type+"\" where id="+uid+";");
                            }
                            else{
                                model =  new ModelAndView("redirect:/");
                            }
                            
                        }
                    }
                    else{
                        if (admin.equals("true")){
                            model =  new ModelAndView("redirect:admintools");
                            statement.executeUpdate("UPDATE users set username=\""+username+"\", password=\""+password+"\", type=\""+type+"\" where id="+uid+";");
                        }
                        else{
                            model =  new ModelAndView("redirect:/");
                        }
                    }
                    
                        
                        
                    connection.close();
                }
                
                return model;


            }
                
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  
	
//MAX LEGRAND
// routes to rep tools
@RequestMapping("/reptools")
	public ModelAndView reptools(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
            if (isrep(userid)){
                    String connectionURL = geturl();
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();

                    rs = statement.executeQuery("select * from aircrafts");
                    rs.beforeFirst();
                    ArrayList Rows = new ArrayList();
                    while(rs.next()){
                        Dictionary row = new Hashtable();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        for (int i = 1; i <= columnsNumber; i++){
                            row.put(rsmd.getColumnName(i), rs.getString(i));
                        }
                        Rows.add(row);
                    }
                    
                    ModelAndView model = new ModelAndView("reptools", "aircraftrs", Rows);
                    
                    rs = statement.executeQuery("select * from airports");
                    rs.beforeFirst();
                    Rows = new ArrayList();
                    while(rs.next()){
                        Dictionary row = new Hashtable();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        for (int i = 1; i <= columnsNumber; i++){
                            row.put(rsmd.getColumnName(i), rs.getString(i));
                        }
                        Rows.add(row);
                    }
                    model.addObject("airportsrs", Rows);

                    rs = statement.executeQuery("select * from flights");
                    rs.beforeFirst();
                    Rows = new ArrayList();
                    while(rs.next()){
                        Dictionary row = new Hashtable();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        for (int i = 1; i <= columnsNumber; i++){
                            row.put(rsmd.getColumnName(i), rs.getString(i));
                        }
                        Rows.add(row);
                    }
                    model.addObject("flightsrs", Rows);

                    connection.close();
                    return model;
                }
                   
                   
                

                
                ModelAndView model = new ModelAndView("redirect:/");

                return model;
            }


        
        ModelAndView model =  new ModelAndView("index");
		return model;
	}

    //MAX LEGRAND
    // routes to add aircraft jsp
    @RequestMapping("/addaircraft")
	public ModelAndView addaircraft(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                        ModelAndView model =  new ModelAndView("addaircraft");
                        return model;
                }
                   
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }

    //MAX LEGRAND
    // add aircraft confirmation / logistics
    @RequestMapping("/addaircraftconf")
    public ModelAndView addaircraftconf(@RequestParam("id") String id,HttpSession session
            )
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
            NoSuchAlgorithmException, FileNotFoundException, IOException {

        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    ModelAndView model;
                    
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("SELECT * FROM aircrafts where id=\""+id+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:aircraftexists");
                        }
                        else{
                            statement.executeUpdate("INSERT into aircrafts(id) VALUES(\""+id+"\");");

                                model =  new ModelAndView("redirect:reptools");
                            
                        }
                        
                    
                    connection.close();
                    return model;
                }
                    
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
        return model;       
        
    }
    
    //MAX LEGRAND
    // routes to edit aircraft jsp
    @RequestMapping("/aircraftedit")
	public ModelAndView aircraftedit(@RequestParam(name="selectedid") String selectedid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        if (selectedid == null || selectedid.equals("") || selectedid.equals(" ")){
            ModelAndView model =  new ModelAndView("redirect:reptools");
            return model;
        }
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        Connection connection = null;
                        Statement statement = null;
                        ResultSet rs = null;
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("select * from aircrafts where id=\""+selectedid+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.first();
                        if (rows > 0){
                            ArrayList Rows = new ArrayList();
                       
                            Dictionary row = new Hashtable();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            
                            for (int i = 1; i <= columnsNumber; i++){
                                row.put(rsmd.getColumnName(i), rs.getString(i));
                            }
                            Rows.add(row);
                            connection.close();
                            ModelAndView model = new ModelAndView("aircraftedit", "rs", Rows);
                            return model;
                        }
                        
                    }

            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }
    
    //MAX LEGRAND
    // aircraft edit confirmation / logistics
    @RequestMapping("/aircrafteditconf")
    public ModelAndView aircrafteditconf(
    @RequestParam("id") String id,@RequestParam("previd") String previd, 
        HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                ModelAndView model = null;
                
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();      
                    if (!previd.equals(id)){
                        rs = statement.executeQuery("SELECT * FROM aircrafts where id=\""+id+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:duplicate");
                        }
                        else{
                        
                            model =  new ModelAndView("redirect:reptools");
                            statement.executeUpdate("UPDATE aircrafts set id=\""+id+"\" where id=\""+previd+"\";");
                        }
                    }
                    else{
                        model =  new ModelAndView("redirect:reptools");
                        statement.executeUpdate("UPDATE aircrafts set id=\""+id+"\" where id=\""+previd+"\";");
                    }
                    connection.close();
                
                
                return model;


            }
                
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  
    
//MAX LEGRAND
// delete aircraft
@RequestMapping("/aircraftdel")
public ModelAndView aircraftdel(@RequestParam("deleteid") String delid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                Connection connection = null;
                Statement statement = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                statement.executeUpdate("delete from aircrafts where id=\""+delid+"\"");
                connection.close();
                ModelAndView model =  new ModelAndView("redirect:/reptools");
                return model;
            }
                           
    }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  

//MAX LEGRAND
// routes to add airport jsp
@RequestMapping("/addairports")
	public ModelAndView addairports(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                        ModelAndView model =  new ModelAndView("addairports");
                        return model;
                }
                   
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }
    //MAX LEGRAND
    // add airport conf
    @RequestMapping("/addairportsconf")
    public ModelAndView addairportsconf(@RequestParam("id") String id,HttpSession session
            )
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
            NoSuchAlgorithmException, FileNotFoundException, IOException {

        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    ModelAndView model;
                    
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("SELECT * FROM airports where id=\""+id+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:airportexists");
                        }
                        else{
                            statement.executeUpdate("INSERT into airports(id) VALUES(\""+id+"\");");

                                model =  new ModelAndView("redirect:reptools");
                            
                        }
                        
                    
                    connection.close();
                    return model;
                }
                    
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
        return model;       
        
    }
    //MAX LEGRAND
    // routes to add airport edit jsp
    @RequestMapping("/airportsedit")
	public ModelAndView airportsedit(@RequestParam(name="selectedid") String selectedid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        if (selectedid == null || selectedid.equals("") || selectedid.equals(" ")){
            ModelAndView model =  new ModelAndView("redirect:reptools");
            return model;
        }
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        Connection connection = null;
                        Statement statement = null;
                        ResultSet rs = null;
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("select * from airports where id=\""+selectedid+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.first();
                        if (rows > 0){
                            ArrayList Rows = new ArrayList();
                       
                            Dictionary row = new Hashtable();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            
                            for (int i = 1; i <= columnsNumber; i++){
                                row.put(rsmd.getColumnName(i), rs.getString(i));
                            }
                            Rows.add(row);
                            connection.close();
                            ModelAndView model = new ModelAndView("airportsedit", "rs", Rows);
                            return model;
                        }
                        
                    }

            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }
    
    //MAX LEGRAND
    // edit airports logistics
    @RequestMapping("/airportseditconf")
public ModelAndView airportseditconf(
    @RequestParam("id") String id,@RequestParam("previd") String previd, 
HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                ModelAndView model = null;
                
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();      
                    if (!previd.equals(id)){
                        rs = statement.executeQuery("SELECT * FROM airports where id=\""+id+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:duplicate");
                        }
                        else{                  
                            model =  new ModelAndView("redirect:reptools");
                            statement.executeUpdate("UPDATE airports set id=\""+id+"\" where id=\""+previd+"\";");
                        }
                    }
                    else{
                                    
                        model =  new ModelAndView("redirect:reptools");
                        statement.executeUpdate("UPDATE airports set id=\""+id+"\" where id=\""+previd+"\";");
                    
                    }
                    
                    connection.close();
                
                
                return model;


            }
                
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  

//MAX LEGRAND
// delete airport
@RequestMapping("/airportsdel")
public ModelAndView airportsdel(@RequestParam("deleteid") String delid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                Connection connection = null;
                Statement statement = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                statement.executeUpdate("delete from airports where id=\""+delid+"\"");
                connection.close();
                ModelAndView model =  new ModelAndView("redirect:/reptools");
                return model;
            }
                           
    }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  

//MAX LEGRAND
// routes to add flight jsp
@RequestMapping("/flightsadd")
	public ModelAndView flightsadd(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                        ModelAndView model =  new ModelAndView("flightsadd");
                        return model;
                }
                   
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }
    
    //MAX LEGRAND
    // add flight conf
    @RequestMapping("/flightsaddconf")
    public ModelAndView flightsaddconf(@RequestParam("number") String number, @RequestParam("depart") String depart,
    @RequestParam("arrive") String arrive, @RequestParam("farefirst") int firstclass,
    @RequestParam("fareecon") int econclass, @RequestParam("type") String type, HttpSession session
            )
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
            NoSuchAlgorithmException, FileNotFoundException, IOException {
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    ModelAndView model;
                    
                        Class.forName("com.mysql .jdbc.Driver").newInstance();
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("SELECT * FROM flights where number=\""+number+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:duplicate");
                        }
                        else{
                            statement.executeUpdate("INSERT into flights(number, type, depart_time, arrive_time, fare_first, fare_econ) VALUES(\""+number+"\", \""+type+"\", \""+depart+"\", \""+arrive+"\", "+firstclass+", "+econclass+");");

                            model =  new ModelAndView("redirect:reptools");
                            
                        }
                        
                    
                    connection.close();
                    return model;
                }
                    
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
        return model;       
        
    }

    //MAX LEGRAND
    // routes to flights edit jsp
    @RequestMapping("/flightsedit")
	public ModelAndView flightsedit(@RequestParam(name="selectnum") String selectnum, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        if (selectnum == null || selectnum.equals("") || selectnum.equals(" ")){
            ModelAndView model =  new ModelAndView("redirect:reptools");
            return model;
        }
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        Connection connection = null;
                        Statement statement = null;
                        ResultSet rs = null;
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("select * from flights where number=\""+selectnum+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.first();
                        if (rows > 0){
                            ArrayList Rows = new ArrayList();
                       
                            Dictionary row = new Hashtable();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            
                            for (int i = 1; i <= columnsNumber; i++){
                                row.put(rsmd.getColumnName(i), rs.getString(i));
                            }
                            Rows.add(row);
                            connection.close();
                            ModelAndView model = new ModelAndView("flightsedit", "rs", Rows);
                            return model;
                        }
                        
                    }

            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }

    @RequestMapping("/flightseditconf")
public ModelAndView flightseditconf(
    @RequestParam("number") String number, @RequestParam("prevnumber") String prevnumber, @RequestParam("depart") String depart,
    @RequestParam("arrive") String arrive, @RequestParam("farefirst") int firstclass,
    @RequestParam("fareecon") int econclass, @RequestParam("type") String type, HttpSession session
) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                ModelAndView model = null;
                
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();      
                    if (!prevnumber.equals(number)){
                        rs = statement.executeQuery("SELECT * FROM flights where number=\""+number+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:duplicate");
                        }
                        else{                  
                            model =  new ModelAndView("redirect:reptools");
                            statement.executeUpdate("UPDATE flights set number=\""+number+"\", depart_time=\""+depart+"\", arrive_time=\""+arrive+"\", fare_first="+firstclass+", fare_econ="+econclass+", type=\""+type+"\" where number=\""+prevnumber+"\";");
                        }
                    }
                   else{
                                   
                        model =  new ModelAndView("redirect:reptools");
                        statement.executeUpdate("UPDATE flights set number=\""+number+"\", depart_time=\""+depart+"\", arrive_time=\""+arrive+"\", fare_first="+firstclass+", fare_econ="+econclass+", type=\""+type+"\" where number=\""+prevnumber+"\";");
                   }
                    connection.close();
                
                
                return model;


            }
                
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  

    //MAX LEGRAND
    // delete flights from table
    @RequestMapping("/flightsdel")
    public ModelAndView flightsdel(@RequestParam("delnum") String delnum, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                Connection connection = null;
                Statement statement = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                statement.executeUpdate("delete from flights where number=\""+delnum+"\"");
                connection.close();
                ModelAndView model =  new ModelAndView("redirect:/reptools");
                return model;
            }
                
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}  

    //MAX LEGRAND
    // reservations for representatives
@RequestMapping("/represerve")
public ModelAndView represerve(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
String connectionURL = geturl();
String userid = (String)session.getAttribute("ID");
if (userid != null){
        if (isrep(userid)){
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
            statement = connection.createStatement();

            rs = statement.executeQuery("select * from reservations");
            rs.beforeFirst();
            ArrayList Rows = new ArrayList();
            while(rs.next()){
                Dictionary row = new Hashtable();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                for (int i = 1; i <= columnsNumber; i++){
                    row.put(rsmd.getColumnName(i), rs.getString(i));
                }
                Rows.add(row);
            }
            connection.close();
            ModelAndView model = new ModelAndView("represerve", "rs", Rows);
            return model;
        }
            
            
            ModelAndView model =  new ModelAndView("redirect:/");
            return model;
    }


ModelAndView model =  new ModelAndView("index");
return model;
}  


    //MAX LEGRAND
    // add reservations page
@RequestMapping("/addres")
public ModelAndView addres(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    String connectionURL = geturl();
    String userid = (String)session.getAttribute("ID");
    if (userid != null){
            if (isrep(userid)){
                    ModelAndView model =  new ModelAndView("addres");
                    return model;
            }
               
                
                ModelAndView model =  new ModelAndView("redirect:/");
                return model;
        }

    
    ModelAndView model =  new ModelAndView("index");
    return model;
}

    //MAX LEGRAND
    // add reservation logic
    @RequestMapping("/addresconf")
    public ModelAndView addresconf(@RequestParam("cid") String cid, @RequestParam("flightnum") String flightnum,
    @RequestParam("firstclass") String firstclass, @RequestParam("economy") String economy,
    HttpSession session
            )
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
            NoSuchAlgorithmException, FileNotFoundException, IOException {
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if (userid != null){
                if (isrep(userid)){
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    ModelAndView model;
                    
                        Class.forName("com.mysql .jdbc.Driver").newInstance();
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("SELECT * FROM users where id="+cid);
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows == 0){
                                model =  new ModelAndView("redirect:userdne");
                        }
                        rs = statement.executeQuery("SELECT * FROM flights where number=\""+flightnum+"\"");
                        rs.last();
                        rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows == 0){
                                model =  new ModelAndView("redirect:flightdne");
                        }

                        rs = statement.executeQuery("SELECT * FROM reservations where cid="+cid+" and flightnum=\""+flightnum+"\"");
                        rs.last();
                        rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("redirect:resexists");
                        }
                        else{
                            statement.executeUpdate("INSERT into reservations(cid, flightnum, num_first_class, num_economy) VALUES("+cid+", \""+flightnum+"\", "+firstclass+", \""+economy+");");

                            model =  new ModelAndView("redirect:represerve");
                            
                        }
                        
                    
                    connection.close();
                    return model;
                }
                    
                    
                    ModelAndView model =  new ModelAndView("redirect:/");
                    return model;
            }

        
        ModelAndView model =  new ModelAndView("index");
        return model;       
        
    }


    // MAX LEGRAND
    // reservation exists
    @RequestMapping("/resexists")
    public ModelAndView resexists(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        ModelAndView model =  new ModelAndView("resexists");
        return model;
    }

}