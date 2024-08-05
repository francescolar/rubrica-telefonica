package com.lar.rubrica.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.lar.rubrica.module.User;
import com.lar.rubrica.util.CryptoPassword;
import com.lar.rubrica.util.DbUtility;

import jakarta.validation.Valid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class UserService {

    public boolean registerUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return false;
        }
        try {
            String salt = BCrypt.gensalt();
            Connection c = DbUtility.createConnection();
            boolean userExist = DbUtility.checkUsername(c, user.getUsername());

            if (userExist) {
                DbUtility.insertUserPreparedStatement(c,
                        user.getUsername(),
                        CryptoPassword.cryptoPasswordwithSalt(user.getPassword(), salt),
                        salt,
                        user.getFname(),
                        user.getLname());

                DbUtility.closeConnection(c);
                return true;
            } else {
                DbUtility.closeConnection(c);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateExistingUser(String fname, String lname, String username, String password, int id) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "UPDATE \"user\" SET username = ?, fname = ?, lname = ?, password = ? WHERE id = ?";
        PreparedStatement stmt =  c.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, fname);
        stmt.setString(3, lname);
        stmt.setString(4, password);
        stmt.setInt(5, id);
        stmt.executeUpdate();
        stmt.close();
        c.close();
    }
}
