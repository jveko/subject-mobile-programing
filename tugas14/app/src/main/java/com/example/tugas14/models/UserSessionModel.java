package com.example.tugas14.models;

import java.util.Date;

public class UserSessionModel {
    public UserSessionModel(){}
    public UserSessionModel(int id, Date login,Date logout){
        this.Id = id;
        this.Login = login;
        this.Logout = logout;
    }
    public int Id;
    public Date Login;
    public Date Logout;
}
