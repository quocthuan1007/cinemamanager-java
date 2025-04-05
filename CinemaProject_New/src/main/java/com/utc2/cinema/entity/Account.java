package com.utc2.cinema.entity;

public class Account {
    private int id;
    private String email;
    private String password;
    private String accountStatus;
    private int roleId;
    public Account(int id, String email, String password, String accountStatus, int roleId)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accountStatus = accountStatus;
        this.roleId = roleId;
    }
    public Account() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
