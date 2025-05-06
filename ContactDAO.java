package com.example.demo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:FREE";
    private static final String USER = "system";
    private static final String PASSWORD = "oracle_4U";
    //vérifie si la classe du driver Oracle est présente dans ton projet
    // (c'est-à-dire si le fichier .jar contenant le driver Oracle est bien ajouté au classpath).

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver manquant", e);
        }
    }

    public List<contact> findAll() {
        List<contact> contacts = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             //Statement stmt permet d’exécuter des requêtes SQL.
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM contact")) {

            while (rs.next()) {
                contacts.add(new contact(
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("telephone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
// Requête SQL paramétrée.
    public boolean addContact(contact c) {
        String sql = "INSERT INTO contact (prenom, nom, telephone) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getFirstName());
            pstmt.setString(2, c.getName());
            pstmt.setString(3, c.getPhone());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteContact(String phone) {
        String sql = "DELETE FROM contact WHERE telephone = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
