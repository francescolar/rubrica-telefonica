package com.lar.rubrica.module;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactDetails {

    @NotNull
    private int id;

    @NotNull
    private int contactId;

    @NotNull
    @Size(min=2, max=2)
    private String initials;

    private String imgPath;

    private boolean imgEnabled;





    public ContactDetails(@NotNull int contactId, @NotNull @Size(min = 2, max = 2) String initials, String imgPath,
            boolean imgEnabled) {
        this.contactId = contactId;
        this.initials = initials;
        this.imgPath = imgPath;
        this.imgEnabled = imgEnabled;
    }

    public ContactDetails(@NotNull int contactId, @NotNull @Size(min = 2, max = 2) String initials) {
        this.contactId = contactId;
        this.initials = initials;
    }

    public ContactDetails() {
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
     * @return int return the contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param contactId the contactId to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @return String return the initials
     */
    public String getInitials() {
        return initials;
    }

    /**
     * @param initials the initials to set
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * @return String return the imgPath
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * @param imgPath the imgPath to set
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * @return boolean return the imgEnabled
     */
    public boolean isImgEnabled() {
        return imgEnabled;
    }

    /**
     * @param imgEnabled the imgEnabled to set
     */
    public void setImgEnabled(boolean imgEnabled) {
        this.imgEnabled = imgEnabled;
    }

}