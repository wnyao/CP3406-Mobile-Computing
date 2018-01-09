package com.example.kongwenyao.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongwenyao on 1/9/18.
 */

public class LeaderboardDatabase extends SQLiteOpenHelper {

    //Database Info
    private static final String DATABASE_NAME = "database_leaderboard";
    private static final int DATABASE_VER = 1;
    private static final String TABLE_SCORE = "table_score";

    //Attributes
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    public LeaderboardDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_contacts_table = "CREATE TABLE " + TABLE_SCORE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT," + KEY_SCORE + " INTEGER)";

        db.execSQL(create_contacts_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_SCORE;
        db.execSQL(sql);

        onCreate(db);
    }

    public void addScoreRecord(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Construct content
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, score.getPlayerName());
        contentValues.put(KEY_SCORE, score.getScore());

        //Insert new data instance
        db.insert(TABLE_SCORE, null, contentValues);
        db.close();
    }

    public void deleteScoreRecord(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SCORE, KEY_ID + "=?", new String[] {String.valueOf(score.getId())});
        db.close();
    }

    public void clearAllScoreRecords() {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_SCORE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //Set Object
                Score score = new Score(cursor.getInt(0), cursor.getString(1),
                        cursor.getInt(2));

                deleteScoreRecord(score);

            } while (cursor.moveToNext());
        }
    }

    //Get single record
    public Score getScoreRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SCORE, new String[] {KEY_ID, KEY_NAME, KEY_SCORE}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Score score = new Score(cursor.getInt(0), cursor.getString(1),
                cursor.getInt(2));

        return score;
    }

    //Get all records
    public List<Score> getScoreRecords() {
        List<Score> scoreRecords = new ArrayList<>();

        //Construct query
        String query = "SELECT * FROM " + TABLE_SCORE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //Set Object
                Score score = new Score(cursor.getInt(0), cursor.getString(1),
                        cursor.getInt(2));

                //Add to list
                scoreRecords.add(score);

            } while (cursor.moveToNext());
        }

        return scoreRecords;
    }

}
