package com.example.gamemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.gamemanager.Character.Character;
import com.example.gamemanager.Character.GameCharacter;
import com.example.gamemanager.Character.RPGCharacter;

public class GameDatabaseHelper extends  SQLiteOpenHelper
{
    // This database, in order to work with the frameworks of character and, in the future, of item, which both have multiple subclasses,
    // must be at least in 3rd normal form.

    private static GameDatabaseHelper sInstance; // Implementing singleton pattern

    private static final String DBNAME = "games_database.db";
    public static final String PROJ_TABLE = "projects";        // Name of table used for projects

    // Character related tables
    public static final String CHAR_TABLE = "characters";      // Name of table used for characters
    public static final String GAMC_TABLE = "game_characters"; // Name of table used for game characters
    public static final String RPG_TABLE = "rpg_characters";   // Name of table used for RPG characters

    // Item related tables
    public static final String ITEM_TABLE = "items";
    public static final String WEPN_TABLE = "weapons";

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* Create table of projects, where primary key is the name of the project */
        db.execSQL("CREATE TABLE " + PROJ_TABLE + " (name TEXT PRIMARY KEY, description TEXT)");

        // CHARACTER FRAMEWORK

        /* Create table of characters, each character can be uniquely identified by the project they belong to
        *  and their name; in case there are multiple characters with the same name, the user would be required to set their
        *  name with some more detail in order to avoid confusion, so for example if a project has a character Horus (egyptian god)
        *  and another character named Horus purely for comedic effect (say a petty prince that can't tolerate that others bear his name),
        *  the user would be required to name one "Horus" or "Horus (god)" and the other "Horus (prince)" or "Prince Horus" */
        db.execSQL("CREATE TABLE " + CHAR_TABLE + " (prj_name TEXT, name TEXT, aliases TEXT, " +
                "species TEXT, birth TEXT, age TEXT, aspect TEXT, personality TEXT, PRIMARY KEY (prj_name, name))");
        // {parent project, name} -> {species, birth, age etc...}

        // This table contains the extra information of characters of class GameCharacter, not the information of its subclasses
        db.execSQL("CREATE TABLE " + GAMC_TABLE + " (prj_name TEXT, name TEXT, max_health INTEGER, PRIMARY KEY (prj_name, name))");

        // This table contains the extra information of characters of class RPGCharacter
        db.execSQL("CREATE TABLE " + RPG_TABLE + " (prj_name TEXT, name TEXT, cur_health INTEGER, owner TEXT, PRIMARY KEY (prj_name, name))");

        // ITEM FRAMEWORK

        db.execSQL("CREATE TABLE " + ITEM_TABLE + " (prj_name TEXT, name TEXT, description TEXT, PRIMARY KEY (prj_name, name))");

        db.execSQL("CREATE TABLE " + WEPN_TABLE + " (prj_name TEXT, name TEXT, dmg INTEGER, PRIMARY KEY (prj_name, name))");
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

        String[] tables = obj.getTables();
        ContentValues[] cvs = obj.getContentValues();

        if (tables.length != cvs.length)
        {
            return false;
        }

        /*  "Why are we doing this?"
        *   Inheritance is not a thing in SQL and each subclass of character or really any class is gonna have data
        *   that its parents and other sibling and nephew classes don't have, the amount and types of data stored
        *   varies as we go down the inheritance tree. So to memorize all this data we define different tables
        *   which hold the data unique to each specific class, and through the getTables() and getContentValues() methods
        *   we get the list of the tables we can use to memorize this stuff as well as the values to memorize.
        * */
        for (int i = 0; i < tables.length; i++)
        {
            // TODO: Make more robust because if one insertion goes wrong you have garbage data
            long res = db.insert(tables[i], obj.getNullColumnHacks(), cvs[i]);
            if (res == -1L)
            {
                return false;
            }
        }

        return true;
    }

    private Character resolveCharacter(Project owner, String name, Cursor cursor, SQLiteDatabase db)
    {
        // Assuming the moveToFirst() or moveToNext() operations were successful

        Character ret;

        String species      = cursor.getString(cursor.getColumnIndexOrThrow("species"));
        String birth        = cursor.getString(cursor.getColumnIndexOrThrow("birth"));
        String age          = cursor.getString(cursor.getColumnIndexOrThrow("age"));

        String aspect       = cursor.getString(cursor.getColumnIndexOrThrow("aspect"));
        String aliases      = cursor.getString(cursor.getColumnIndexOrThrow("aliases"));
        //String backstory    = cursor.getString(cursor.getColumnIndexOrThrow("backstory"));  // TODO: IMPLEMENT BACKSTORY!!!
        String personality  = cursor.getString(cursor.getColumnIndexOrThrow("personality"));

        Cursor aux_cursor = db.rawQuery("SELECT * FROM " + GAMC_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

        if (aux_cursor.moveToFirst())
        {
            int maxHealth = aux_cursor.getInt(aux_cursor.getColumnIndexOrThrow("max_health"));

            aux_cursor.close();

            // Checking if character is an RPG character (this'll get funny once we reach D&D)
            aux_cursor = db.rawQuery("SELECT * FROM " + RPG_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

            if (aux_cursor.moveToFirst())
            {
                int curHealth = aux_cursor.getInt(aux_cursor.getColumnIndexOrThrow("cur_health"));
                String ownerPlayer = aux_cursor.getString(aux_cursor.getColumnIndexOrThrow("owner"));

                // TODO: Implement D&D character maybe, low priority and high refactoring

                ret = new RPGCharacter(owner, name, species, birth, age, maxHealth, curHealth, ownerPlayer);
            }
            else
            {
                ret = new GameCharacter(owner, name, species, birth, age, maxHealth);
            }
        }
        else
        {
            ret = new Character(owner, name, species, birth, age);
        }

        aux_cursor.close();

        ret.setAspect(aspect);
        ret.setAliases(aliases);
        // TODO: ADD BACKSTORY AAAA!!!
        ret.setPersonality(personality);

        return ret;
    }

    // Gets a character from the database based on the name
    public Character getCharacter(Project owner, String name)
    {
        SQLiteDatabase db = getReadableDatabase();

        // Selecting the character based on primary key - Will either return no entries or one entry
        Cursor cursor = db.rawQuery("SELECT * FROM " + CHAR_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

        if (!cursor.moveToFirst())
        {
            return null;
        }

        // TODO: Maybe substitute with function call of resolveCharacter()
        // resolveCharacter(owner, name, cursor, db);
        Character ret;

        String species      = cursor.getString(cursor.getColumnIndexOrThrow("species"));
        String birth        = cursor.getString(cursor.getColumnIndexOrThrow("birth"));
        String age          = cursor.getString(cursor.getColumnIndexOrThrow("age"));

        String aspect       = cursor.getString(cursor.getColumnIndexOrThrow("aspect"));
        String aliases      = cursor.getString(cursor.getColumnIndexOrThrow("aliases"));
        //String backstory    = cursor.getString(cursor.getColumnIndexOrThrow("backstory"));  // TODO: IMPLEMENT BACKSTORY!!!
        String personality  = cursor.getString(cursor.getColumnIndexOrThrow("personality"));

        // Finished reading basic character info, query for other types of characters
        cursor.close();

        // Checking if character is a game character
        cursor = db.rawQuery("SELECT * FROM " + GAMC_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

        if (cursor.moveToFirst())
        {
            int maxHealth = cursor.getInt(cursor.getColumnIndexOrThrow("max_health"));

            cursor.close();

            // Checking if character is an RPG character (this'll get funny once we reach D&D)
            cursor = db.rawQuery("SELECT * FROM " + RPG_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

            if (cursor.moveToFirst())
            {
                int curHealth = cursor.getInt(cursor.getColumnIndexOrThrow("cur_health"));
                String ownerPlayer = cursor.getString(cursor.getColumnIndexOrThrow("owner"));

                // TODO: Implement D&D character maybe, low priority and high refactoring

                ret = new RPGCharacter(owner, name, species, birth, age, maxHealth, curHealth, ownerPlayer);
            }
            else
            {
                ret = new GameCharacter(owner, name, species, birth, age, maxHealth);
            }
        }
        else
        {
            ret = new Character(owner, name, species, birth, age);
        }

        cursor.close();

        ret.setAspect(aspect);
        ret.setAliases(aliases);
        // TODO: ADD BACKSTORY AAAA!!!
        ret.setPersonality(personality);

        return ret;
    }

    public int getCharacterCountIn(Project owner)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + CHAR_TABLE + " WHERE (prj_name IS '" + owner.getName() + "')", null);

        int num = cursor.moveToFirst() ? cursor.getInt(0) : 0;

        cursor.close();

        return num;
    }

    public Character[] getAllCharactersFrom(Project owner)
    {
        // Doing things INEFFICIENTLY

        int count = getCharacterCountIn(owner);
        Log.d("DB", "Count: " + count);
        if (count < 1)
        {
            return new Character[0];
        }

        Character ret[] = new Character[count];

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CHAR_TABLE + " WHERE (prj_name IS " +  owner.getName() + ")", null);
        if (!cursor.moveToFirst())
        {
            return null;
        }

        for (int i = 0; i < count; i++)
        {
            // Could call getCharacter(owner, character.name), but that'd be slow
            try
            {
                ret[i] = resolveCharacter(owner, cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor, db);
                Log.d("DB", "Loaded character name: " + ret[i].getName());
            }
            catch (Exception e)
            {
                Log.d("DB", "Error loading character: " + e.toString());
            }

            if (!cursor.moveToNext())
            {
                break;
            }
        }

        return ret;
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

       // return db.query(tableName, null, null, null, null, null, null);
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
        db.execSQL("DROP TABLE IF EXISTS " + CHAR_TABLE);   // Erases character table
        db.execSQL("DROP TABLE IF EXISTS " + GAMC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RPG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        // TODO: Drop other tables

        onCreate(db);

        return true;
    }

    private GameDatabaseHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }
}

/* For historical reasons, this is the original database defined in Kotlin
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