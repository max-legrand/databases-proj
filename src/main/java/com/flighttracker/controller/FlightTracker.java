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

    public static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); // hex encoding
    }

    
    @RequestMapping("/login")
    public ModelAndView login(HttpServletResponse response) {
        ModelAndView model = new ModelAndView("login");
        return model;
    }

    @RequestMapping("/invalidlogin")
    public ModelAndView invalidlogin() {
        ModelAndView model = new ModelAndView("invalidlogin");
        return model;
    }


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


    @RequestMapping("/signupconf")
    public ModelAndView signupconf(@RequestParam("password") String password,
            @RequestParam("confpassword") String confpassword, @RequestParam("username") String username)
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
                model =  new ModelAndView("redirect:exists");
            }
            else{
                statement.executeUpdate("INSERT into users(username, password) VALUES(\""+username+"\", \""+password+"\");");
                model =  new ModelAndView("redirect:/");
            }
            
        }
        connection.close();
		return model;
    }
    
    @RequestMapping("/signup")
	public ModelAndView signup() {
		ModelAndView model =  new ModelAndView("signup");
		return model;
    }

    @RequestMapping("/invalid")
	public ModelAndView invalid() {
		ModelAndView model =  new ModelAndView("invalid");
		return model;
    }

    @RequestMapping("/exists")
	public ModelAndView exists() {
		ModelAndView model =  new ModelAndView("exists");
		return model;
    }
    
    
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
	

@RequestMapping("/admintools")
	public ModelAndView admintools(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{

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

                if (Integer.parseInt(rs.getString("admin")) == 1){
                    Dictionary adminuser = new Hashtable();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    
                    for (int i = 1; i <= columnsNumber; i++){
                        adminuser.put(rsmd.getColumnName(i), rs.getString(i));
                    }

                    rs = statement.executeQuery("select * from users where admin=0");
                    rs.beforeFirst();
                    ArrayList Rows = new ArrayList();
                    while(rs.next()){
                        Dictionary row = new Hashtable();
                        rsmd = rs.getMetaData();
                        columnsNumber = rsmd.getColumnCount();
                        for (int i = 1; i <= columnsNumber; i++){
                            row.put(rsmd.getColumnName(i), rs.getString(i));
                        }
                        Rows.add(row);
                    }
                    connection.close();
                    ModelAndView model = new ModelAndView("admintools", "rs", Rows);
                    model.addObject("admin", adminuser);
                    return model;
                }
                   
                   
                
                connection.close();
                
                ModelAndView model = new ModelAndView("redirect:feed");

                return model;
            }
            connection.close();

        }
        ModelAndView model =  new ModelAndView("index");
		return model;
	}



@RequestMapping("/adminedit")
	public ModelAndView adminedit(@RequestParam(name="selectedid") String selectedid, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, SocketException, FileNotFoundException, IOException{
        
        if (selectedid == null || selectedid.equals("") || selectedid.equals(" ")){
            ModelAndView model =  new ModelAndView("redirect:admintools");
            return model;
        }



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

                    if (Integer.parseInt(rs.getString("admin")) == 1){
                        Dictionary adminuser = new Hashtable();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        
                        for (int i = 1; i <= columnsNumber; i++){
                            adminuser.put(rsmd.getColumnName(i), rs.getString(i));
                        }
                        rs = statement.executeQuery("select * from users where id="+selectedid);
                        rs.last();
                        rows = rs.getRow();
                        rs.first();
                        if (rows > 0){
                            if (rs.getString("admin").equals("1")){
                                ModelAndView model =  new ModelAndView("redirect:admintools");
                                connection.close();
                                return model;
                            }
                            ArrayList Rows = new ArrayList();
                       
                            Dictionary row = new Hashtable();
                            rsmd = rs.getMetaData();
                            columnsNumber = rsmd.getColumnCount();
                            
                            for (int i = 1; i <= columnsNumber; i++){
                                row.put(rsmd.getColumnName(i), rs.getString(i));
                            }
                            Rows.add(row);
                            connection.close();
                            ModelAndView model = new ModelAndView("adminedit", "rs", Rows);
                            model.addObject("admin", adminuser);
                            return model;
                        }
                        
                    }
                   
                    connection.close();
                    ModelAndView model =  new ModelAndView("redirect:admintools");
                    return model;
            }
            connection.close();

        }
        ModelAndView model =  new ModelAndView("index");
        model.addObject("conn", geturl());
		return model;
	}
	
}

