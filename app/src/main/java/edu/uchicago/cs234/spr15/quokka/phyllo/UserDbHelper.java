package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangyangxinye on 5/13/15.
 */
public class UserDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "localUserQueue";
    public static final String COLUMN_STORY_ID = "_id";
    public static final String COLUMN_STORY_TYPE = "type";
    public static final String COLUMN_STORY_TITLE = "title";
    public static final String COLUMN_STORY_CONTENT = "content";
    public static final String COLUMN_STORY_TIMESTAMP = "timestamp";
    public static final String COLUMN_STORY_POSTER = "poster"; // ORIGINAL POSTER
    public static final String COLUMN_STORY_LOCATION_ID = "locationid";
    public static final String COLUMN_STORY_TAGS = "tags";

    private static final String DATABASE_NAME = "localUserQueue.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_STORY_ID + " INTEGER PRIMARY KEY , " +
                    COLUMN_STORY_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_STORY_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_STORY_CONTENT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_STORY_TIMESTAMP + REAL_TYPE + COMMA_SEP +
                    COLUMN_STORY_POSTER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_STORY_LOCATION_ID + INT_TYPE + COMMA_SEP +
                    COLUMN_STORY_TAGS + TEXT_TYPE +
            " )";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
