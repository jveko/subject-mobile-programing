package com.example.tugas10.models;

import java.util.Date;

public class UserSessionModel {
    public UserSessionModel(int id, Date login, Date logout) {
        this.id = id;
        this.login = login;
        this.logout = logout;
    }
    public int id;
    public Date login;
    public Date logout;
}
