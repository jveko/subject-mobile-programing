package com.example.tugas03.helper;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Watcher {
    private Date dateLogin, dateLogout;
    private long diffInMillis;
    private static Watcher sharedInstance;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static Watcher sharedInstance() {
        if (sharedInstance == null) {
            Watcher state = new Watcher();
            sharedInstance = state;
        }
        return sharedInstance;
    }
    public void setDateLogin(){
        dateLogin = new Date();
    }

    public void setDateLogout(){
        dateLogout = new Date();
    }

    public void clearDateLogout(){
        dateLogout = null;
    }

    public boolean setDifference(){
        if(dateLogout == null || dateLogin == null){
            return false;
        }
        diffInMillis = Math.abs(dateLogout.getTime() - dateLogin.getTime());
        return true;
    }

    public String getLastLogin(){
        if(dateLogin == null){
            return "";
        }
        return sdf.format(dateLogin);
    }

    public String getLastLogout(){
        if(dateLogout == null){
            return "";
        }
        return sdf.format(dateLogout);
    }

    public String getDuration(){
        if(setDifference()){
            return String.format("%02d min, %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(diffInMillis),
                    TimeUnit.MILLISECONDS.toSeconds(diffInMillis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffInMillis))
            );
        }
        return "";
    }

}
