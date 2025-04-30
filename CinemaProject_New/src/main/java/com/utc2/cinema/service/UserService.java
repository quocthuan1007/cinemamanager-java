package com.utc2.cinema.service;

import com.utc2.cinema.dao.UserDao;
import com.utc2.cinema.model.entity.User;

public class UserService {
    public static User getUser(int Id)
    {
        return UserDao.getDataByAccountId(Id);
    }
    public static boolean updateUser(User a)
    {
        return UserDao.updateUser(a);
    }
    public static boolean insertUser(User a)
    {
        return UserDao.insertUser(a);
    }
    public static boolean deleteUserByAccountId(int accountId) {
        return UserDao.deleteByAccountId(accountId);
    }

}
