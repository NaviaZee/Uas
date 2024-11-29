/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiMySQL {
    private static final String URL = "jdbc:mysql://localhost:3306/pbo";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "AlgamerahFikoeritrin1";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
