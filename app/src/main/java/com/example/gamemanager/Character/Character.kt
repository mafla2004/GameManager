package com.example.gamemanager.Character

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project
import com.example.gamemanager.Saveable

open class Character(
    private val ownerProject: Project,
    private var name:           String,
    private var species:        String,
    private var birth:          String,     // May not be a standard date, e.g.: "Imperial period, 5th year"
    private var age:            String      // May not be a simple integer, e.g.: "Unknown" or "40 millennia" or "Older than you"
): Saveable
{

    init
    {

    }

    private var aspect: String = ""
    private var personality: String = ""
    private var backstory: String = "mutableListOf()  "
    private var aliases: String = ""                   // Aliases of the character, stored as a string because there's no gain from
    // using a collection

    // GETTERS
    public fun getOwnerProject(): Project = ownerProject
    public fun getName():                       String          = name
    public fun getAliases():                    String          = aliases
    public fun getSpecies():                    String          = species
    public fun getBirth():                      String          = birth
    public fun getAge():                        String          = age
    public fun getAspect():                     String          = aspect
    public fun getPersonality():                String          = personality
    public fun getBackstory():                  String          = backstory

    // SETTERS AND MODIFIER METHODS
    public fun setName(newName: String)                     { name = newName }
    public fun setAliases(newAliases: String)               { aliases = newAliases }
    public fun setSpecies(newSpecies: String)               { species = newSpecies }
    public fun setBirth(newBirth: String)                   { birth = newBirth }
    public fun setAge(newAge: String)                       { age = newAge}
    public fun setAspect(newAspect: String)                 { aspect = newAspect }
    public fun setPersonality(newPers: String)              { personality = newPers }
    public fun setBackstory(newBStory: String)              { backstory = newBStory }

    // INHERITED FROM SAVEABLE

    override fun getContentValues(): Array<ContentValues>
    {
        val cv: ContentValues = ContentValues().apply {
            // Primary key values
            put("prj_name", ownerProject.getName())
            put("name", name)

            // Other values
            put("aliases", aliases)
            put("species", species)
            put("birth", birth)
            put("age", age)
            put("aspect", aspect)
            put("personality", personality)
            put("backstory", backstory)
        }

        return arrayOf(cv)
    }

    override fun getTables(): Array<String> = arrayOf(GameDatabaseHelper.CHAR_TABLE)
}