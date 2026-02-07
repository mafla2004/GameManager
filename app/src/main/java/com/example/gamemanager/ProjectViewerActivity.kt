package com.example.gamemanager

import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView

class ProjectViewerActivity : AppCompatActivity()
{
    private val CHAR_S = 0
    private val ITEM_S = 1
    private val NAR_S = 2

    private fun onCharRecClick(scroller: Int, prj_name: String, name: String)
    {
        val intent: Intent
        when (scroller)
        {
            0 -> intent = Intent(this, CharacterViewerActivity()::class.java)
            1 -> intent = Intent(this, ItemViewerActivity()::class.java)
            2 -> intent = Intent(this, NarrativeViewerActivity()::class.java)
            else -> return
        }

        intent.putExtra("prj_name", prj_name)
        intent.putExtra("name", name)
        startActivity(intent)
    }

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

        // Standard elements
        val descriptionEditView:    EditText    = findViewById(R.id.descriptionEdit)
        val projectHeader:          TextView    = findViewById(R.id.projectHeader)
        val saveButton:             Button      = findViewById(R.id.saveBtn)
        val loadButton:             Button      = findViewById(R.id.loadBtn)
        val delButton:              Button      = findViewById(R.id.delBtn)

        // Character collection elements
        val newCharBtn:             Button      = findViewById(R.id.newCharBtn)
        val clearCharBtn:           Button      = findViewById(R.id.clearCharBtn)
        val charScroller:           RecyclerView= findViewById(R.id.charRecView)

        // Item collection elements
        val newItemBtn:             Button      = findViewById(R.id.newItemBtn)
        val clearItemsBtn:          Button      = findViewById(R.id.clearItemsBtn)
        val itemScroller:           RecyclerView= findViewById(R.id.itemRecView)

        // Narrative Framework elements
        val newLocBtn:              Button      = findViewById(R.id.newLocationBtn)
        val newFacBtn:              Button      = findViewById(R.id.newFactionBtn)
        val newEvtBtn:              Button      = findViewById(R.id.newEventBtn)
        val clearLocBtn:            Button      = findViewById(R.id.clearLocationsBtn)
        val clearFacBtn:            Button      = findViewById(R.id.clearFactionsBtn)
        val clearEvtBtn:            Button      = findViewById(R.id.clearEventsBtn)

        // Standard actions

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

        delButton.setOnClickListener {
            for (c in project.getCharacters())
            {
                database.removeObject(c)
            }

            for (i in project.getItems())
            {
                database.removeObject(i)
            }

            for (n in project.getNarrativeElements())
            {
                database.removeObject(n)
            }

            database.removeObject(project)

            finish()
        }

        // Character collection actions

        newCharBtn.setOnClickListener {

        }

        clearCharBtn.setOnClickListener {

        }

        // Item collection actions

        newItemBtn.setOnClickListener {

        }

        clearItemsBtn.setOnClickListener {

        }

        // Narrative Framework actions

        newLocBtn.setOnClickListener {

        }

        clearLocBtn.setOnClickListener {

        }

        newFacBtn.setOnClickListener {

        }

        clearFacBtn.setOnClickListener {

        }

        newEvtBtn.setOnClickListener {

        }

        clearEvtBtn.setOnClickListener {

        }
    }
}