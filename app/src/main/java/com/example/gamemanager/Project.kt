package com.example.gamemanager

class Project(private var Name: String = "New Project", private var Description: String = "Project Description") : Jsonable()
{
    private var characters: MutableSet<Character> = mutableSetOf()

    init
    {

    }

    override fun toJson(): String
    {

    }
}