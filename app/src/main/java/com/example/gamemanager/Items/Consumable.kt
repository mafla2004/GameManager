package com.example.gamemanager.Items

import android.accessibilityservice.GestureDescription
import com.example.gamemanager.Project

class Consumable(
    ownerProject:       Project,
    name:               String,
    description:        String,
    private var uses:   Int,
    private var effect: String
): GameItem()
{
}