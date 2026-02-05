package com.example.gamemanager

abstract class Jsonable()
{
    abstract fun toJson(formatted: Boolean): String
    abstract fun getType(): String
    abstract fun makeField(field: String, value: String): Boolean
}