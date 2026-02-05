package com.example.gamemanager

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DBNAME: String = "games_database.db"

class GameDatabaseHelper(context: Context): SQLiteOpenHelper(context, DBNAME, null, 1)
{
    override fun onCreate(db: SQLiteDatabase?)
    {
        TODO("Figure out how to structure your database")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    )
    {
        TODO("Figure out how to upgrade your database")
    }

}