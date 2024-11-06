package com.example.testreader.data;

public class ReadingProgress {

    private int id;
    private int comicId;
    private int chapterId;
    private int pageNumber;
    private String timestamp;

    public ReadingProgress(int id, int comicId, int chapterId, int pageNumber, String timestamp) {
        this.id = id;
        this.comicId = comicId;
        this.chapterId = chapterId;
        this.pageNumber = pageNumber;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getComicId() { return comicId; }
    public void setComicId(int comicId) { this.comicId = comicId; }

    public int getChapterId() { return chapterId; }
    public void setChapterId(int chapterId) { this.chapterId = chapterId; }

    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}