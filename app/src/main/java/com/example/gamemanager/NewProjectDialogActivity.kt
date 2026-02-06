package com.example.gamemanager

import android.os.Bundle
import android.widget.Toast     // Toast, just like me!
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewProjectDialogActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_new_project_dialog)

        // Declare UI elements
        val goBackButton:           Button   = findViewById(R.id.goBackButton)
        val createProjectButton:    Button   = findViewById(R.id.createProjectButton)
        val projectNameText:        TextView = findViewById(R.id.projectNameText)
        val projectDescriptionText: TextView = findViewById(R.id.projectDescriptionText)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database: GameDatabaseHelper = GameDatabaseHelper.getInstance(this)

        // Set Behaviour for create project button
        createProjectButton.setOnClickListener {
            // Display a warning and don't do anything else if the name field is empty.
            // A project must have a name but may not have a description
            if (projectNameText.text.isBlank())
            {
                Toast.makeText(this, "Project must have a name", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val newProject: Project = Project(projectNameText.text.toString(), projectDescriptionText.text.toString())
                database.addObject(newProject)
                // TODO: Maybe open project?

                finish()
            }
        }

        // Set behaviour for go back button
        goBackButton.setOnClickListener {
            // TODO: Clear text fields maybe?
            finish() // Terminate activity
        }
    }
}
