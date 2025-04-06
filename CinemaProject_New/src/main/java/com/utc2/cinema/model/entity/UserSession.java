package com.utc2.cinema.model.entity;

import com.utc2.cinema.controller.MainController;

public class UserSession
{
    private static UserSession instance = null;
    public  String email;
    private String password;

    public static UserSession getInstance() {
        return instance;
    }

    public String getEmail() {
        return email;
    }
    public void cleanUserSession()
    {
        this.email = null;
        this.password = null;
        instance = null;
    }
    public String getPassword() {
        return password;
    }


    private UserSession(String email, String password)
    {
        this.email = email;
        this.password = password;
    }
    public static void createUserSession(String email, String password)
    {
        if(instance == null)
        {
            instance = new UserSession(email, password);
        }
    }

}
