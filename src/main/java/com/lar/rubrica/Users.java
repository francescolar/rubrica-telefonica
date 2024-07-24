package com.lar.rubrica;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Users {
    @NotNull
    private int id;

    @NotNull
	@Size(min=2, max=30)
    private String username;

    @NotNull
	@Size(min=2, max=200)
    private String password;

    @Size(min=2, max=30)
    private String fname;

    @Size(min=2, max=30)
    private String lname;

    private String salt;


    public Users(@NotNull @Size(min = 2, max = 30) String username, @NotNull @Size(min = 2, max = 200) String password,
            String fname, String lname) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }

    @Override
    public String toString() {
        return "Users [id=" + id + ", username=" + username + ", password=" + password + ", fname=" + fname + ", lname="
                + lname + ", salt=" + salt + "]";
    }

    public Users() {
    }

    public Users(String username, String password, String fname, String lname, String salt) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.salt = salt;
    }

    public Users(int id, String username, String password, String fname, String lname, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.salt = salt;
    }

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return String return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return String return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

}
