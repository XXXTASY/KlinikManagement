/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author XXXTASY
 */
public class Connector {
    private static final Logger LOGGER = Logger.getLogger(Connector.class.getName());

    public static Connection openConnect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/klinik_db";
            String username = "root";
            String password = "";
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver MySQL tidak ditemukan!", e);
            throw new SQLException("Driver MySQL tidak ditemukan!");
        } catch (SQLException se) {
            LOGGER.log(Level.SEVERE, "Kesalahan saat menghubungkan ke database!", se);
            throw se;
        }
    }
}