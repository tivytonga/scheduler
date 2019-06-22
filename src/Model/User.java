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
public class User 
{
    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    
    public User(){
    }
    
    public User(int userId, String userName, String password, boolean active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public int getUserId(){
        return this.userId;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public String getUserName(){
        return this.userName;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return this.password;
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
    
    public void setLastUpdatedBy(String lastUpdatedBy){
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public String getLastUpdatedBy(){
        return this.lastUpdatedBy;
    }
    
    public User extractUserData(ResultSet rs) throws SQLException{
        User currentUser = new User();
        
        currentUser.setUserId(rs.getInt("userId"));
        currentUser.setUserName(rs.getString("userName"));
        currentUser.setPassword(rs.getString("password"));
        currentUser.setActive(rs.getBoolean("active"));
        currentUser.setCreateDate(rs.getTimestamp("createDate"));
        currentUser.setCreatedBy(rs.getString("createdBy"));
        currentUser.setLastUpdate(rs.getTimestamp("lastUpdate"));
        currentUser.setLastUpdatedBy("lastUpdatedBy");
        
        return currentUser;
    }
    
    @Override
    public String toString(){
        return this.getUserName();
    }
}
