package com.utc2.cinema.service;

import com.utc2.cinema.dao.AccountDao;
import com.utc2.cinema.model.entity.Account;

public class AccountService
{
    public static Account findAccount(String email, String password)
    {
        Account findAccount = new AccountDao().getData(email, password);
        if(findAccount == null)
            return null;
        return findAccount;
    }
    public static boolean checkEmail(String email)
    {
        return new AccountDao().getEmail(email) != null;
    }
    public static boolean registerAccount(Account account)
    {
        return new AccountDao().insertData(account) > 0;
    }
}
