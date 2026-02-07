package com.example.gamemanager

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProjectViewerActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_project_viewer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = getIntent()

        // Imagine passing null as the critical parameter on which the entire activity depends on
        var projectName: String? = intent.getStringExtra("name")

        if (projectName == null)
        {
            Toast.makeText(this, "RECEIVED NULL NAME - GOING BACK", Toast.LENGTH_SHORT).show()
            finish()
        }

        // TODO: Probably can be made better rather than having to redo the query
        val database: GameDatabaseHelper = GameDatabaseHelper.getInstance(this)
        val cursor: Cursor = database.getProjectFromName(projectName)

        Log.d("DB", "Projects found with name $projectName: ${cursor.count}")

        if (!cursor.moveToFirst())
        {
            finish()
        }

        var projDescription: String = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        var project: Project = Project(projectName.toString(), projDescription) // Converting string to string, WELCOME TO FUCKING KOTLIN
        project.setCharacters(database.getAllCharactersFrom(project))
        project.setItems(database.getAllItemsIn(project))

        // TODO: Make Recycler View of character

        // TODO: Make Recycler View of items

        // Declare UI elements
        val descriptionEditView:    EditText    = findViewById(R.id.descriptionEdit)
        val projectHeader:          TextView    = findViewById(R.id.projectHeader)
        val saveButton:             Button      = findViewById(R.id.saveBtn)
        val loadButton:             Button      = findViewById(R.id.loadBtn)

        projectHeader.text = projectName
        descriptionEditView.setText(if (projDescription.isEmpty()) "Project description goes here..." else projDescription)

        saveButton.setOnClickListener {
            // NOTE: The fields that aren't that easy to update automatically, like those of EditTexts for some reason, must be saved here
            project.setDescription(descriptionEditView.text.toString())

            if (database.updateObject(project))
            {
                Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show()
            }
        }

        loadButton.setOnClickListener {
            // TODO: Add loading functionality you goober
        }
    }
}