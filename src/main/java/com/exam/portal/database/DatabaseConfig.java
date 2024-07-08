package com.exam.portal.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Exam_Portal?" +
            "characterEncoding=utf-8&allowMultiQueries=true";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "Quark@4275";

    private static Connection connection;

    private static Connection makeConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //returns the singleton object of connection.
    public static Connection getConnection(){
        if (connection == null)
            connection = makeConnection();
        return connection;
    }
}
