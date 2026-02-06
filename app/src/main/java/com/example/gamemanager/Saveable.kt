package com.example.gamemanager

import android.content.ContentValues

// Interface for objects that can be saved and loaded from the database
interface Saveable
{
    fun getNullColumnHacks(): String? = null
    fun getContentValues(): Array<ContentValues>
    fun getTables(): Array<String>
}