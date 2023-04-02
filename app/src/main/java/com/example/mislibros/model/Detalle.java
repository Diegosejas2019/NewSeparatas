package com.example.mislibros.model;

import com.google.gson.annotations.SerializedName;

public class Detalle {

    private int id;
    private String fecha;
    private String videoTitle;
    private String videoUrl;
    private String audioTitle;
    private String audioUrl;
    private String fileTitle;
    private String fileUrl;
    private String PublicacionTitle;
    private String PublicacionUrl;

    public Detalle(int id, String fecha, String videoTitle, String videoUrl, String audioTitle, String audioUrl, String fileTitle, String fileUrl, String PublicacionTitle, String PublicacionUrl) {
        this.id = id;
        this.fecha = fecha;
        this.videoTitle = videoTitle;
        this.videoUrl = videoUrl;
        this.audioTitle = audioTitle;
        this.audioUrl = audioUrl;
        this.fileTitle = fileTitle;
        this.fileUrl = fileUrl;
        this.PublicacionTitle = PublicacionTitle;
        this.PublicacionUrl = PublicacionUrl;
    }

    public Detalle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPublicacionTitle() {
        return PublicacionTitle;
    }

    public void setPublicacionTitle(String publicacionTitle) {
        PublicacionTitle = publicacionTitle;
    }

    public String getPublicacionUrl() {
        return PublicacionUrl;
    }

    public void setPublicacionUrl(String publicacionUrl) {
        PublicacionUrl = publicacionUrl;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
