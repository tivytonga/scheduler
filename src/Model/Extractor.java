/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

/**
 *  This class helps get specific parts of a timestamp returned
 * @author Tivinia Tonga
 */
public class Extractor {
    
    public static int extractMonth(Timestamp timestamp){
        // Break timestamp into tokens
        String[] tokens = timestamp.toString().split(" ");
        
        // tokens[0] is date stamp
        String date = tokens[0];
        
        // substring loc 5-7 is month
        String month = date.substring(5, 7);
        
        return Integer.parseInt(month);
    }
    
    public static int extractWeek(Timestamp timestamp){
        // Break timestamp into tokens
        String[] tokens = timestamp.toString().split(" ");
        
        // tokens[0] is date stamp
        String date = tokens[0];
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        int year = Integer.parseInt(date.substring(0, 4));
        
        // Create calendar instance and set to tokens[0]
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.TUESDAY);
        cal.set(year, month, day);
        
        return cal.get(Calendar.WEEK_OF_MONTH);
    }
    
    public static int extractDay(Timestamp timestamp){
        // Break timestamp into tokens
        String[] tokens = timestamp.toString().split(" ");
        
        // tokens[0] is date stamp
        String date = tokens[0];
              
        return Integer.parseInt(date.substring(8));
    }
    
    public static LocalTime extractTime(Timestamp timestamp){
        // Break timestamp into tokens
        String[] tokens = timestamp.toString().split(" ");
        
        // tokens[0] is date stamp
        String date = tokens[1];
                
        return LocalTime.parse(date);
    }
    
    public static int extractHour(Timestamp timestamp){
        //  Break timestamp into tokens
        String[] tokens = timestamp.toString().split(" ");
        String[] time = tokens[1].split(":");
        int hour = Integer.parseInt(time[0]);
        
        return hour;
        
    }
    
        /*
     *  Convert string parameter to a Timestamp
     */
    public static Timestamp getTimestamp(LocalDate date, String time, String ampmChoice){
        String dateString = date.toString();
        String newTimestamp = "";
                
        String[] tokens = time.split(":");
        
        int hour = Integer.parseInt(tokens[0]);
        int min = Integer.parseInt(tokens[1]);
        
        // special case 12:00AM = 00:00:00
        if (ampmChoice.equals("AM") && hour == 0){
            newTimestamp = dateString + " 00" + ":" + min + ":00.0";
        }
        // special case 12:00PM = 12:00:00
        else if (ampmChoice.equals("PM") && hour == 12){
            newTimestamp = dateString + " " + hour + ":" + min + ":00.0";
        }
        else if (ampmChoice.equals("AM") && hour >= 1 && hour <= 9){ // 1:00AM - 9:00AM
            newTimestamp = dateString + " 0" + hour + ":" + min + ":00.0";
        }
        else if (ampmChoice.equals("AM") && hour >= 10 && hour <= 11){ // 10:00AM - 11:00AM
            newTimestamp = dateString + " " + hour + ":" + min + ":00.0";
        }
        else if (ampmChoice.equals("PM") && hour >= 1 && hour <= 11){ // 1:00PM - 11:00PM (13:00 - 23:00)
            hour = hour + 12;
            newTimestamp = dateString + " " + hour + ":" + min + ":00.0";
        }
        else{
            newTimestamp = dateString + " " + hour + ":" + min + ":00.0";
        }
        
        return Timestamp.valueOf(newTimestamp);
    }
}
