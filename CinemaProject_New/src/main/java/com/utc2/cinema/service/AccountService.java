package com.utc2.cinema.service;

import com.utc2.cinema.dao.AccountDao;
import com.utc2.cinema.model.entity.Account;

import java.util.List;

public class AccountService
{
    public static Account findAccount(String email)
    {
        Account findAccount = new AccountDao().getDataByEmail(email);
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
    public static boolean updateAccount(Account account, int option)
    {
        return new AccountDao().updateData(account, option) > 0;
    }
    public static List<Account> getAllAccounts() {
        return new AccountDao().getAllData();
    }
    public static int deleteAccount(Account target) {
        return AccountDao.deleteAccount(target);
    }
    public static String getPassword(String email)
    {
        AccountDao a = new AccountDao();
        return a.getPassword(email);
    }

}
