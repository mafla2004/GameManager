package com.example.gamemanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Declare UI components
        val newProjectButton:   Button      = findViewById(R.id.addProjectButton)
        val clearPrjButton:     Button      = findViewById(R.id.clearPrjButton)
        val projectScroller:    ScrollView  = findViewById(R.id.projectScroller)

        // TODO: Instantiate database

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newProjectButton.setOnClickListener {
            val newProjectIntent: Intent = Intent(this, NewProjectDialogActivity::class.java)
            startActivity(newProjectIntent)
        }

        var projects: List<Project> // TODO: Read projects from memory

        clearPrjButton.setOnClickListener {

        }

        // TODO: Add buttons corresponding to projects, maybe also add sorting to projects
    }
}