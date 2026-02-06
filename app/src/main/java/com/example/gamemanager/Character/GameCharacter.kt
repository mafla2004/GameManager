package com.example.gamemanager.Character

import com.example.gamemanager.Project

open class GameCharacter(
    ownerProject: Project,
    name:                       String,
    species:                    String,
    birth:                      String,
    age:                        String,
    private var maxHealth:      UInt
): Character(ownerProject, name, species, birth, age)
{
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String): this(ownerProject, name, species, birth, age, 100U)

    public fun getMaxHealth(): UInt = maxHealth

    public fun setMaxHealth(newMaxHealth: UInt) { maxHealth = newMaxHealth }
}