package com.utc2.cinema.config;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {
    public static Connection getConnection()
    {
        Connection c= null;
        try {
            Properties properties = new Properties();

            FileInputStream input = new FileInputStream("src\\main\\resources\\database.properties");
            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            System.out.println("URL: " + url);
            System.out.println("Username: " + user);
            System.out.println("Password: " + password);
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Kết nối thất bại");
        }
        return c;
    }
    public static void closeConnection(Connection c)
    {
        try {
            if(c != null)
            {
                c.close();
                System.out.println("Đóng kết nối");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
