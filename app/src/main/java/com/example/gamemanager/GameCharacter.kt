package com.example.gamemanager

open class GameCharacter(
    private val component: Character,
    private var maxHealth: UInt
): CharacterDecorator(component)
{
    constructor(component: Character): this(component, 100U)

    public fun getMaxHealth(): UInt = maxHealth

    public fun setMaxHealth(newMaxHealth: UInt) { maxHealth = newMaxHealth }
}