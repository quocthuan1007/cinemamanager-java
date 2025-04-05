package com.utc2.cinema.service;

import com.utc2.cinema.dao.AccountDao;
import com.utc2.cinema.model.entity.Account;


public class AccountService
{
    private AccountDao account = new AccountDao();
    public Account loginAccount(String username, String password)
    {
        Account findAccount = account.getData(username, password);
        if(findAccount == null)
            return null;
        return findAccount;
    }
}
