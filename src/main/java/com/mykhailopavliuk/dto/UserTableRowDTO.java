package com.mykhailopavliuk.dto;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class UserTableRowDTO extends RecursiveTreeObject<UserTableRowDTO> {
    private long id;
    private String email;
    private int numberOfUrls;
    private boolean wasEdited;

    public UserTableRowDTO(long id, String email, int numberOfUrls) {
        this.id = id;
        this.email = email;
        this.numberOfUrls = numberOfUrls;
        wasEdited = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumberOfUrls() {
        return numberOfUrls;
    }

    public void setNumberOfUrls(int numberOfUrls) {
        this.numberOfUrls = numberOfUrls;
    }


    public boolean isWasEdited() {
        return wasEdited;
    }

    public void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
    }
}
