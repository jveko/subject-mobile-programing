package com.example.tugas15.models;

import android.location.Address;

import java.util.Date;

public class UserLocationModel {
    public UserLocationModel(int id,int userId,  double latitude, double longitude, Date createdAt){
        this.Id = id;
        this.UserId = userId;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.CreatedAt = createdAt;
    }
    public int Id;
    public int UserId;
    public double Latitude;
    public double Longitude;
    public Address Address;
    public Date CreatedAt;
}
