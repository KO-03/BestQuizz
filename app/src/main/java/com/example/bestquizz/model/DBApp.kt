package com.example.bestquizz.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.IOException

class DBApp(context: Context?) :
    SQLiteOpenHelper(context, "bestquizz.db", null, 1) {

    companion object {
        private const val PlayerS = "Players"
        private const val createPlayer = ("CREATE TABLE "
                + PlayerS + "(name TEXT primary key,  bestScore Int)")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createPlayer)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerS)
        onCreate(db)
    }

    fun savePlayer(name: String, bestScore: Int): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("select * from $PlayerS where name = ?", arrayOf(name))
        return if (cursor.count > 0) {
            cursor.moveToFirst()
            if(cursor.getInt(1) < bestScore ){
                //Log.e("save best player ","new best score")
                return this.updatePlayer(name,bestScore)
            }
            else{
                //Log.e("save 1 player ",cursor.getString(0))
                return true;
            }
        } else {
            // Log.e("save 2 player ", name)
            return this.createtPlayer(name,bestScore)

        }
    }

    private fun createtPlayer(name: String, bestScore: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("bestScore", bestScore)
        val result = db.insert(PlayerS, null, contentValues)
        return result != -1L
    }

    private fun updatePlayer(name: String, bestScore: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("bestScore", bestScore)
        val cursor = db.rawQuery("select * from $PlayerS where name = ?", arrayOf(name))
        return if (cursor.count > 0) {
            val result = db.update(PlayerS, contentValues, "name=?", arrayOf(name)).toLong()
            return result == -1L
        } else {
            false
        }
    }

    fun getData(): Cursor{
        val db = this.writableDatabase
        return db.rawQuery("select * from $PlayerS order by bestScore desc ", null)
    }

    fun deleteTable(tableName: String?) {
        val db = this.writableDatabase
        db.delete(tableName, null, null)
    }

    fun getListUser(res: Cursor): List<Player> {
        var listPlayer : List<Player> = listOf();
        try {
            while (res.moveToNext()) {
                listPlayer += Player(res.getString(0),res.getInt(1))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listPlayer;
    }

}