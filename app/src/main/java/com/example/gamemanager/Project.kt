package com.example.gamemanager

class Project(private var Name: String = "New Project", private var Description: String = "Project Description") : Serializeable()
{
    init
    {

    }

    override fun save() = false         // TODO: Implement
    override fun wipe() {}              // TODO: Implement
    override fun isMemorized() = false  // TODO: Implement
}