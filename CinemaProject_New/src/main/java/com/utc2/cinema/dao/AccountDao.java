package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Account;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class AccountDao implements DaoInterface<Account>
{
    @Override
    public int insertData(Account target) {
        return 0;
    }
    @Override
    public int updateData(Account target) {
        return 0;
    }
    @Override
    public int deleteData(Account target) {
        return 0;
    }

    @Override
    public Account getData(String email, String password)
    {
        Connection connect = Database.getConnection();
        Account account = null;
        try
        {
            PreparedStatement st = connect.prepareStatement("Select * From account WHERE email = ? AND password = ?");
            st.setString(1 ,email);
            st.setString(2,password);
            ResultSet rs = st.executeQuery();
            while(rs.next())
            {
                 account = new Account(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("accountstatus"),
                        rs.getInt("roleid")
                );
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Database.closeConnection(connect);
        return account;
    }

    @Override
    public List<Account> getAllData() {
        return null;
    }
}
