package com.example.gamemanager

val SUBTYPE_NONE: String = "none"   // Keyword for none subtype

abstract class Jsonable()
{
    abstract fun toJson(tabNum: Int): String
    abstract fun getType(): String
    //abstract fun makeField(field: String, value: String): Boolean
}