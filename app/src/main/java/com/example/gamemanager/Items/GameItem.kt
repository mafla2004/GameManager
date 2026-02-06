package com.example.gamemanager.Items

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project
import com.example.gamemanager.Saveable

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