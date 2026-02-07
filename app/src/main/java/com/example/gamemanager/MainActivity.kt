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
                } while (projCursor.moveToNext())   // Fun fact - IDK if it was a typo or autocomplete or what, but this was moveToFirst(), which caused stack overflow
            }

            val projectRecyclerAdapter: ProjectRecyclerAdapter = ProjectRecyclerAdapter(projects.toTypedArray()) { project ->
                onRecyclerClick(project)
            }
            // Kotlin be like:
            // "toArray()" That is the fakest shit I've ever seen in my life
            // "toTypedArray()" HOLY SHIT!!! OwO

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

        scroller = findViewById(R.id.projectScroller)   // This one be special

        database = GameDatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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