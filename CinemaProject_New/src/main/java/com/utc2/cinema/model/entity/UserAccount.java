package com.utc2.cinema.model.entity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class UserAccount {
    private User user;
    private Account account;

    public UserAccount(User user, Account account) {
        this.user = user;
        this.account = account;
    }

    // Getter cho User
    public String getName() {
        return user.getName();
    }

    public String getPhoneNumber() {
        return user.getPhone();
    }

    public String getAddress() {
        return user.getAddress();
    }

    public String getGender() {
        return user.isGender() ? "Nam" : "Nữ";
    }

    public User getUser() {
        return user;
    }

    public Account getAccount() {
        return account;
    }

    public Date getBirth() {
        return user.getBirth();
    }

    public int getUserId() {
        return user.getId();
    }

    public int getAccountId() {
        return user.getAccountId();  // hoặc account.getId();
    }

    // Getter cho Account
    public String getEmail() {
        return account.getEmail();
    }

    public String getAccountStatus() {
        return account.getAccountStatus();
    }

    public int getRoleId() {
        return account.getRoleId();
    }

    public String getPassword() {
        return account.getPassword();
    }
}
