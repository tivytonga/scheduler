/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author Tivinia Tonga
 */
public class Customer 
{
    // Common Data
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    // Customer Table in DB
    private int addressId;
    private int customerId;
    private String customerName;
    private boolean active;
    
    // Address Table in DB
    private int cityId;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    
    // City Table in DB
    private String city;
    private int countryId;
    
    // Country Table in DB
    private String country;
    
    public Customer(){
        this.active = false;
        this.address = "";
        this.address2 = "";
        this.postalCode = "";
        this.phone = "";
        this.city = "";
    }
    
    public void setCity(String city){
        this.city = city;
    }
    
    public String getCity(){
        return this.city;
    }
    
    public void setCountryId(int countryId){
        this.countryId = countryId;
    }
    
    public int getCountryId(){
        return this.countryId;
    }
    
    public void setCountry(String country){
        this.country = country;
    }
    
    public String getCountry(){
        return this.country;
    }
    
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    
    public int getCityId(){
        return this.cityId;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getAddress(){
        return this.address;
    }
    
    public void setAddress2(String address2){
        this.address2 = address2;
    }
    
    public String getAddress2(){
        return this.address2;
    }
    
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    
    public String getPostalCode(){
        return this.postalCode;
    }
    
    public void setPhone(String phone){
        this.phone = phone;
    }
    
    public String getPhone(){
        return this.phone;
    }
    
    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }
    
    public int getCustomerId(){
        return this.customerId;
    }
    
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }
    
    public String getCustomerName(){
        return this.customerName;
    }
    
    public void setAddressId(int addressId){
        this.addressId = addressId;
    }
    
    public int getAddressId(){
        return this.addressId;
    }
    
    public void setActive(boolean active){
        this.active = active;
    }
    
    public boolean getActive(){
        return this.active;
    }
    
    public void setCreateDate(Timestamp createDate){
        this.createDate = createDate;
    }
    
    public Timestamp getCreateDate(){
        return this.createDate;
    }
    
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    
    public String getCreatedBy(){
        return this.createdBy;
    }
    
    public void setLastUpdate(Timestamp lastUpdate){
        this.lastUpdate = lastUpdate;
    }
    
    public Timestamp getLastUpdate(){
        return this.lastUpdate;
    }
    
    public void setLastUpdateBy(String lastUpdatedBy){
        this.lastUpdateBy = lastUpdatedBy;
    }
    
    public String getLastUpdateBy(){
        return this.lastUpdateBy;
    }
    
    /*
     *  Pull all data from Customer Table in DB
     */
    public void extractCustomerData(ResultSet rs){
        try{
            this.setCustomerId(rs.getInt("customerId"));
            this.setCustomerName(rs.getString("customerName"));
            this.setAddressId(rs.getInt("addressId"));
            this.setActive(rs.getBoolean("active"));
            this.setCreateDate(rs.getTimestamp("createDate"));
            this.setCreatedBy(rs.getString("createdBy"));
            this.setLastUpdate(rs.getTimestamp("lastUpdate"));
            this.setLastUpdateBy(rs.getString("lastUpdateBy"));
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Pull all data from Customer Table in DB
     */
    public void extractBasicCustomerData(ResultSet rs){
        try{
            this.setCustomerName(rs.getString("customerName"));
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Pull all data from Address Table in DB
     */
    public void extractAddressData(ResultSet rs){
        try{
            this.setAddressId(rs.getInt("addressId"));
            this.setAddress(rs.getString("address"));
            this.setAddress2(rs.getString("address2"));
            this.setCityId(rs.getInt("cityId"));
            this.setPostalCode(rs.getString("postalCode"));
            this.setPhone(rs.getString("phone"));
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Pull all data from Address Table in DB
     */
    public void extractBasicAddressData(ResultSet rs){
        try{
            this.setAddress(rs.getString("address"));
            this.setPhone(rs.getString("phone"));
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Pull all data from City Table in DB
     */
    public void extractCityData(ResultSet rs){
        try{
            this.setCity(rs.getString("city"));
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Pull all data from Country Table in DB
     */
    public void extractCountryData(ResultSet rs){
        try{
            this.setCountry(rs.getString("country"));
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
   
    @Override
    public String toString(){
        return this.getCustomerName();
    }
}
