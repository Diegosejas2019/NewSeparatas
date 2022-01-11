package com.example.erreparseparatas.model;

import com.google.gson.annotations.SerializedName;

public class ResponseUSER {

    public ResponseUSER() {
    }

    @SerializedName("token")
    public String token;

    @SerializedName("user")
    public UserData user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}

