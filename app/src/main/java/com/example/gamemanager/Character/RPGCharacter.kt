package com.example.gamemanager.Character

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project

open class RPGCharacter(
    ownerProject:               Project,
    name:                       String,
    species:                    String,
    birth:                      String,
    age:                        String,
    maxHealth:                  Int,
    private var curHealth:      Int,
    private var owner:          String
): GameCharacter(ownerProject, name, species, birth, age, maxHealth)
{
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String, maxHealth: Int, owner: String): this(ownerProject, name, species, birth, age, maxHealth, maxHealth, owner)
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String, owner: String): this(ownerProject, name, species, birth, age, 10, owner)
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String): this(ownerProject, name, species, birth, age, "Unspecified")

    public fun getCurrentHealth(): Int = curHealth

    public fun setCurrentHealth(newHealth: Int) { curHealth = newHealth }

    override fun getTables(): Array<String> = super.getTables() + GameDatabaseHelper.RPG_TABLE

    override fun getContentValues(): Array<ContentValues> = super.getContentValues() + ContentValues().apply {
        put("prj_name", getOwnerProject().getName())
        put("name", getName())
        put("cur_health", curHealth)

    }
}