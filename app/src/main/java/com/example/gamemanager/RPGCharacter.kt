package com.example.gamemanager

open class RPGCharacter(
    ownerProject:               Project,
    name:                       String,
    species:                    String,
    birth:                      String,
    age:                        String,
    maxHealth:                  UInt,
    private var curHealth:      UInt,
    private var owner:          String
): GameCharacter(ownerProject, name, species, birth, age, maxHealth)
{
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String, maxHealth: UInt, owner: String): this(ownerProject, name, species, birth, age, maxHealth, maxHealth, owner)
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String, owner: String): this(ownerProject, name, species, birth, age, 10U, owner)
    constructor(ownerProject: Project, name: String, species: String, birth: String, age: String): this(ownerProject, name, species, birth, age, "Unspecified")

    public fun getCurrentHealth(): UInt = curHealth

    public fun setCurrentHealth(newHealth: UInt) { curHealth = newHealth }
}