package com.example.gamemanager

import android.database.Cursor
import android.os.Bundle
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

        if (!cursor.moveToFirst())
        {
            Toast.makeText(this, "Project doesn't exist, going back", Toast.LENGTH_SHORT).show()
            finish()
        }

        var projDescription: String = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        var project: Project = Project(projectName.toString(), projDescription) // Converting string to string, WELCOME TO FUCKING KOTLIN
        project.setCharacters(database.getAllCharactersFrom(project))
        project.setItems(database.getAllItemsIn(project))

        // TODO: Make Recycler View of character

        // TODO: Make Recycler View of items

    }
}