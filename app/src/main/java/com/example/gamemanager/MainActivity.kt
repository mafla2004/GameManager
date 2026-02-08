package com.example.gamemanager

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
    // GENERAL PROJECTS TODOS
    // TODO: Refactor XML code to use more constants instead of hardcoded literals
    // TODO: Refactor database for same reason, the way SQLite code is handled is very error prone
    // TODO: VERY IMPORTANT - Add sensor code and logic to switch between dark mode and light mode depending on illumination
    // For the above - see if it is possble to define a color variable in XML and assign all elements in the XML their color variables,
    // then switch value of color variable as needed.

    private lateinit var database: GameDatabaseHelper
    private lateinit var scroller: RecyclerView

    private fun onRecyclerClick(project: String)
    {
        val intent: Intent = Intent(this, ProjectViewerActivity()::class.java)
        intent.putExtra("name", project)
        startActivity(intent)
    }

    private fun updateScroller()
    {
        var projects: MutableList<String> = mutableListOf()
        try
        {
            val projCursor: Cursor = database.getAllEntriesFromTable(GameDatabaseHelper.PROJ_TABLE)

            Log.d("DB", "${projCursor.count}")

            if (projCursor.moveToFirst())
            {
                do
                {
                    // We just get the name of the projects instead of loading them all
                    val name:   String = projCursor.getString(projCursor.getColumnIndexOrThrow("name"))

                    projects.add(name)
                } while (projCursor.moveToNext())   // Fun fact - IDK if it was a typo or autocomplete or what, but this was moveToFirst(), which caused an infinite loop and stack overflow
            }

            val projectRecyclerAdapter: ProjectRecyclerAdapter = ProjectRecyclerAdapter(projects.toTypedArray()) { project ->
                onRecyclerClick(project)
            }
            // Kotlin be like:
            // "toArray()" That is the fakest thing I've ever seen in my life
            // "toTypedArray()" HOLY CRAP!!! OwO

            scroller.layoutManager = LinearLayoutManager(this)
            scroller.adapter = projectRecyclerAdapter
        }
        catch (e: RuntimeException)
        {
            Toast.makeText(this, "ERROR: projects table doesn't exist", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Declare UI components
        val newProjectButton:   Button          = findViewById(R.id.addProjectButton)
        val clearPrjButton:     Button          = findViewById(R.id.clearPrjButton)

        scroller = findViewById(R.id.projectScroller)

        database = GameDatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bckgr)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newProjectButton.setOnClickListener {
            val newProjectIntent: Intent = Intent(this, NewProjectDialogActivity::class.java)
            startActivity(newProjectIntent)
        }

        updateScroller()

        clearPrjButton.setOnClickListener {
            val intent: Intent = Intent(this, RequestClearConfirmDialog::class.java)
            startActivity(intent)
        }
    }

    override fun onResume()
    {
        super.onResume()

        updateScroller()
    }
}