package com.utc2.cinema.model.entity;

import com.utc2.cinema.controller.MainController;

public class UserSession
{
    private static UserSession instance = null;
    private int userId;
    private  String email;
    private String password;
    private String accountStatus;
    private int roleId;
    public int getRoleId() {
        return roleId;
    }

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
        this.accountStatus = null;
        this.userId = 0;
        this.roleId = 0;
        instance = null;
    }
    public String getPassword() {
        return password;
    }


    private UserSession(int userId, String email, String password,String accountStatus,int roleId)
    {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.accountStatus = "ONLINE";
        this.userId = userId;
    }
    public static void createUserSession(int userId,String email, String password,String status, int roleId)
    {
        if(instance == null)
        {
            instance = new UserSession(userId, email, password, status,roleId);
        }
    }

}
