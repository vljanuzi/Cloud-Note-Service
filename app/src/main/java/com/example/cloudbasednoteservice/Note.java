package com.example.cloudbasednoteservice;

public class Note {

    private String content;
    private long dateInMillis;
    private String websafeKey;

    public Note() {

    }

    public Note(String content, long date) {
        this.content = content;
        this.dateInMillis = date;
    }

    public String getContent() {
        return content;
    }

    public long getDateInMillis() {
        return dateInMillis;
    }

    public String getWebsafeKey() { return websafeKey;}

    @Override
    public String toString() {
        return content;
    }
}
