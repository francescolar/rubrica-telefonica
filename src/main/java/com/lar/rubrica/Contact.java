package com.lar.rubrica;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Contact {

    @NotNull
    private int id;
    
    @Size(min=2, max=30)
    private String fname;

    @Size(min=2, max=30)
    private String lname;

    @Size(min=2, max=50)
    private String email;

    @Size(min=5, max=15)
    private String tel;

    @NotNull
    private int ownerId;

    public Contact(@Size(min = 2, max = 30) String fname, @Size(min = 2, max = 30) String lname,
            @Size(min = 2, max = 50) String email, @Size(min = 5, max = 15) String tel) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.tel = tel;
    }



    public Contact(@Size(min = 2, max = 30) String fname, @Size(min = 2, max = 30) String lname,
            @Size(min = 2, max = 50) String email, @Size(min = 10, max = 10) String tel, @NotNull int ownerId) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.tel = tel;
        this.ownerId = ownerId;
    }



    public Contact() {
    }

    public Contact(String email, String fname, int id, String lname, int ownerId, String tel) {
        this.email = email;
        this.fname = fname;
        this.id = id;
        this.lname = lname;
        this.ownerId = ownerId;
        this.tel = tel;
    }

    public Contact(int id, String fname, String lname, String email, String tel) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.tel = tel;
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
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }


    /**
     * @return int return the ownerId
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contact{");
        sb.append("id=").append(id);
        sb.append(", fname=").append(fname);
        sb.append(", lname=").append(lname);
        sb.append(", email=").append(email);
        sb.append(", tel=").append(tel);
        sb.append(", ownerId=").append(ownerId);
        sb.append('}');
        return sb.toString();
    }

}
