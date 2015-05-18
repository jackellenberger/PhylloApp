package edu.uchicago.cs234.spr15.quokka.phyllo;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by jellenberger on 5/14/15.
 */
public class ClassStoryInfo {
    private long storyID;
    private String type; // tip, url OR longform
    private String title;
    private String content;
    private long timestamp;
    private String originalPoster;
    private long locationId;
    private String[] tagList;

    public long getStoryID() {
        return storyID;
    }

    public void setStoryID(long storyID) {
        this.storyID = storyID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOriginalPoster() {
        return originalPoster;
    }

    public void setOriginalPoster(String originalPoster) {
        this.originalPoster = originalPoster;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String[] getTagList() {
        return tagList;
    }

    public void setTagList(String[] tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        Timestamp ts = new Timestamp(timestamp);
        return Arrays.toString(tagList) + ", " + locationId + ", " + title + " (" + ts.toString() + " )" + ": " + content;
    }
}
