package com.spaceotechnologies.training.stopwatch.volley;

/**
 * Created by Kostez on 02.09.2016.
 */
public class Model {

    private String model;
    private String imageUrl;
    private int id;
    private int rank;
    private String author;

    public String getModel() {
        return model;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public String getAuthor() {
        return author;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}