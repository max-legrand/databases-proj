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
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;

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

                ArrayList Rows = singleAL(rs);
                // for (int i = 0; i < rows; i++){
                //  Dictionary row = new Hashtable();
                //  ResultSetMetaData rsmd = rs.getMetaData();
                //  int columnsNumber = rsmd.getColumnCount();
                 
                //  for (i = 1; i <= columnsNumber; i++){
                //      row.put(rsmd.getColumnName(i), rs.getString(i));
                //  }
                //  Rows.add(row);
                //  rs.next();
                // }
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
                    ArrayList Rows = multiAL(rs);
                    // while(rs.next()){
                    //     Dictionary row = new Hashtable();
                    //     ResultSetMetaData rsmd = rs.getMetaData();
                    //     int columnsNumber = rsmd.getColumnCount();
                    //     for (int i = 1; i <= columnsNumber; i++){
                    //         row.put(rsmd.getColumnName(i), rs.getString(i));
                    //     }
                    //     Rows.add(row);
                    // }
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
                            ArrayList Rows = singleAL(rs);
                       
                            // Dictionary row = new Hashtable();
                            // ResultSetMetaData rsmd = rs.getMetaData();
                            // int columnsNumber = rsmd.getColumnCount();
                            
                            // for (int i = 1; i <= columnsNumber; i++){
                            //     row.put(rsmd.getColumnName(i), rs.getString(i));
                            // }
                            // Rows.add(row);
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
    

public ArrayList multiAL(ResultSet rs) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
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
    return Rows;
}

public ArrayList singleAL(ResultSet rs) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
    ArrayList Rows = new ArrayList();                   
    Dictionary row = new Hashtable();
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnsNumber = rsmd.getColumnCount();
    
    for (int i = 1; i <= columnsNumber; i++){
        row.put(rsmd.getColumnName(i), rs.getString(i));
    }
    Rows.add(row);
    return Rows;
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
                    // ArrayList Rows = new ArrayList();
                    // while(rs.next()){
                    //     Dictionary row = new Hashtable();
                    //     ResultSetMetaData rsmd = rs.getMetaData();
                    //     int columnsNumber = rsmd.getColumnCount();
                    //     for (int i = 1; i <= columnsNumber; i++){
                    //         row.put(rsmd.getColumnName(i), rs.getString(i));
                    //     }
                    //     Rows.add(row);
                    // }
                    ArrayList Rows = multiAL(rs);
                    
                    ModelAndView model = new ModelAndView("reptools", "aircraftrs", Rows);
                    
                    rs = statement.executeQuery("select * from airports");
                    rs.beforeFirst();
                    Rows = multiAL(rs);
                    // while(rs.next()){
                    //     Dictionary row = new Hashtable();
                    //     ResultSetMetaData rsmd = rs.getMetaData();
                    //     int columnsNumber = rsmd.getColumnCount();
                    //     for (int i = 1; i <= columnsNumber; i++){
                    //         row.put(rsmd.getColumnName(i), rs.getString(i));
                    //     }
                    //     Rows.add(row);
                    // }
                    model.addObject("airportsrs", Rows);

                    rs = statement.executeQuery("select * from flights join aircrafts on flights.aircraft = aircrafts.id order by number");
                    rs.beforeFirst();
                    Rows = multiAL(rs);
                    // while(rs.next()){
                    //     Dictionary row = new Hashtable();
                    //     ResultSetMetaData rsmd = rs.getMetaData();
                    //     int columnsNumber = rsmd.getColumnCount();
                    //     for (int i = 1; i <= columnsNumber; i++){
                    //         row.put(rsmd.getColumnName(i), rs.getString(i));
                    //     }
                    //     Rows.add(row);
                    // }
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
                        Connection connection = null;
                        Statement statement = null;
                        ResultSet rs = null;
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();

                        rs = statement.executeQuery("select * from airline");
                        rs.beforeFirst();
                        ArrayList Rows = multiAL(rs);
                        // while(rs.next()){
                        //     Dictionary row = new Hashtable();
                        //     ResultSetMetaData rsmd = rs.getMetaData();
                        //     int columnsNumber = rsmd.getColumnCount();
                        //     for (int i = 1; i <= columnsNumber; i++){
                        //         row.put(rsmd.getColumnName(i), rs.getString(i));
                        //     }
                        //     Rows.add(row);
                        // }

                        ModelAndView model =  new ModelAndView("addaircraft", "airline", Rows );
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
    public ModelAndView addaircraftconf(@RequestParam("id") String id,@RequestParam("airline") String airline,HttpSession session
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
                            statement.executeUpdate("INSERT into aircrafts(id, airline) VALUES(\""+id+"\", \""+airline+"\");");

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
                            ArrayList Rows = singleAL(rs);
                       
                            // Dictionary row = new Hashtable();
                            // ResultSetMetaData rsmd = rs.getMetaData();
                            // int columnsNumber = rsmd.getColumnCount();
                            
                            // for (int i = 1; i <= columnsNumber; i++){
                            //     row.put(rsmd.getColumnName(i), rs.getString(i));
                            // }
                            // Rows.add(row);
                            ModelAndView model = new ModelAndView("aircraftedit", "rs", Rows);

                            rs = statement.executeQuery("select * from airline");
                            rs.beforeFirst();
                            Rows = multiAL(rs);
                            // while(rs.next()){
                            //     row = new Hashtable();
                            //     rsmd = rs.getMetaData();
                            //     columnsNumber = rsmd.getColumnCount();
                            //     for (int i = 1; i <= columnsNumber; i++){
                            //         row.put(rsmd.getColumnName(i), rs.getString(i));
                            //     }
                            //     Rows.add(row);
                            // }

                            model.addObject("airline", Rows);

                            connection.close();
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
    @RequestParam("id") String id,@RequestParam("previd") String previd, @RequestParam("airline") String airline, 
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
                            statement.executeUpdate("UPDATE aircrafts set id=\""+id+"\", airline=\""+airline+"\" where id=\""+previd+"\";");
                        }
                    }
                    else{
                        model =  new ModelAndView("redirect:reptools");
                        statement.executeUpdate("UPDATE aircrafts set id=\""+id+"\", airline=\""+airline+"\" where id=\""+previd+"\";");
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
                            ArrayList Rows = singleAL(rs);
                       
                            // Dictionary row = new Hashtable();
                            // ResultSetMetaData rsmd = rs.getMetaData();
                            // int columnsNumber = rsmd.getColumnCount();
                            
                            // for (int i = 1; i <= columnsNumber; i++){
                            //     row.put(rsmd.getColumnName(i), rs.getString(i));
                            // }
                            // Rows.add(row);
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
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                    statement = connection.createStatement();

                    rs = statement.executeQuery("select * from airports");
                    rs.beforeFirst();
                    ArrayList Rows = multiAL(rs);
                    // while(rs.next()){
                    //     Dictionary row = new Hashtable();
                    //     ResultSetMetaData rsmd = rs.getMetaData();
                    //     int columnsNumber = rsmd.getColumnCount();
                    //     for (int i = 1; i <= columnsNumber; i++){
                    //         row.put(rsmd.getColumnName(i), rs.getString(i));
                    //     }
                    //     Rows.add(row);
                    // }

                        
                    ModelAndView model =  new ModelAndView("flightsadd", "airports", Rows);
                    rs = statement.executeQuery("select * from aircrafts");
                    rs.beforeFirst();
                    Rows = multiAL(rs);
                    // while(rs.next()){
                    //     Dictionary row = new Hashtable();
                    //     ResultSetMetaData rsmd = rs.getMetaData();
                    //     int columnsNumber = rsmd.getColumnCount();
                    //     for (int i = 1; i <= columnsNumber; i++){
                    //         row.put(rsmd.getColumnName(i), rs.getString(i));
                    //     }
                    //     Rows.add(row);
                    // }
                    model.addObject("aircrafts", Rows);
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
    // add flight conf
    @RequestMapping("/flightsaddconf")
    public ModelAndView flightsaddconf(@RequestParam("number") String number, @RequestParam("depart") String depart,
    @RequestParam("arrive") String arrive, @RequestParam("farefirst") int firstclass,
    @RequestParam("fareecon") int econclass, @RequestParam("type") String type, 
    @RequestParam("aircraft") String aircraft, @RequestParam("airport_to") String ato, @RequestParam("airport_from") String afrom,
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
                    
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
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
                            statement.executeUpdate("INSERT into flights(number, type, depart_time, arrive_time, fare_first, fare_econ, aircraft, airport_to, airport_from) VALUES(\""+number+"\", \""+type+"\", \""+depart+"\", \""+arrive+"\", "+firstclass+", "+econclass+", \""+aircraft+"\", \""+ato+"\", \""+afrom+"\");");

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
                            ArrayList Rows = singleAL(rs);
                       
                            // Dictionary row = new Hashtable();
                            // ResultSetMetaData rsmd = rs.getMetaData();
                            // int columnsNumber = rsmd.getColumnCount();
                            
                            // for (int i = 1; i <= columnsNumber; i++){
                            //     row.put(rsmd.getColumnName(i), rs.getString(i));
                            // }
                            // Rows.add(row);
                            ModelAndView model = new ModelAndView("flightsedit", "rs", Rows);

                            rs = statement.executeQuery("select * from airports");
                            rs.beforeFirst();
                            Rows = multiAL(rs);
                            // while(rs.next()){
                            //     row = new Hashtable();
                            //     rsmd = rs.getMetaData();
                            //     columnsNumber = rsmd.getColumnCount();
                            //     for (int i = 1; i <= columnsNumber; i++){
                            //         row.put(rsmd.getColumnName(i), rs.getString(i));
                            //     }
                            //     Rows.add(row);
                            // }

                        
                            model.addObject("airports", Rows);

                            rs = statement.executeQuery("select * from aircrafts");
                            rs.beforeFirst();
                            Rows = multiAL(rs);
                            // while(rs.next()){
                            //     row = new Hashtable();
                            //     rsmd = rs.getMetaData();
                            //     columnsNumber = rsmd.getColumnCount();
                            //     for (int i = 1; i <= columnsNumber; i++){
                            //         row.put(rsmd.getColumnName(i), rs.getString(i));
                            //     }
                            //     Rows.add(row);
                            // }
                            model.addObject("aircrafts", Rows);
					        connection.close();
                            return model;
                        }
                        
                    }

            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }
    // MAX LEGRAND
    // edit flights logic
    @RequestMapping("/flightseditconf")
public ModelAndView flightseditconf(
    @RequestParam("number") String number, @RequestParam("prevnumber") String prevnumber, @RequestParam("depart") String depart,
    @RequestParam("arrive") String arrive, @RequestParam("farefirst") int firstclass,
    @RequestParam("fareecon") int econclass, @RequestParam("type") String type,
    @RequestParam("aircraft") String aircraft,@RequestParam("airport_to") String ato,@RequestParam("airport_from") String afrom,
    HttpSession session
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
                            statement.executeUpdate("UPDATE flights set number=\""+number+"\", depart_time=\""+depart+"\", arrive_time=\""+arrive+"\", fare_first="+firstclass+", fare_econ="+econclass+", type=\""+type+"\", aircraft=\""+aircraft+"\", airport_to=\""+ato+"\", airport_from=\""+afrom+"\" where number=\""+prevnumber+"\";");
                        }
                    }
                   else{
                                   
                        model =  new ModelAndView("redirect:reptools");
                        statement.executeUpdate("UPDATE flights set number=\""+number+"\", depart_time=\""+depart+"\", arrive_time=\""+arrive+"\", fare_first="+firstclass+", fare_econ="+econclass+", type=\""+type+"\", aircraft=\""+aircraft+"\", airport_to=\""+ato+"\", airport_from=\""+afrom+"\" where number=\""+prevnumber+"\";");
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
            ArrayList Rows = multiAL(rs);
            // while(rs.next()){
            //     Dictionary row = new Hashtable();
            //     ResultSetMetaData rsmd = rs.getMetaData();
            //     int columnsNumber = rsmd.getColumnCount();
            //     for (int i = 1; i <= columnsNumber; i++){
            //         row.put(rsmd.getColumnName(i), rs.getString(i));
            //     }
            //     Rows.add(row);
            // }
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentdate = dateFormat.format(date);
	    // System.out.println(dateFormat.format(date))
        if (userid != null){
                if (isrep(userid)){
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet rs = null;
                    ModelAndView model;
                    
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                        statement = connection.createStatement();
                        rs = statement.executeQuery("SELECT * FROM users where id="+cid);
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows == 0){
                                model =  new ModelAndView("userdne");
                                return model;
                        }
                        rs = statement.executeQuery("SELECT * FROM flights where number=\""+flightnum+"\"");
                        rs.last();
                        rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows == 0){
                                model =  new ModelAndView("flightdne");
                                return model;
                        }

                        rs = statement.executeQuery("SELECT * FROM reservations where cid="+cid+" and flightnum=\""+flightnum+"\"");
                        rs.last();
                        rows = rs.getRow();
                        rs.beforeFirst();
                        if (rows > 0){
                                model =  new ModelAndView("resexists");
                        }
                        else{
                            statement.executeUpdate("INSERT into reservations(cid, flightnum, num_first_class, num_economy, date_made) VALUES("+cid+", \""+flightnum+"\", "+firstclass+", "+economy+", \""+currentdate+"\");");
                            // String query = "INSERT into reservations(cid, flightnum, num_first_class, num_economy, date_made) VALUES("+cid+", \""+flightnum+"\", "+firstclass+", "+economy+", \""+currentdate+"\");";

                            model =  new ModelAndView("redirect:represerve");
                            // connection.close();
                            // model = new ModelAndView("testpage", "q", query);
                            return model;
                            
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

    //MAX LEGRAND
    // reservations edit
    @RequestMapping("/resedit")
	public ModelAndView resedit(@RequestParam(name="id") String id, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        if (id == null || id.equals("") || id.equals(" ")){
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
                        rs = statement.executeQuery("select * from reservations where id=\""+id+"\"");
                        rs.last();
                        int rows = rs.getRow();
                        rs.first();
                        if (rows > 0){
                            ArrayList Rows = singleAL(rs);
                       
                            // Dictionary row = new Hashtable();
                            // ResultSetMetaData rsmd = rs.getMetaData();
                            // int columnsNumber = rsmd.getColumnCount();
                            
                            // for (int i = 1; i <= columnsNumber; i++){
                            //     row.put(rsmd.getColumnName(i), rs.getString(i));
                            // }
                            // Rows.add(row);
                            connection.close();
                            ModelAndView model = new ModelAndView("resedit", "rs", Rows);
                            return model;
                        }
                        
                    }

            }

        
        ModelAndView model =  new ModelAndView("index");
		return model;
    }

    // MAX LEGRAND
    // edit reservation logic
    @RequestMapping("/reseditconf")
public ModelAndView reseditconf(
    @RequestParam("flightnum") String flightnum, @RequestParam("cid") String cid,  @RequestParam("id") String id, @RequestParam("first_class") String firstclass, @RequestParam("economy") String economy, HttpSession session
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
                    
                                   
                    model =  new ModelAndView("redirect:represerve");
                    statement.executeUpdate("UPDATE reservations set num_first_class="+ firstclass +", num_economy="+economy+" where id = "+id+";");
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
// waiting list page
@RequestMapping("/repwaitinglist")
public ModelAndView repwaitinglist(HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
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
                rs = statement.executeQuery("select * from flights join aircrafts on flights.aircraft = aircrafts.id order by number");
                rs.beforeFirst();
                ArrayList Rows = multiAL(rs);
                // while(rs.next()){
                //     Dictionary row = new Hashtable();
                //     ResultSetMetaData rsmd = rs.getMetaData();
                //     int columnsNumber = rsmd.getColumnCount();
                //     for (int i = 1; i <= columnsNumber; i++){
                //         row.put(rsmd.getColumnName(i), rs.getString(i));
                //     }
                //     Rows.add(row);
                // }
                ModelAndView model = new ModelAndView("repwaitinglist", "rs", Rows);
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
// waiting list page
@RequestMapping("/waitinglist")
public ModelAndView waitinglist(@RequestParam("number") String flightnum, HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
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
                rs = statement.executeQuery("select * from waitinglist join flights on flights.number=waitinglist.flightnum join users on users.id = waitinglist.cid where flightnum=\""+flightnum+"\" order by cid");
                rs.beforeFirst();

                ArrayList Rows = multiAL(rs);
                // while(rs.next()){
                //     Dictionary row = new Hashtable();
                //     ResultSetMetaData rsmd = rs.getMetaData();
                //     int columnsNumber = rsmd.getColumnCount();
                //     for (int i = 1; i <= columnsNumber; i++){
                //         row.put(rsmd.getColumnName(i), rs.getString(i));
                //     }
                //     Rows.add(row);

                // }
                ModelAndView model = new ModelAndView("waitinglist", "rs", Rows);
                model.addObject("num", flightnum);
                connection.close();
                return model;
            }
            ModelAndView model =  new ModelAndView("redirect:/");
            return model;
            
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }
    //Peter Marchese
    //Creates page for sales report (Admin function)
    @RequestMapping("/salesreport")
    public ModelAndView salesreport(@RequestParam(name = "month", required = false, defaultValue = "NONE") String m, @RequestParam(name = "year", required = false, defaultValue = "NONE") String y, HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if(isadmin(userid)){
            if(!m.equals("NONE") && !y.equals("NONE")){
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT * FROM reservations join flights on reservations.flightnum = flights.number where year(date_made)='"+y+"' and month(date_made)='"+m+"'");
                rs.beforeFirst();
                ArrayList rows = multiAL(rs);
                connection.close();
                ModelAndView model =  new ModelAndView("salesreport", "rs", rows);
                return model;
            }
            ModelAndView model =  new ModelAndView("salesreport");
            return model;
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }
    //Peter Marchese
    //Creates page for obtaining reservation list from either customer name or flight number.
    @RequestMapping("/reslist")
    public ModelAndView reslist(@RequestParam(name = "name", required = false, defaultValue = "NONE") String name, @RequestParam(name = "flightNumber", required = false, defaultValue = "NONE") String flightNumber, HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if(isadmin(userid)){
            if(!name.equals("NONE") || !flightNumber.equals("NONE")){
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                if(name.equals("NONE")){
                    //For finding reservations by flightID
                    //I gotta figure the SQL code out
                    rs = statement.executeQuery("SELECT * FROM reservations join flights on reservations.flightnum = flights.number");
                } 
                else {
                    //For finding reservations based off of customer name
                    //gotta figure out the SQL for this too
                    rs = statement.executeQuery("SELECT * FROM reservations join flights on reservations.flightnum = flights.number");
                }
                rs.beforeFirst();
                ArrayList rows = multiAL(rs);
                connection.close();
                ModelAndView model =  new ModelAndView("reslist");
                return model;
            }
            ModelAndView model =  new ModelAndView("reslist");
            return model;
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }
    //Peter Marchese
    @RequestMapping("/revgen")
    public ModelAndView revgen(@RequestParam(name = "flight", required = false, defaultValue = "NONE") String flight,@RequestParam(name = "airline", required = false, defaultValue = "NONE") String airline,@RequestParam(name = "customer", required = false, defaultValue = "NONE") String customer,
    HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if(isadmin(userid)){
            if(!flight.equals("NONE") || !airline.equals("NONE") || !customer.equals("NONE")){
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                //For flight
                if(!flight.equals("NONE") && customer.equals("NONE") && airline.equals("NONE")){
                    ////////////////////////////////////////
                    rs = statement.executeQuery("SELECT * FROM reservations join flights on reservations.flightnum = flights.number");
                } 
                //For customer
                else if(flight.equals("NONE") && !customer.equals("NONE") && airline.equals("NONE")){
                    ///////////////////////////////////////
                    rs = statement.executeQuery("SELECT * FROM reservations join flights on reservations.flightnum = flights.number");
                }
                //For airline
                else{
                    ///////////////////////////////////////
                    rs = statement.executeQuery("SELECT * FROM reservations join flights on reservations.flightnum = flights.number");
                }
                rs.beforeFirst();
                ArrayList rows = multiAL(rs);
                connection.close();
                ModelAndView model =  new ModelAndView("reslist");
                return model;
            }
            ModelAndView model =  new ModelAndView("revgen");
            return model;
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }
    //Peter Marchese
    @RequestMapping("/customerrev")
    public ModelAndView customerrev(HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if(isadmin(userid)){
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
            statement = connection.createStatement();
            //Gotta figure out SQL path
            rs = statement.executeQuery("");
            rs.beforeFirst();
            ArrayList rows = multiAL(rs);
            connection.close();
            ModelAndView model =  new ModelAndView("customerrev");
            return model;
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }
    //Peter Marchese
    @RequestMapping("/ticketssold")
    public ModelAndView ticketssold(HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if(isadmin(userid)){
            Connection connection = null;
            Statement statement = null;
            ResultSet rs = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
            statement = connection.createStatement();
            //Gotta figure out SQL path
            rs = statement.executeQuery("");
            rs.beforeFirst();
            ArrayList rows = multiAL(rs);
            connection.close();
            ModelAndView model =  new ModelAndView("ticketssold");
            return model;
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }
    //Peter Marchese
    @RequestMapping("/allflights")
    public ModelAndView allflights(@RequestParam(name = "airport", required = false, defaultValue = "NONE") String airport, HttpSession session)throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        String connectionURL = geturl();
        String userid = (String)session.getAttribute("ID");
        if(isadmin(userid)){
            if(!airport.equals("NONE")){
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(connectionURL, getuser(),getpass());
                statement = connection.createStatement();
                ///////////////////////////////////
                rs = statement.executeQuery("");
                rs.beforeFirst();
                ArrayList rows = multiAL(rs);
                connection.close();
                ModelAndView model =  new ModelAndView("salesreport", "rs", rows);
                return model;
            }
            ModelAndView model =  new ModelAndView("allflights");
            return model;
        }
        ModelAndView model =  new ModelAndView("index");
        return model;
    }

    @RequestMapping("/testpage")
    public ModelAndView testpage(){
        ModelAndView model = new ModelAndView("testpage");
        return model;
    }

}