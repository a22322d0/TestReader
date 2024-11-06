package com.example.testreader.data;

public class Chapter {

    private int id;
    private String name;
    private int comicId;
    private int order;

    public Chapter(int id, String name, int comicId, int order) {
        this.id = id;
        this.name = name;
        this.comicId = comicId;
        this.order = order;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getComicId() { return comicId; }
    public void setComicId(int comicId) { this.comicId = comicId; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
}