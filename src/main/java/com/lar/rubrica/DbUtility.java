package com.lar.rubrica;

import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;


public class DbUtility {
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Connection c = null;
        Dotenv dotenv = Dotenv.load();
        String password_db = dotenv.get("PASSWORD_DB");
        String username = dotenv.get("USERNAME_DB");
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/rubrica",
                username, password_db);
        System.out.println("Database opened successfully");
        return c;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static List<User> viewUser(Connection connection) throws SQLException {

        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM user LIMIT 100";
        List<User> listUser = new LinkedList<>();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            String salt = rs.getString("salt");
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            User user = new User(username, password, fname, lname);
            user.setSalt(salt);
            listUser.add(user);
        }
        rs.close();
        stmt.close();
        return listUser;
    }

    public static void insertUserPreparedStatement(Connection connection, String username, String password, String salt,
            String fname, String lname) throws SQLException {
        String sql = "INSERT INTO user(password,fname,lname,username,salt) VALUES( ?, ?, ?, ?, ?);";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, password);
        stmt.setString(2, fname);
        stmt.setString(3, lname);
        stmt.setString(4, username);
        stmt.setString(5, salt);

        stmt.executeUpdate();
        stmt.close();
    }

    public static void insertContactPreparedStatement(Connection c, String fname, String lname, String email,
            String tel) throws SQLException {
        String sql = "INSERT INTO contact(fname, lname, email, tel) VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, fname);
        stmt.setString(2, lname);
        stmt.setString(3, email);
        stmt.setString(4, tel);
        stmt.executeUpdate();
        stmt.close();
    }

    /*
     * UPDATE cars
SET color = 'red'
WHERE brand = 'Volvo';
     */

    public static void updateContactPreparedStatement(Connection c, String id, String fname, String lname, String email,
            String tel) throws SQLException {
        String sql = "UPDATE contact SET fname = ?, lname = ?, email = ?, tel = ? WHERE id = " + id + ";";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, fname);
        stmt.setString(2, lname);
        stmt.setString(3, email);
        stmt.setString(4, tel);
        stmt.executeUpdate();
        stmt.close();
    }

    public static boolean checkUsername(Connection c, String username) throws SQLException {
        Statement stmt = c.createStatement();
        System.out.println("prova");
        String sql = "SELECT * FROM user WHERE username = '" + username + "';";
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            rs.close();
            stmt.close();
            return false;
        } else {
            rs.close();
            stmt.close();
            return true;
        }
    }



    public static List<Contact> viewContact(Connection c, int contactId) throws SQLException, ClassNotFoundException {
        Statement stmt = c.createStatement();
        String sql = "";
        if (contactId == -1) {
            sql = "SELECT * FROM contact ORDER BY fname ASC;";
        } else {
            sql = "SELECT * FROM contact WHERE id = " + contactId + ";";
        }
        List<Contact> listContact = new LinkedList<>();
        ResultSet rs = stmt.executeQuery(sql);
        while ( rs.next() ) {
            int id = parseInt(rs.getString("id"));
            String  fname = rs.getString("fname");
            String  lname = rs.getString("lname");
            String  email = rs.getString("email");
            String  tel = rs.getString("tel");
            Contact contact = new Contact(id, fname, lname, email, tel);
            listContact.add(contact);
        }
        rs.close();
        stmt.close();
        return listContact;
    }
}
