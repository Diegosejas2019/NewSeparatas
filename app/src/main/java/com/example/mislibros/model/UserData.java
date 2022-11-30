package com.example.mislibros.model;

import com.google.gson.annotations.SerializedName;

public class UserData {

    public UserData() {
    }

    @SerializedName("mail")
    public String mail;

    @SerializedName("id")
    public int id;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
