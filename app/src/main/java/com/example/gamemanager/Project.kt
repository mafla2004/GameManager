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

        var ret: String = "{\n$tabs\"type\":\"Project\",\n$tabs\"subtype\":\"$SUBTYPE_NONE\",\n"

        ret += "$tabs\"name\":\"$Name\",\n\"description\":\"$Description\",\n"
        ret += tabs +"\"characters:\"[\n"

        for (c: Character in characters)
        {
            ret += c.toJson(if (tabNum != Parser.DONT_USE_TABS) tabNum + 1 else tabNum) + ",\n"
        }
        ret += "$tabs],\n"

        // As always, imposter syndrome is chiming in telling me this is the worst piece of software mankind ever made



        return ret
    }

    override fun getType(): String = "Project"
}