package com.lar.rubrica;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class CryptoPassword {

    public static String cryptoPassword(String clearPassword){
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(clearPassword, salt);
    }

    public static String cryptoPasswordwithSalt(String clearPassword, String salt){
        return BCrypt.hashpw(clearPassword, salt);
    }

    public static boolean checkEqualsPassword(String clearPassword, String hashedPassword){
        return BCrypt.checkpw(clearPassword, hashedPassword);
    }
}