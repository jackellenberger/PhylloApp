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
    private String[] allColumns = { UserDbHelper.COLUMN_STORY_ID, UserDbHelper.COLUMN_STORY_TYPE,
            UserDbHelper.COLUMN_STORY_TITLE,
            UserDbHelper.COLUMN_STORY_CONTENT, UserDbHelper.COLUMN_STORY_TIMESTAMP,
            UserDbHelper.COLUMN_STORY_POSTER, UserDbHelper.COLUMN_STORY_LOCATION_ID, UserDbHelper.COLUMN_STORY_TAGS, };

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
                            long timestamp, String poster, long locationId, String[] tags) {
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COLUMN_STORY_TYPE, type);
        values.put(UserDbHelper.COLUMN_STORY_TITLE, title);
        values.put(UserDbHelper.COLUMN_STORY_CONTENT, content);
        values.put(UserDbHelper.COLUMN_STORY_TIMESTAMP, System.currentTimeMillis());
        values.put(UserDbHelper.COLUMN_STORY_POSTER, poster);
        values.put(UserDbHelper.COLUMN_STORY_LOCATION_ID, locationId);
        values.put(UserDbHelper.COLUMN_STORY_TAGS, convertArrayToString(tags));
        long insertId = database.insert(UserDbHelper.TABLE_NAME, null, values);
        Log.w("inserting timestamp ", String.valueOf(timestamp));
        Cursor cursor = database.query(UserDbHelper.TABLE_NAME, allColumns, UserDbHelper.COLUMN_STORY_ID + " = " + insertId, null, null, null, null);
        //Story newStory = cursorToStory(cursor);
        cursor.close();
        //return newStory;
    }

    public void deleteStory(ClassStoryInfo story) {
        long id = story.getStoryID();
        long timestamp = story.getTimestamp();
        String poster = story.getOriginalPoster();
        Log.w("deleting timestamp ", String.valueOf(timestamp));
        //database.delete(UserDbHelper.TABLE_NAME, UserDbHelper.COLUMN_STORY_ID + " = " + id, null);
        database.delete(UserDbHelper.TABLE_NAME,UserDbHelper.COLUMN_STORY_TIMESTAMP + " = " + timestamp, null);
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
        story.setLocationId(cursor.getLong(6));
        String tags = cursor.getString(7);
        story.setTagList(convertStringToArray(tags));
        return story;
    }

    // Utility functions for converting tag arrays to a String
    public static String strSeparator = "__,__";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if(i < array.length - 1){
                str = str + strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
