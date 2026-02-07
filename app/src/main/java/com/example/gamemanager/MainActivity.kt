package com.example.gamemanager

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.Recycler.ProjectRecyclerAdapter

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

            Log.d("DB", "${projCursor.count}")

            if (projCursor.moveToFirst())
            {
                do
                {
                    val name:   String = projCursor.getString(projCursor.getColumnIndexOrThrow("name"))
                    val descr:  String = projCursor.getString(projCursor.getColumnIndexOrThrow("description"))
                    // TODO: Once implemented in the DB, add a functionality to read the characters and other important voices for projects

                    val project: Project = Project(name, descr)
                    project.setCharacters(database.getAllCharactersFrom(project)) // ERROR HERE!

                    projects.add(project)
                } while(projCursor.moveToNext())

                val projectRecyclerAdapter: ProjectRecyclerAdapter = ProjectRecyclerAdapter(projects.toTypedArray())
                // Kotlin be like:
                // "toArray()" That is the fakest shit I've ever seen in my life
                // "toTypedArray()" HOLY SHIT!!! OwO

                val projectScroller: RecyclerView = findViewById(R.id.projectScroller)
                projectScroller.layoutManager = LinearLayoutManager(this)
                projectScroller.adapter = projectRecyclerAdapter

                // TODO: Add functionality to buttons
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
    }
}