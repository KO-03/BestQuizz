package com.example.bestquizz.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBApp extends SQLiteOpenHelper {

    private static final String PlayerS = "Players";

    private static final String createPlayer = "CREATE TABLE "
            + PlayerS + "(name TEXT primary key,  bestScore TEXT)";

    public DBApp(Context context) {
        super(context, "bestquizz.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPlayer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerS);
        onCreate(db);

    }

    public boolean createtPlayer(String name, String bestScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("bestScore", bestScore);
        long result = db.insert(PlayerS, null, contentValues);

        if (result == -1) return false;

        else return true;
    }

    public boolean updatePlayer(String name, String bestScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("bestScore", bestScore);

        Cursor cursor = db.rawQuery("select * from Player where name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.update(PlayerS, contentValues, "name=?", new String[]{name});
            if (result == -1) {return false;}

            else {return true;}
        } else { return false;}
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " +  PlayerS + " order by bestScore ", null);

        return cursor;
    }

    public void deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
    }

    /*
    public boolean validLoginPassword(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PlayerS + " where login = ?", new String[]{login});

        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                if(cursor.getString(5).matches(password)){
                    return true;
                }
            }
        }
        return false;
    }
*/

}