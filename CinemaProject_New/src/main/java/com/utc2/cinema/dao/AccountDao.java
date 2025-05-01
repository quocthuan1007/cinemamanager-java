package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements DaoInterface<Account>
{
    @Override
    public int insertData(Account target)
    {
        try {
            Connection connect = Database.getConnection();
            PreparedStatement st = connect.prepareStatement(
                    "INSERT INTO Account (Email, Password, AccountStatus, RoleId) \n" +
                            "VALUES (?,?,?, ?)");
            st.setString(1, target.getEmail());
            st.setString(2, target.getPassword());
            st.setString(3, target.getAccountStatus());
            st.setInt(4, target.getRoleId());
            return st.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }

    }
    @Override
    public int deleteData(Account target)
    {
        return 0;
    }

    @Override
    public int updateData(Account target, int option)
    {
        int rowsAffected = 0;
        try {
            Connection connect = Database.getConnection();
            PreparedStatement st = null;

            switch (option) {
                case 1:
                    st = connect.prepareStatement("UPDATE account SET email = ? WHERE id = ?");
                    st.setString(1, target.getEmail());
                    st.setInt(2, target.getId());
                    break;
                case 2:
                    st = connect.prepareStatement("UPDATE account SET password = ? WHERE id = ?");
                    st.setString(1, target.getPassword());
                    st.setInt(2, target.getId());
                    break;
                case 3:
                    st = connect.prepareStatement("UPDATE account SET accountStatus = ? WHERE id = ?");
                    st.setString(1, target.getAccountStatus());
                    st.setInt(2, target.getId());
                    break;
                case 4:
                    st = connect.prepareStatement("UPDATE account SET roleId = ? WHERE id = ?");
                    st.setInt(1, target.getRoleId());
                    st.setInt(2, target.getId());
                    break;
                default:
                    System.out.println("Tùy chọn không hợp lệ!");
                    Database.closeConnection(connect);
                    return 0;
            }

            rowsAffected = st.executeUpdate();
            Database.closeConnection(connect);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int deleteAccount(Account target) {
        int rowsAffected = 0;
        Connection connect = Database.getConnection();
        try {
            PreparedStatement st = connect.prepareStatement("DELETE FROM account WHERE id = ?");
            st.setInt(1, target.getId());
            rowsAffected = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connect);
        }
        return rowsAffected;
    }


    public String getEmail(String email)
    {
        Connection connect = Database.getConnection();
        try {
            PreparedStatement st = connect.prepareStatement("SELECT EMAIL FROM account WHERE EMAIL = ?");
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                return rs.getString("EMAIL");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Account getData(String email,String pass)
    {
        return null;
    }
    public Account getDataByEmail(String email)
    {
        Connection connect = Database.getConnection();
        Account account = null;
        try
        {
            PreparedStatement st = connect.prepareStatement("Select * From account WHERE email = ?");
            st.setString(1 ,email);
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
        List<Account> accounts = new ArrayList<>();
        Connection connect = Database.getConnection();

        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM account");

            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("accountstatus"),
                        rs.getInt("roleid")
                );
                accounts.add(account);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection(connect);
        }

        return accounts;
    }
    public String getPassword(String userName)
    {
        Connection connect = Database.getConnection();
        String password = null;
        try
        {
            // Truy vấn chỉ lấy mật khẩu từ username
            PreparedStatement st = connect.prepareStatement("SELECT password FROM account WHERE email = ?");
            st.setString(1, userName);  // Sử dụng userName để tìm kiếm
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                password = rs.getString("password");  // Lấy mật khẩu từ cơ sở dữ liệu
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Database.closeConnection(connect);
        return password;  // Trả về mật khẩu nếu tìm thấy, nếu không thì null
    }
}
