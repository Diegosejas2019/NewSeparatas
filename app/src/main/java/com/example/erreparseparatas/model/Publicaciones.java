package com.example.erreparseparatas.model;

import com.google.gson.annotations.SerializedName;

public class Publicaciones {
    private int id;

    @SerializedName("title")
    private String title;
    private String shortdesc;
    @SerializedName("ImageUrl")
    private String imageUrl;
    private String linkPDF1;
    private String linkPDF2;
    private String linkVimeo;
    private String linkAudio;

    public Publicaciones(int id, String title, String shortdesc, String image,String linkPDF1,String linkPDF2,String linkVimeo,String linkAudio) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.imageUrl = image;
        this.linkPDF1 = linkPDF1;
        this.linkPDF2 = linkPDF2;
        this.linkVimeo = linkVimeo;
        this.linkAudio = linkAudio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkPDF1() {
        return linkPDF1;
    }

    public void setLinkPDF1(String linkPDF1) {
        this.linkPDF1 = linkPDF1;
    }

    public String getLinkPDF2() {
        return linkPDF2;
    }

    public void setLinkPDF2(String linkPDF2) {
        this.linkPDF2 = linkPDF2;
    }

    public String getLinkVimeo() {
        return linkVimeo;
    }

    public void setLinkVimeo(String linkVimeo) {
        this.linkVimeo = linkVimeo;
    }

    public String getLinkAudio() {
        return linkAudio;
    }

    public void setLinkAudio(String linkAudio) {
        this.linkAudio = linkAudio;
    }
}
