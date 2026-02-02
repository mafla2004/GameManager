package com.example.gamemanager

open class RPGCharacter(
    private val component: Character,
    private var maxHealth: UInt,
    private var curHealth: UInt,
    private var owner:     String
): GameCharacter(component, maxHealth)
{
    constructor(component: Character, maxHealth: UInt, owner: String): this(component, maxHealth, maxHealth, owner)
    constructor(component: Character, owner: String): this(component, 10U, owner)
    constructor(component: Character): this(component, "Unspecified")

    public fun getCurrentHealth(): UInt = curHealth

    public fun setCurrentHealth(newHealth: UInt) { curHealth = newHealth }
}