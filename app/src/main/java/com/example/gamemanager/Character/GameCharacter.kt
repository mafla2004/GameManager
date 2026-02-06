package com.example.gamemanager.Character

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project

open class GameCharacter(
    ownerProject:               Project,
    name:                       String,
    species:                    String,
    birth:                      String,
    age:                        String,
    private var maxHealth:      Int
): Character(ownerProject, name, species, birth, age)
{
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String): this(ownerProject, name, species, birth, age, 100)

    public fun getMaxHealth(): Int = maxHealth

    public fun setMaxHealth(newMaxHealth: Int) { maxHealth = newMaxHealth }

    override fun getTables(): Array<String> = super.getTables() + GameDatabaseHelper.GAMC_TABLE

    override fun getContentValues(): Array<ContentValues> = super.getContentValues() + ContentValues().apply {
        put("prj_name", getOwnerProject().getName())
        put("name", getName())
        put("max_health", maxHealth)
    }
}