package com.susankya.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhinav on 12/1/17.
 */

public class Book {
    @SerializedName("name")
    private String name;
    @SerializedName("author")
    private String author;
    @SerializedName("total")
    private int total;
    @SerializedName("remaining")
    private int remaining;
    @SerializedName("id")
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
