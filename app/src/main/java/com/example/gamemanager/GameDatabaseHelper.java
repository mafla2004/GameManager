package com.example.gamemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.sax.Element;
import android.util.Log;
import com.example.gamemanager.Character.Character;
import com.example.gamemanager.Character.GameCharacter;
import com.example.gamemanager.Character.RPGCharacter;
import com.example.gamemanager.Items.Consumable;
import com.example.gamemanager.Items.GameItem;
import com.example.gamemanager.Items.RangedWeapon;
import com.example.gamemanager.Items.Weapon;
import com.example.gamemanager.NarrativeElements.NarrativeElement;

public class GameDatabaseHelper extends  SQLiteOpenHelper
{
    // This database, in order to work with the frameworks of character and, in the future, of item, which both have multiple subclasses,
    // must be at least in 3rd normal form.

    // TODO: Refactor at some point, this is horrible

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
    public static final String RGDW_TABLE = "ranged_weapon";

    public static final String CONS_TABLE = "consumables";

    // Table for narrative elements
    public static final String NELM_TABLE = "narrative_elements";

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
                "species TEXT, birth TEXT, age TEXT, aspect TEXT, personality TEXT, backstory TEXT, PRIMARY KEY (prj_name, name))");
        // {parent project, name} -> {species, birth, age etc...}

        // This table contains the extra information of characters of class GameCharacter, not the information of its subclasses
        db.execSQL("CREATE TABLE " + GAMC_TABLE + " (prj_name TEXT, name TEXT, max_health INTEGER, PRIMARY KEY (prj_name, name))");

        // This table contains the extra information of characters of class RPGCharacter
        db.execSQL("CREATE TABLE " + RPG_TABLE + " (prj_name TEXT, name TEXT, cur_health INTEGER, owner TEXT, PRIMARY KEY (prj_name, name))");

        // ITEM FRAMEWORK

        db.execSQL("CREATE TABLE " + ITEM_TABLE + " (prj_name TEXT, name TEXT, description TEXT, PRIMARY KEY (prj_name, name))");

        db.execSQL("CREATE TABLE " + WEPN_TABLE + " (prj_name TEXT, name TEXT, dmg INTEGER, PRIMARY KEY (prj_name, name))");

        db.execSQL("CREATE TABLE " + RGDW_TABLE + " (prj_name TEXT, name TEXT, reach TEXT, ammo TEXT, PRIMARY KEY (prj_name, name))");

        db.execSQL("CREATE TABLE " + CONS_TABLE + " (prj_name TEXT, name TEXT, uses INTEGER, effects TEXT, PRIMARY KEY (prj_name, name))");

        // NARRATIVE ELEMENT FRAMEWORK

        db.execSQL("CREATE TABLE " + NELM_TABLE + " (prj_name TEXT, name TEXT, type INTEGER, start_d TEXT, end_d TEXT, PRIMARY KEY (prj_name, name))");
    }

    @Override
    public void onUpgrade(
        SQLiteDatabase db,
        int oldVersion,
        int newVersion
    )
    {
        clearDatabase();
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

        /* DEBUG */
        if (obj instanceof Project)
        {
            Log.d("DB", "Project description is: " + ((Project)obj).getDescription());
        }

        return true;
    }

    public synchronized boolean removeObject(Saveable obj)
    {
        SQLiteDatabase db = getWritableDatabase();

        String[] tables = obj.getTables();

        for (String table : tables)
        {
            // TODO: Make more robust because if one deletion goes wrong you have garbage data
            long res = db.delete(table, obj.getWhereClause(), null);
            if (res < 1)
            {
                return false;
            }
        }

        return true;
    }

    public synchronized boolean updateObject(Saveable obj)
    {
        if (!removeObject(obj))
        {
            Log.d("DB", "Removal went wrong during update");
            return false;
        }

        return addObject(obj);
    }

    private GameItem resolveItem(Project owner, String name, Cursor cursor, SQLiteDatabase db)
    {
        GameItem ret;

        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        Cursor aux_cursor = db.rawQuery("SELECT * FROM " + WEPN_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

        if (aux_cursor.moveToFirst())
        {
            int damage = aux_cursor.getInt(aux_cursor.getColumnIndexOrThrow("dmg"));

            aux_cursor.close();

            // Check if it is a ranged weapon now TwT
            aux_cursor = db.rawQuery("SELECT * FROM " + RGDW_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

            if (aux_cursor.moveToFirst())
            {
                String reach    = aux_cursor.getString(aux_cursor.getColumnIndexOrThrow("reach"));
                String ammo     = aux_cursor.getString(aux_cursor.getColumnIndexOrThrow("ammo"));

                ret = new RangedWeapon(owner, name, description, damage, reach, ammo);
            }
            else
            {
                ret = new Weapon(owner, name, description, damage);
            }
        }
        else
        {
            aux_cursor.close();

            // Check if it is a consumable
            aux_cursor = db.rawQuery("SELECT * FROM " + CONS_TABLE + " WHERE (prj_name IS '" + owner.getName() + "') AND (name IS '" + name + "')", null);

            if (aux_cursor.moveToFirst())
            {
                int uses = aux_cursor.getInt(aux_cursor.getColumnIndexOrThrow("uses"));
                String effects = aux_cursor.getString(aux_cursor.getColumnIndexOrThrow("effects"));

                ret = new Consumable(owner, name, description, uses, effects);
            }
            else
            {
                ret = new GameItem(owner, name, description);
            }
        }

        aux_cursor.close();

        return ret;
    }

    public NarrativeElement resolveNarrativeElement(Project owner, String name, Cursor cursor, SQLiteDatabase db)
    {
        NarrativeElement ret;

        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String start = cursor.getString(cursor.getColumnIndexOrThrow("start_d"));
        String end = cursor.getString(cursor.getColumnIndexOrThrow("end_d"));
        int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));

        ret = new NarrativeElement(owner, name, type, description, start, end);

        return ret;
    }

    public GameItem[] getAllItemsIn(Project project)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ITEM_TABLE + " WHERE (prj_name IS '" + project.getName() + "')", null);
        int count = cursor.getCount();

        if (!cursor.moveToFirst())
        {
            return new GameItem[0];
        }

        GameItem items[] = new GameItem[count];

        for (int i = 0; i < count; i++)
        {
            items[i] = resolveItem(project, cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor, db);
            cursor.moveToNext();
        }

        return items;
    }

    public NarrativeElement[] getAllNarrativeElementsIn(Project project)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NELM_TABLE + " WHERE (prj_name IS '" + project.getName() + "')", null);
        int count = cursor.getCount();

        if (!cursor.moveToFirst())
        {
            return new NarrativeElement[0];
        }

        NarrativeElement nelms[] = new NarrativeElement[count];

        for (int i = 0; i < count; i++)
        {
            nelms[i] = resolveNarrativeElement(project, cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor, db);
        }

        return nelms;
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
        String backstory    = cursor.getString(cursor.getColumnIndexOrThrow("backstory"));
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
        ret.setBackstory(backstory);
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
        String backstory    = cursor.getString(cursor.getColumnIndexOrThrow("backstory"));
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
        ret.setBackstory(backstory);
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
        // TODO: Remake, make it similar to getAllItemsIn(project)

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

    public Cursor getAllEntriesFromTable(String tableName)
    {
        SQLiteDatabase db = getReadableDatabase();

       // return db.query(tableName, null, null, null, null, null, null);
       return db.rawQuery("SELECT * FROM " + tableName, null);
    }

    public Cursor getProjectFromName(String projName)
    {
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + PROJ_TABLE + " WHERE (name IS '" + projName + "')", null);
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
        db.execSQL("DROP TABLE IF EXISTS " + WEPN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RGDW_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NELM_TABLE);
        // TODO: Drop other tables

        onCreate(db);

        return true;
    }

    private GameDatabaseHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }
}