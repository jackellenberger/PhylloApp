package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    private String[] allColumns = { UserDbHelper.COLUMN_STORY_TYPE,
            UserDbHelper.COLUMN_STORY_TITLE,
            UserDbHelper.COLUMN_STORY_CONTENT, UserDbHelper.COLUMN_STORY_TIMESTAMP,
            UserDbHelper.COLUMN_STORY_POSTER, UserDbHelper.COLUMN_STORY_TAGS, UserDbHelper.COLUMN_STORY_LOCATION_ID };

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
                             long timestamp, String poster, String tags, long locationId) {
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COLUMN_STORY_TYPE, type);
        values.put(UserDbHelper.COLUMN_STORY_TITLE, title);
        values.put(UserDbHelper.COLUMN_STORY_CONTENT, content);
        values.put(UserDbHelper.COLUMN_STORY_TIMESTAMP, timestamp);
        values.put(UserDbHelper.COLUMN_STORY_POSTER, poster);
        values.put(UserDbHelper.COLUMN_STORY_TAGS, tags);
        values.put(UserDbHelper.COLUMN_STORY_LOCATION_ID, locationId);
        long insertId = database.insert(UserDbHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(UserDbHelper.TABLE_NAME, allColumns, UserDbHelper.COLUMN_STORY_ID + " = " + insertId, null, null, null, null);
        //Story newStory = cursorToStory(cursor);
        cursor.close();
        //return newStory;
    }

    public void deleteStory(ClassStoryInfo story) {
        long id = story.getStoryID();
        database.delete(UserDbHelper.TABLE_NAME, UserDbHelper.COLUMN_STORY_ID + " = " + id, null);
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
        story.setType(cursor.getString(0)); // ID is skipped
        story.setTitle(cursor.getString(1));
        story.setContent(cursor.getString(2));
        story.setTimestamp(cursor.getLong(3));
        return story;
    }
}
