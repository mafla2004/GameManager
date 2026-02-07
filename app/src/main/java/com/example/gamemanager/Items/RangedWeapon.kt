package com.example.gamemanager.Items

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project

class RangedWeapon(
    ownerProject:       Project,
    name:               String,
    description:        String,
    damage:             Int,
    private var reach:  String,             // Using string cause one may write "3 meters", "4 tiles", or just "high range", can't be named "range" lest SQL gets mad
    private var ammo:   String              // Which type of ammo it uses, user may also write "none"
): Weapon(ownerProject, name, description, damage)
{
    // Getters
    public fun getReach():  String = reach
    public fun getAmmo():   String = ammo

    // Setters
    public fun setReach(newReach: String)   { reach = newReach }
    public fun setAmmo(newAmmo: String)     { ammo = newAmmo}

    // Saveable methods

    override fun getTables(): Array<String> = super.getTables() + GameDatabaseHelper.RGDW_TABLE

    override fun getContentValues(): Array<ContentValues> = super.getContentValues() + ContentValues().apply {
        put("prj_name", getOwnerProject().getName())
        put("name", getName())
        put("reach", reach)
        put("ammo", ammo)
    }
}