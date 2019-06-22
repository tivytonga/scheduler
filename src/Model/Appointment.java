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
public class Appointment {
    private int appointmentId;
    private int customerId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    public Appointment(){
        
    }
    
    public void setAppointmentId(int appointmentId){
        this.appointmentId = appointmentId;
    }
    
    public int getAppointmentId(){
        return this.appointmentId;
    }
    
    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }
    
    public int getCustomerId(){
        return this.customerId;
    }
    
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public int getUserId(){
        return this.userId;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public void setLocation(String location){
        this.location = location;
    }
    
    public String getLocation(){
        return this.location;
    }
    
    public void setContact(String contact){
        this.contact = contact;
    }
    
    public String getContact(){
        return this.contact;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getType(){
        return this.type;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getUrl(){
        return this.url;
    }
    
    public void setStart(Timestamp start){
        this.start = start;
    }
    
    public Timestamp getStart(){
        return this.start;
    }
    
    public void setEnd(Timestamp end){
        this.end = end;
    }
    
    public Timestamp getEnd(){
        return this.end;
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
    
    public Appointment extractAppointmentData(ResultSet rs) throws SQLException{
        Appointment appt = new Appointment();
        
        appt.setAppointmentId(rs.getInt("appointmentId"));
        appt.setCustomerId(rs.getInt("customerId"));
        appt.setUserId(rs.getInt("userId"));
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
        appt.setLastUpdateBy(rs.getString("lastUpdatedBy"));
        
        return appt;
    }
    
}
