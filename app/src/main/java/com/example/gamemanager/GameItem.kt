package com.example.gamemanager

import android.content.ContentValues

open class GameItem(
    private val ownerProject:   Project,
    private var name:           String,
    private var description:    String
): Saveable
{
    override fun getContentValues(): ContentValues
    {
        val ret: ContentValues = ContentValues().apply {
            put("prj_name", ownerProject.getName())
            put("name", name)
            put("description", description)
        }

        return ret
    }

    override fun getTable(): String = GameDatabaseHelper.ITEM_TABLE

    public fun getName():          String = name
    public fun getDescription():   String = description

    public fun setName(newName: String)         { name = newName }
    public fun setDescription(newDesc: String)  { description = newDesc}
}