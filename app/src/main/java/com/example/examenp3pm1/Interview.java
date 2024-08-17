package com.example.examenp3pm1;

import java.io.Serializable;

public class Interview implements Serializable {
    private String idOrden;
    private String description;
    private String journalist;
    private String date;
    private String imageUrl;
    private String audioUrl;

    public Interview() {
        // Constructor vacío necesario para Firestore
    }

    public Interview(String idOrden, String description, String journalist, String date) {
        this.idOrden = idOrden;
        this.description = description;
        this.journalist = journalist;
        this.date = date;
    }

    // Getters y setters para cada campo
    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJournalist() {
        return journalist;
    }

    public void setJournalist(String journalist) {
        this.journalist = journalist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
