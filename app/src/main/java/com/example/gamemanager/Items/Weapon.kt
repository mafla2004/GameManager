package com.example.gamemanager.Items

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project

open class Weapon(
    ownerProject:       Project,
    name:               String,
    description:        String,
    private var damage: Int
): GameItem(ownerProject, name, description)
{
    override fun getContentValues(): Array<ContentValues> = super.getContentValues() + ContentValues().apply {
        put("prj_name", getOwnerProject().getName())
        put("name", getName())
        put("dmg", damage)
    }

    override fun getTables(): Array<String> = super.getTables() + GameDatabaseHelper.WEPN_TABLE

    public fun getDamage(): Int = damage

    public fun setDamage(newDmg: Int) { damage = newDmg }
}