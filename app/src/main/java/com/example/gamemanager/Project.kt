package com.example.gamemanager

class Project(private var Name: String = "New Project", private var Description: String = "Project Description")
{
    private var characters: MutableSet<Character> = mutableSetOf()

    init
    {

    }
}