package com.example.gamemanager

import android.content.ContentValues
import com.example.gamemanager.Character.Character

class Project(private var Name: String = "New Project", private var Description: String = "Project Description"): Saveable
{
    private var characters: MutableSet<Character> = mutableSetOf()

    init
    {

    }

    // GETTERS
    public fun getName():          String = Name
    public fun getDescription():   String = Description

    // TODO: Maybe for setters introduce an autosave function, or just be lazy AF and have manual save -_-
    // SETTERS AND OTHER MODIFIER METHODS
    public fun setName(newName: String)            { Name = newName }
    public fun setDescription(newDesc: String)     { Description = newDesc }

    override fun getContentValues(): Array<ContentValues>
    {
        val cv: ContentValues = ContentValues().apply {
            put("name", Name)
            put("description", Description)
        }

        return arrayOf(cv)
    }

    override fun getTables(): Array<String> = arrayOf(GameDatabaseHelper.PROJ_TABLE)
}