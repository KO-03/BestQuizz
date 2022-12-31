package com.example.bestquizz.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.IOException

class DBApp(context: Context?) :
    SQLiteOpenHelper(context, "bestquizz.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createPlayer)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerS)
        onCreate(db)
    }

    fun createtPlayer(name: String?, bestScore: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("bestScore", bestScore)
        val result = db.insert(PlayerS, null, contentValues)
        return if (result == -1L) false else true
    }

    fun updatePlayer(name: String, bestScore: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("bestScore", bestScore)
        val cursor = db.rawQuery("select * from Player where name = ?", arrayOf(name))
        return if (cursor.count > 0) {
            val result = db.update(PlayerS, contentValues, "name=?", arrayOf(name)).toLong()
            if (result == -1L) {
                return false
            } else {
                return true
            }
        } else {
            false
        }
    }

    fun getData(): Cursor{
        val db = this.writableDatabase
        return db.rawQuery("select * from $PlayerS order by bestScore ", null)
    }

    fun deleteTable(tableName: String?) {
        val db = this.writableDatabase
        db.delete(tableName, null, null)
    }

    fun getListUser(res: Cursor): List<Player> {
        var listPlayer : List<Player> = listOf();
        try {
            while (res.moveToNext()) {
                listPlayer += Player(res.getString(0),res.getString(1))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listPlayer;
    }

    companion object {
        private const val PlayerS = "Players"
        private const val createPlayer = ("CREATE TABLE "
                + PlayerS + "(name TEXT primary key,  bestScore TEXT)")
    }
}