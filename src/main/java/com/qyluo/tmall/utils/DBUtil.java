package com.qyluo.tmall.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by qy_lu on 2017/5/1.
 */
public class DBUtil {
    static String ip = "127.0.0.1";
    static int port = 3306;
    static String database = "tmall";
    static String encoding = "UTF-8";
    static String loginName = "qyluo";
    static String passWord = "123456";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s", ip, port, database, encoding);
        return DriverManager.getConnection(url, loginName, passWord);
    }

    public static void main(String args[]) throws SQLException {
        System.out.println(getConnection());
    }
}
