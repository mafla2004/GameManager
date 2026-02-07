package com.example.gamemanager.Items

import android.accessibilityservice.GestureDescription
import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project

class Consumable(
    ownerProject:       Project,
    name:               String,
    description:        String,
    private var uses:   Int,
    private var effects:String
): GameItem(ownerProject, name, description)
{
    // Getters
    public fun getUses():       Int     = uses
    public fun getEffects():    String  = effects

    // Setters
    public fun setUses(newUses: Int)            { uses = newUses }
    public fun setEffects(newEffects: String)   { effects = newEffects }

    // Saveable functions

    override fun getContentValues(): Array<ContentValues> = super.getContentValues() + ContentValues().apply {
        put("prj_name", getOwnerProject().getName())
        put("name", getName())
        put("uses", uses)
        put("effects", effects)
    }

    override fun getTables(): Array<String> = super.getTables() + GameDatabaseHelper.CONS_TABLE
}