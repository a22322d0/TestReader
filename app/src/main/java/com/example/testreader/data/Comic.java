package com.example.testreader.data;

public class Comic {

    private int id;
    private String name;
    private String author;
    private String source;
    private String coverImage;

    public Comic(int id, String name, String author, String source, String coverImage) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.source = source;
        this.coverImage = coverImage;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
}