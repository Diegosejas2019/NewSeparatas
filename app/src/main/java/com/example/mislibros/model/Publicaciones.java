package com.example.mislibros.model;

import com.google.gson.annotations.SerializedName;

public class Publicaciones {
    private int id;

    @SerializedName("title")
    private String title;
    @SerializedName("ImageUrl")
    private String imageUrl;
    private String code;
    public Publicaciones(){};
    public Publicaciones(int id, String title, String image, String code) {
        this.id = id;
        this.title = title;
        this.imageUrl = image;
        this.code = code;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() { return code; }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
