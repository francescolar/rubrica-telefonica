package com.lar.rubrica.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.lar.rubrica.module.Contact;
import com.lar.rubrica.module.ContactDetails;
import com.lar.rubrica.module.User;

import io.github.cdimascio.dotenv.Dotenv;


@Component
public class DbUtility {


    private static DataSource dataSource;

    @Autowired
    public DbUtility(DataSource dataSource) {
        DbUtility.dataSource = dataSource;
    }

    public static Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static List<User> viewUser(Connection connection) throws SQLException {

        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM \"user\" LIMIT 100";
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
        String sql = "INSERT INTO \"user\"(password,fname,lname,username,salt) VALUES( ?, ?, ?, ?, ?);";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, password);
        stmt.setString(2, fname);
        stmt.setString(3, lname);
        stmt.setString(4, username);
        stmt.setString(5, salt);
        stmt.executeUpdate();
        sql = "INSERT INTO authorities(username,authority) VALUES(?, ?);";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, "ROLE_USER");

        stmt.executeUpdate();
        stmt.close();
    }

    public static int insertContactPreparedStatement(Connection c, String fname, String lname, String email,
            String tel, int ownerid) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO contact(fname, lname, email, tel, ownerid) VALUES (?, ?, ?, ?, ?) RETURNING id;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, fname);
        stmt.setString(2, lname);
        stmt.setString(3, email);
        stmt.setString(4, tel);
        stmt.setInt(5, ownerid);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            return id;
        } else {
            stmt.close();
            throw new SQLException("Errore nel recupero dell'ID dell'ultimo inserimento.");
        }
    }

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

    public static void deleteContactPreparedStatement(Connection c, int id) throws SQLException {
        String sql = "DELETE FROM contact WHERE id = ?";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static boolean checkUsername(Connection c, String username) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM \"user\" WHERE username = '" + username + "';";
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
            Contact contact = new Contact();
            contact.setId(rs.getInt("id"));
            contact.setFname(rs.getString("fname"));
            contact.setLname(rs.getString("lname"));
            contact.setEmail(rs.getString("email"));
            contact.setTel(rs.getString("tel"));
            contact.setOwnerId(rs.getInt("ownerid"));
            listContact.add(contact);
        }
        rs.close();
        stmt.close();
        return listContact;
    }

     public static int getAuthenticatedUserId() throws SQLException, ClassNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }

        return getUserIdByUsername(username);
    }

    public static int getUserIdByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT id FROM \"user\" WHERE username = ?";
        Connection c = createConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        int userId = -1;
        if (rs.next()) {
            userId = rs.getInt("id");
        }
        rs.close();
        stmt.close();
        closeConnection(c);
        return userId;
    }

    public static String getAuthenticatedUserFname() throws SQLException, ClassNotFoundException {
        int authUserId = DbUtility.getAuthenticatedUserId();
        String sql = "SELECT fname FROM \"user\" WHERE id = ?";
        Connection c = createConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, authUserId);
        ResultSet rs = stmt.executeQuery();
        String authUserFname = "";
        if (rs.next()) {
            authUserFname = rs.getString("fname");
        }
        rs.close();
        stmt.close();
        closeConnection(c);
        return authUserFname;
    }

    public static User getUserDetails() throws SQLException, ClassNotFoundException {
        int authUserId = DbUtility.getAuthenticatedUserId();
        String sql = "SELECT * FROM \"user\" WHERE id = ?";
        Connection c = createConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, authUserId);
        ResultSet rs = stmt.executeQuery();
        User user = new User();
        if (rs.next()) {
            user.setId(authUserId);
            user.setUsername(rs.getString("username"));
            user.setFname(rs.getString("fname"));
            user.setLname(rs.getString("lname"));
        }
        rs.close();
        stmt.close();
        closeConnection(c);
        return user;
    }

    public static void setContactInitials(int contact_id, String fname, String lname) throws SQLException, ClassNotFoundException {
        Connection c = createConnection();
        String sql = "INSERT INTO contact_details(contact_id, initials) VALUES (?, ?);";
        PreparedStatement stmt = c.prepareStatement(sql);
        String nameInitial, surnameInitial, initials;
        nameInitial = "";
        surnameInitial = "";
        initials = "";
        if (fname != null && !fname.isEmpty()) {
            nameInitial = fname.substring(0, 1).toUpperCase();
        }

        if (lname != null && !lname.isEmpty()) {
            surnameInitial = lname.substring(0, 1).toUpperCase();
        }
        initials = nameInitial.concat(surnameInitial);         
        stmt.setInt(1, contact_id);
        stmt.setString(2, initials);
        stmt.executeUpdate();
        stmt.close();
        c.close();
    }

    public static List<ContactDetails> viewContactDetails(Connection c) throws SQLException, ClassNotFoundException {
        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM contact_details;";
        List<ContactDetails> listContactDetails = new LinkedList<>();
        ResultSet rs = stmt.executeQuery(sql);
        while ( rs.next() ) {
            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setId(rs.getInt("id"));
            contactDetails.setContactId(rs.getInt("contact_id"));
            contactDetails.setInitials(rs.getString("initials"));
            contactDetails.setImgPath(rs.getString("img_path"));
            contactDetails.setImgEnabled(rs.getBoolean("img_enabled"));
            listContactDetails.add(contactDetails);
        }
        rs.close();
        stmt.close();
        return listContactDetails;
    }

    public static void saveUploadedFile(MultipartFile file, int ownerId, int contactId) throws IOException, SQLException {
        Dotenv dotenv = Dotenv.load();
        String uploadPath = dotenv.get("UPLOAD.PATH");
        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String newFileName = "avatar-" + contactId + extension;
            Path uploadDir = Paths.get(uploadPath + File.separator + ownerId + File.separator + contactId);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(newFileName);
            file.transferTo(filePath);
            Connection c = DbUtility.createConnection();
            String sql = "UPDATE contact_details SET img_path = ?, img_enabled = ? WHERE contact_id = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, newFileName);
            stmt.setBoolean(2, true);
            stmt.setInt(3, contactId);
            stmt.executeUpdate();
            stmt.close();
        }
    }

}
