package com.battletanks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String url = "jdbc:mysql://localhost:3306/tanques";
    private static final String user = "root";
    private static final String pass = "root";

    private static Connection conn;

    public static Connection getConexao() {
        try {
            if(conn == null || conn.isClosed()){
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                conn = DriverManager.getConnection(url, user, pass);
            }
            return conn;

        } catch (SQLException e) {
                e.printStackTrace();
                return null;
        }
    }

    public static void fecharConexao() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
