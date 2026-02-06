package com.example.gamemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDatabaseHelper extends  SQLiteOpenHelper
{
    private static GameDatabaseHelper sInstance; // Implementing singleton pattern

    private static final String DBNAME = "games_database.db";
    public static final String PROJ_TABLE = "projects";        // Name of table used for projects
    public static final String CHAR_TABLE = "characters";      // Name of table used for characters

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* Create table of projects, where primary key is the name of the project */
        db.execSQL("CREATE TABLE " + PROJ_TABLE + " (name TEXT PRIMARY KEY, description TEXT)");

        /* Create table of characters, each character can be uniquely identified by the project they belong to
        *  and their name; in case there are multiple characters with the same name, the user would be required to set their
        *  name with some more detail in order to avoid confusion, so for example if a project has a character Horus (egyptian god)
        *  and another character named Horus purely for comedic effect (say a petty prince that can't tolerate that others bear his name),
        *  the user would be required to name one "Horus" or "Horus (god)" and the other "Horus (prince)" or "Prince Horus" */
        db.execSQL("CREATE TABLE " + CHAR_TABLE + " (prj_name TEXT, name TEXT, aliases TEXT, " +
                "species TEXT, birth TEXT, age TEXT, aspect TEXT, personality TEXT, PRIMARY KEY (prj_name, name))");
    }

    @Override
    public void onUpgrade(
        SQLiteDatabase db,
        int oldVersion,
        int newVersion
    )
    {
        db.execSQL("DROP TABLE IF EXISTS " + PROJ_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHAR_TABLE);
    }

    public synchronized boolean addObject(Saveable obj)
    {
        SQLiteDatabase db = getWritableDatabase();

        long result = db.insert(obj.getTable(), obj.getNullColumnHacks(), obj.getContentValues());
        return result != -1L;
    }

    public int getProjectCount()
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + PROJ_TABLE, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        return count;
    }

    public static synchronized GameDatabaseHelper getInstance(Context context)
    {
        if (sInstance == null)
        {
            sInstance = new GameDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public synchronized Cursor getAllEntriesFromTable(String tableName)
    {
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + tableName, null);
    }

    synchronized boolean clearDatabase()
    {
        SQLiteDatabase db = getWritableDatabase();

        if (db == null)
        {
            return false;
        }

        db.execSQL("DROP TABLE IF EXISTS " + PROJ_TABLE);   // Erases project table
        // TODO: Drop other tables

        return true;
    }

    private GameDatabaseHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }
}

/*
val DBNAME: String = "games_database.db"
val PROJ_TABLE: String = "projects"         // Name of table used for projects
val CHAR_TABLE: String = "characters"       // Name of table used for characters

class GameDatabaseHelper(context: Context): SQLiteOpenHelper(context, DBNAME, null, 1)
{
    override fun onCreate(db: SQLiteDatabase)
    {
        /* Create table of projects, where primary key is the name of the project /
        db.execSQL("CREATE TABLE $PROJ_TABLE (name TEXT PRIMARY KEY, description TEXT)")

        /* Create table of characters, each character can be uniquely identified by the project they belong to
        *  and their name; in case there are multiple characters with the same name, the user would be required to set their
        *  name with some more detail in order to avoid confusion, so for example if a project has a character Horus (egyptian god)
        *  and another character named Horus purely for comedic effect (say a petty prince that can't tolerate that others bear his name),
        *  the user would be required to name one "Horus" or "Horus (god)" and the other "Horus (prince)" or "Prince Horus" /
        db.execSQL("CREATE TABLE $CHAR_TABLE ((prj_name TEXT, name TEXT) PRIMARY KEY, aliases TEXT, " +
                "species TEXT, birth TEXT, age TEXT, aspect TEXT, personality TEXT)")
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    )
    {
        db.execSQL("DROP TABLE IF EXISTS $PROJ_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $CHAR_TABLE")
    }

    fun addObject(obj: Saveable): Boolean
    {
        val db: SQLiteDatabase = this.writableDatabase

        val result: Long = db.insert(obj.getTable(), obj.getNullColumnHacks(), obj.getContentValues())
        return result != -1L
    }

    fun getProjectCount(): Int
    {
        val db: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM $PROJ_TABLE", null)
        cursor.moveToFirst()
        val count: Int = cursor.getInt(0)
        cursor.close()

        return count
    }
}*/