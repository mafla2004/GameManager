package com.example.gamemanager.Items

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project
import com.example.gamemanager.Saveable

/*
*   Repurposed this class because I noticed this project was building lots of redundancy.
*   For example, there used to be another framework in the works for skills, but aside from the surface
*   concept, there isn't much difference at all between skills and items: both can be described through the
*   same attributes in the programming language and both would have the same "inheritance tree" with the same
*   characteristics, so I'm fusing them together.
* */
open class GameItem(
    private val ownerProject:   Project,
    private var name:           String,
    private var description:    String
): Saveable
{
    override fun getContentValues(): Array<ContentValues>
    {
        val cv: ContentValues = ContentValues().apply {
            put("prj_name", ownerProject.getName())
            put("name", name)
            put("description", description)
        }

        return arrayOf(cv)
    }

    override fun getTables(): Array<String> = arrayOf(GameDatabaseHelper.ITEM_TABLE)

    fun getOwnerProject(): Project = ownerProject

    public fun getName():          String = name
    public fun getDescription():   String = description

    public fun setName(newName: String)         { name = newName }
    public fun setDescription(newDesc: String)  { description = newDesc}
}