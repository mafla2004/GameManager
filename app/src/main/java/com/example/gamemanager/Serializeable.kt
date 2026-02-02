package com.example.gamemanager


abstract class Serializeable
{
    abstract fun save(): Boolean
    abstract fun wipe()                     // Wipes object from memory
    abstract fun isMemorized(): Boolean     // Returns true if object is memorized in data.xml
}