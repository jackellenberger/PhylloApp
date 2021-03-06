package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyangxinye on 5/14/15.
 */
public class UserStoryDb {

    // Database fields
    private SQLiteDatabase database;
    private UserDbHelper dbHelper;
    private String[] allColumns = { UserDbHelper.COLUMN_STORY_ID,
            UserDbHelper.COLUMN_STORY_TYPE, UserDbHelper.COLUMN_STORY_TITLE,
            UserDbHelper.COLUMN_STORY_CONTENT, UserDbHelper.COLUMN_STORY_TIMESTAMP,
            UserDbHelper.COLUMN_STORY_POSTER, UserDbHelper.COLUMN_STORY_LATITUDE,
            UserDbHelper.COLUMN_STORY_LONGITUDE, UserDbHelper.COLUMN_STORY_TAGS, };

    public UserStoryDb(Context context) {
        dbHelper = new UserDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createStory(String type, String title, String content,
                            long timestamp, String poster, double latitude, double longitude, String[] tags) {
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COLUMN_STORY_TYPE, type);
        values.put(UserDbHelper.COLUMN_STORY_TITLE, title);
        values.put(UserDbHelper.COLUMN_STORY_CONTENT, content);
        values.put(UserDbHelper.COLUMN_STORY_TIMESTAMP, System.currentTimeMillis());
        values.put(UserDbHelper.COLUMN_STORY_POSTER, poster);
        values.put(UserDbHelper.COLUMN_STORY_LATITUDE, latitude);
        values.put(UserDbHelper.COLUMN_STORY_LONGITUDE, longitude);
        values.put(UserDbHelper.COLUMN_STORY_TAGS, convertArrayToString(tags));
        long insertId = database.insert(UserDbHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(UserDbHelper.TABLE_NAME, allColumns, UserDbHelper.COLUMN_STORY_ID + " = " + insertId, null, null, null, null);
        //Story newStory = cursorToStory(cursor);
        cursor.close();
        //return newStory;
    }

    public void createStory(ClassStoryInfo story) {
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COLUMN_STORY_TYPE, story.getType());
        values.put(UserDbHelper.COLUMN_STORY_TITLE, story.getTitle());
        values.put(UserDbHelper.COLUMN_STORY_CONTENT, story.getContent());
        values.put(UserDbHelper.COLUMN_STORY_TIMESTAMP, story.getTimestamp());
        values.put(UserDbHelper.COLUMN_STORY_POSTER, story.getOriginalPoster());
        values.put(UserDbHelper.COLUMN_STORY_LATITUDE, story.getLatitude());
        values.put(UserDbHelper.COLUMN_STORY_LONGITUDE, story.getLongitude());
        values.put(UserDbHelper.COLUMN_STORY_TAGS, convertArrayToString(story.getTagList()));
        long insertId = database.insert(UserDbHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(UserDbHelper.TABLE_NAME, allColumns, UserDbHelper.COLUMN_STORY_ID + " = " + insertId, null, null, null, null);
        //Story newStory = cursorToStory(cursor);
        cursor.close();
        //return newStory;
    }

    public void deleteStory(ClassStoryInfo story) {
        long id = story.getStoryID();
//        long timestamp = story.getTimestamp();
//        String poster = story.getOriginalPoster();
//        Log.w("deleting timestamp ", String.valueOf(timestamp));
        database.delete(UserDbHelper.TABLE_NAME, UserDbHelper.COLUMN_STORY_ID + " = " + id, null);
        //database.delete(UserDbHelper.TABLE_NAME,UserDbHelper.COLUMN_STORY_TIMESTAMP + " = " + timestamp, null);
    }

    public List<ClassStoryInfo> getAllStories() {
        List<ClassStoryInfo> stories = new ArrayList<ClassStoryInfo>();

        Cursor cursor = database.query(UserDbHelper.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ClassStoryInfo story = cursorToStory(cursor);
            stories.add(story);
            cursor.moveToNext();
        }
        cursor.close();
        return stories;
    }

    public ClassStoryInfo cursorToStory(Cursor cursor) {
        ClassStoryInfo story = new ClassStoryInfo();
        story.setStoryID(cursor.getLong(0));
        story.setType(cursor.getString(1));
        story.setTitle(cursor.getString(2));
        story.setContent(cursor.getString(3));
        story.setTimestamp(cursor.getLong(4));
        story.setOriginalPoster(cursor.getString(5));
        story.setLongitude(cursor.getDouble(6));
        story.setLatitude(cursor.getDouble(7));
        String tags = cursor.getString(8);
        story.setTagList(convertStringToArray(tags));
        return story;
    }

    // Utility functions for converting tag arrays to a String
    public static String strSeparator = "__,__";
    public static String convertArrayToString(String[] array){
        String str = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                str = str + array[i];
                // Do not append comma at the end of last element
                if (i < array.length - 1) {
                    str = str + strSeparator;
                }
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
