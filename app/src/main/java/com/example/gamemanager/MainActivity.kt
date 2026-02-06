package com.example.gamemanager

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Declare UI components
        val newProjectButton:   Button          = findViewById(R.id.addProjectButton)
        val clearPrjButton:     Button          = findViewById(R.id.clearPrjButton)
        val projectScroller:    RecyclerView    = findViewById(R.id.projectScroller)

        val database: GameDatabaseHelper = GameDatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newProjectButton.setOnClickListener {
            val newProjectIntent: Intent = Intent(this, NewProjectDialogActivity::class.java)
            startActivity(newProjectIntent)
        }

        var projects: MutableList<Project> = mutableListOf()
        try
        {
            val projCursor: Cursor = database.getAllEntriesFromTable(GameDatabaseHelper.PROJ_TABLE)

            if (projCursor.moveToFirst())
            {
                do
                {
                    val name:   String = projCursor.getString(projCursor.getColumnIndexOrThrow("name"))
                    val descr:  String = projCursor.getString(projCursor.getColumnIndexOrThrow("description"))
                    // TODO: Once implemented in the DB, add a functionality to read the characters and other important voices for projects

                    val project: Project = Project(name, descr)
                    projects.add(project)

                    // TODO: Implement functionality that adds button to the scrollview
                } while(projCursor.moveToNext())
            }
            else
            {
                Toast.makeText(this, "ERROR READING FROM DATABASE: Invalid table or db is empty", Toast.LENGTH_SHORT).show()
            }

            projCursor.close()  // I suppose?
        }
        catch (e: RuntimeException)
        {
            Toast.makeText(this, "ERROR: projects table doesn't exist", Toast.LENGTH_LONG).show()
        }

        clearPrjButton.setOnClickListener {
            val intent: Intent = Intent(this, RequestClearConfirmDialog::class.java)
            startActivity(intent)
        }

        // TODO: Add buttons corresponding to projects, maybe also add sorting to projects
    }
}