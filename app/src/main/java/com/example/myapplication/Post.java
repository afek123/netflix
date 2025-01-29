package com.example.myapplication;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "content")
    private String content;
    public Post(String content) {
        this.content = content;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return content;
    }
}
