package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class UserDao{
    public static boolean updateUser(User target) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE User SET Name = ?, Gender = ?, Birth = ?, Phone = ?, Address = ? WHERE AccountId = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, target.getName());
            st.setInt(2, target.isGender());
            st.setDate(3, new java.sql.Date(target.getBirth().getTime()));
            st.setString(4, target.getPhone());
            st.setString(5, target.getAddress());
            st.setInt(6, target.getAccountId());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static User getDataByAccountId(int Id)
    {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("Select * From User Where AccountId = ?");
            st.setInt(1, Id);
            ResultSet rs = st.executeQuery();
            if(rs == null) return null;
            while(rs.next())
            {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                int gender = rs.getInt("Gender");
                Date birth = rs.getDate("Birth");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                int accountId = rs.getInt("AccountId");

                return new User(id,name,gender,birth,phone,address,accountId);
            }
            Database.closeConnection(conn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean insertUser(User user) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = Database.getConnection();
            String sql = "INSERT INTO User (Name, Gender, Birth, Phone, Address, AccountId) VALUES (?, ?, ?, ?, ?, ?)";
            st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            st.setString(1, user.getName());
            st.setInt(2, user.isGender());
            st.setDate(3, new java.sql.Date(user.getBirth().getTime()));
            st.setString(4, user.getPhone());
            st.setString(5, user.getAddress());
            st.setInt(6, user.getAccountId());

            int rowsInserted = st.executeUpdate();

            if (rowsInserted > 0) {
                return true;
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean deleteByAccountId(int accountId) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = Database.getConnection();
            String sql = "DELETE FROM User WHERE AccountId = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, accountId);

            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }
            Database.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
