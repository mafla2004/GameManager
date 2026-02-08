package com.example.gamemanager

import android.content.ContentValues
import android.widget.Toast
import com.example.gamemanager.Character.Character
import com.example.gamemanager.Items.GameItem
import com.example.gamemanager.NarrativeElements.NarrativeElement

class Project(private var Name: String = "New Project", private var Description: String = "Project Description"): Saveable
{
    private var characters: MutableList<Character>          = mutableListOf()
    private var items:      MutableList<GameItem>           = mutableListOf()
    private var narrative:  MutableList<NarrativeElement>   = mutableListOf()

    init
    {

    }

    // GETTERS
    public fun getName():               String                          = Name
    public fun getDescription():        String                          = Description
    public fun getCharacters():         Collection<Character>           = characters
    public fun getItems():              Collection<GameItem>            = items
    public fun getNarrativeElements():  Collection<NarrativeElement>    = narrative

    // TODO: Maybe for setters introduce an autosave function, or just be lazy AF and have manual save -_- | ADDENDUM: I'm lazy AF
    // SETTERS AND OTHER MODIFIER METHODS
    public fun setName(newName: String)            { Name = newName }
    public fun setDescription(newDesc: String)     { Description = newDesc }
    fun setCharacters(chars: Array<Character>)     { characters = chars.toMutableList() }
    fun setItems(itArr: Array<GameItem>)           { items = itArr.toMutableList() }

    fun setNarrative(nArr: Array<NarrativeElement>){ narrative = nArr.toMutableList() }

    fun addCharacter(char: Character): Boolean
    {
        for (c in characters)
        {
            if (char.getName().equals(c.getName()))
            {
                return false
            }
        }
        characters.add(char)
        return true
    }

    fun removeCharacter(charName: String)
    {
        for (c in characters)
        {
            if (c.getName().equals(charName))
            {
                characters.remove(c)
                return
            }
        }
    }

    override fun getContentValues(): Array<ContentValues>
    {
        val cv: ContentValues = ContentValues().apply {
            put("name", Name)
            put("description", Description)
        }

        return arrayOf(cv)
    }

    override fun getTables(): Array<String> = arrayOf(GameDatabaseHelper.PROJ_TABLE)

    override fun getWhereClause(): String = "(name IS '" + Name + "')"
}