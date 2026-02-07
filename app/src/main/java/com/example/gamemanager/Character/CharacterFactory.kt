package com.example.gamemanager.Character

import com.example.gamemanager.Project

const val STANDARD_CHARACTER: Int = 0
const val GAME_CHARACTER:     Int = 1
const val RPG_CHARACTER:      Int = 2

class CharacterFactory(private val ownerProject: Project)
{
    public fun createCharacter(
        type:       Int,
        name:       String,
        species:    String,
        birth:      String,
        age:        String): Character =
        when (type)
        {
            STANDARD_CHARACTER ->  Character(ownerProject, name, species, birth, age)
            GAME_CHARACTER ->  GameCharacter(ownerProject, name, species, birth, age)
            RPG_CHARACTER -> RPGCharacter(ownerProject, name, species, birth, age)
            else -> Character(ownerProject, name, species, birth, age)  // Could omit standard character field, but IDK if I wanna implement smth else
        }
}