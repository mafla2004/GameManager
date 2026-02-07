package com.example.gamemanager

import android.content.ContentValues
import com.example.gamemanager.Character.Character
import com.example.gamemanager.Items.GameItem

class Project(private var Name: String = "New Project", private var Description: String = "Project Description"): Saveable
{
    private var characters: MutableList<Character>  = mutableListOf()
    private var items:      MutableList<GameItem>   = mutableListOf()

    init
    {

    }

    // GETTERS
    public fun getName():          String = Name
    public fun getDescription():   String = Description
    public fun getCharacters():    Collection<Character> = characters

    // TODO: Maybe for setters introduce an autosave function, or just be lazy AF and have manual save -_-
    // SETTERS AND OTHER MODIFIER METHODS
    public fun setName(newName: String)            { Name = newName }
    public fun setDescription(newDesc: String)     { Description = newDesc }
    fun setCharacters(chars: Array<Character>)     { characters = chars.toMutableList() }
    fun setItems(itArr: Array<GameItem>)            { items = itArr.toMutableList() }

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