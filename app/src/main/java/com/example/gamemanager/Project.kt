package com.example.gamemanager

class Project(private var Name: String = "New Project", private var Description: String = "Project Description") : Jsonable()
{
    private var characters: MutableSet<Character> = mutableSetOf()

    init
    {

    }

    override fun toJson(tabNum: Int): String
    {
        var tabs: String = ""
        if (tabNum > 0)
            for (i in 1..tabNum)
            {
                tabs += '\t'
            }

        var ret: String = tabs + "\"type\":\"Project\",\n\"subtype\":\"none\",\n"

        // TODO: Make a function that automatically writes out attributes

        return ret
    }

    override fun getType(): String = "Project"
}