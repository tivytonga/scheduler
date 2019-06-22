/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author tivyt
 */
public class DBConnection {
    
    private static Connection conn;
    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://52.206.157.109:3306/U05T5t";
    private final static String DBUSER = "U05T5t";
    private final static String DBPASS = "53688598897";
    
    /*
     *  Constructor
     */
    public DBConnection()
    {
        
    }
    
    /*
     *  Setup the connection
     */
    public static void init()
    {
        try
        {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class Not Found. Add mysql library to configuration");
            cnfe.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static Connection getConn()
    {
        return conn;
    }
    
    public static void closeConn()
    {
        try
        {
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Connection closed.");
        }
    }
    
    public static ArrayList<Appointment> getAllAppointments(String statement){
        ArrayList<Appointment> allAppts = new ArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                Appointment appt = new Appointment();
                appt.setAppointmentId(rs.getInt("appointmentId"));
                appt.setTitle(rs.getString("title"));
                appt.setDescription(rs.getString("description"));
                appt.setLocation(rs.getString("location"));
                appt.setContact(rs.getString("contact"));
                appt.setType(rs.getString("type"));
                appt.setUrl(rs.getString("url"));
                appt.setStart(rs.getTimestamp("start"));
                appt.setEnd(rs.getTimestamp("end"));
                appt.setCreateDate(rs.getTimestamp("createDate"));
                appt.setCreatedBy(rs.getString("createdBy"));
                appt.setLastUpdate(rs.getTimestamp("lastUpdate"));
                appt.setLastUpdateBy(rs.getString("lastUpdateBy"));
                appt.setUserId(rs.getInt("userId"));
                appt.setCustomerId(rs.getInt("customerId"));
                allAppts.add(appt);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return allAppts;
    }
    
    public static Appointment getSelectedAppointment(String statement, int appointmentId){
        Appointment appt = new Appointment();
        try{
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                appt.setAppointmentId(rs.getInt("appointmentId"));
                appt.setTitle(rs.getString("title"));
                appt.setDescription(rs.getString("description"));
                appt.setLocation(rs.getString("location"));
                appt.setContact(rs.getString("contact"));
                appt.setType(rs.getString("type"));
                appt.setUrl(rs.getString("url"));
                appt.setStart(rs.getTimestamp("start"));
                appt.setEnd(rs.getTimestamp("end"));
                appt.setCreateDate(rs.getTimestamp("createDate"));
                appt.setCreatedBy(rs.getString("createdBy"));
                appt.setLastUpdate(rs.getTimestamp("lastUpdate"));
                appt.setLastUpdateBy(rs.getString("lastUpdateBy"));
                appt.setUserId(rs.getInt("userId"));
                appt.setCustomerId(rs.getInt("customerId"));
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return appt;
    }
    
    public static ArrayList<Customer> getAllCustomers(String statement){
        ArrayList<Customer> customers = new ArrayList();
        try{
            PreparedStatement ps = DBConnection.getConn().prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                Customer cust = new Customer();
                cust.setCustomerId(rs.getInt("customerId"));
                cust.setCustomerName(rs.getString("customerName"));
                cust.setAddressId(rs.getInt("addressId"));
                cust.setCreateDate(rs.getTimestamp("createDate"));
                cust.setCreatedBy(rs.getString("createdBy"));
                cust.setLastUpdate(rs.getTimestamp("lastUpdate"));
                cust.setLastUpdateBy(rs.getString("lastUpdateBy"));
                customers.add(cust);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return customers;
    }
    
    public static void addCountryDB(Customer customer){
        try{
            String stmt = "INSERT INTO U05T5t.country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setString(1, customer.getCountry());
            ps.setTimestamp(2, customer.getCreateDate());
            ps.setString(3, customer.getCreatedBy());
            ps.setTimestamp(4, customer.getLastUpdate());
            ps.setString(5, customer.getLastUpdateBy());
            ps.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    public static void addCityDB(Customer customer){
        try{
            String stmt = "INSERT INTO U05T5t.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setString(1, customer.getCity());
            ps.setInt(2, customer.getCountryId());
            ps.setTimestamp(3, customer.getCreateDate());
            ps.setString(4, customer.getCreatedBy());
            ps.setTimestamp(5, customer.getLastUpdate());
            ps.setString(6, customer.getLastUpdateBy());
            ps.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    public static void addAddressDB(Customer customer){
        try{
            String stmt = "INSERT INTO U05T5t.address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setString(1, customer.getAddress());
            ps.setString(2, customer.getAddress2());
            ps.setInt(3, customer.getCityId());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhone());
            ps.setTimestamp(6, customer.getCreateDate());
            ps.setString(7, customer.getCreatedBy());
            ps.setTimestamp(8, customer.getLastUpdate());
            ps.setString(9, customer.getLastUpdateBy());
            ps.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    public static void updateCustomerDB(Customer customer){
        try{
            String stmt = "UPDATE U05T5t.customer SET customerName=?, addressId=? WHERE customerId=?";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(stmt);
            ps.setString(1, customer.getCustomerName());
            ps.setInt(2, customer.getAddressId());
            ps.setInt(3, customer.getCustomerId());
            ps.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    public static ArrayList<Customer> getCountryDB(){
        ArrayList<Customer> countries = new ArrayList();
        try{
            String stmt = "SELECT * FROM U05T5t.country";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setCountry(rs.getString("country"));
                customer.setCountryId(rs.getInt("countryId"));
                countries.add(customer);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return countries;
    }
    
    public static ArrayList<Customer> getCityDB(){
        ArrayList<Customer> cities = new ArrayList();
        try{
            String stmt = "SELECT * FROM U05T5t.city";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setCity(rs.getString("city"));
                customer.setCityId(rs.getInt("cityId"));
                customer.setCountryId(rs.getInt("countryId"));
                cities.add(customer);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return cities;
    }
    
    public static ArrayList<Customer> getAddressDB(){
        ArrayList<Customer> address = new ArrayList();
        try{
            String stmt = "SELECT * FROM U05T5t.address";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setAddress(rs.getString("address"));
                customer.setAddress2(rs.getString("address2"));
                customer.setPhone(rs.getString("phone"));
                customer.setPostalCode(rs.getString("postalCode"));
                customer.setAddressId(rs.getInt("addressId"));
                customer.setCityId(rs.getInt("cityId"));
                address.add(customer);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return address;
    }
}
