package com.example.gamemanager.NarrativeElements

import android.content.ContentValues
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project
import com.example.gamemanager.Saveable

/*
*   Could have made these different classes, but due to lack of time we have to work smarter instead of harder.
*   A narrative element will have the basic elements to represent a nation, a location, an event or a war.
*   Under the hood, the way data is represented stays the same, on the surface it is presented in different ways
*   based on these values
* */

const val LOCATION: Int = 0
const val FACTION: Int = 1
const val EVENT: Int = 2

/*
*   Every element has a set of defining characteristics which can be boiled down to information that can be stored in the same fields:
*   namely, a description is common for all, it can be an in-depth description of an event or of the dynamics of a war of of the ideals of a nation,
*   a date of start and end would be common for all except for location, a location can't begin and end, but we can have a date of discovery or settlement
*   and a date of disbandment.
* */
class NarrativeElement(
    private val ownerProject:   Project,
    private var name:           String,
    private var type:           Int,
    private var description:    String,
    private var startDate:      String,
    private var endDate:        String
): Saveable
{
    // Getters
    public fun getOwnerProject():   Project     = ownerProject
    public fun getName():           String      = name
    public fun getType():           Int         = type
    public fun getDescription():    String      = description
    public fun getStartDate():      String      = startDate
    public fun getEndDate():        String      = endDate

    // Setters
    fun setName(newName: String)        { name = newName }
    fun setDescription(newDesc: String) { description = newDesc }
    fun setType(newType: Int)           { type = newType }
    fun setStartDate(newStart: String)  { startDate = newStart }
    fun setEndDate(newEnd: String)      { endDate = newEnd }

    override fun getContentValues(): Array<ContentValues>
    {
        val cv: ContentValues = ContentValues().apply {
            put("prj_name", ownerProject.getName())
            put("name", name)
            put("type", type)
            put("description", description)
            put("start_d", startDate)
            put("end_d", endDate)
        }

        return arrayOf(cv)
    }

    override fun getTables(): Array<String> = arrayOf(GameDatabaseHelper.NELM_TABLE)

    override fun getWhereClause(): String = "(prj_name IS '${ownerProject.getName()}') AND (name IS '$name')"
}