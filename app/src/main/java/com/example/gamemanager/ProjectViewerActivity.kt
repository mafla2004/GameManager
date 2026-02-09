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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.Character.*
import com.example.gamemanager.Items.CreateItemActivity
import com.example.gamemanager.NarrativeElements.*
import com.example.gamemanager.Recycler.CharacterRecyclerAdapter
import com.example.gamemanager.Recycler.EventRecyclerAdapter
import com.example.gamemanager.Recycler.FactionRecyclerAdapter
import com.example.gamemanager.Recycler.ItemsRecyclerAdapter
import com.example.gamemanager.Recycler.LocationRecyclerAdapter

/*
*   I have spent 5000 years in this activity as of now
*   and by the looks of it I'll spend 5000 more stuck in here
 */
class ProjectViewerActivity : AppCompatActivity()
{
    /*  TODO: You clearly need some rest because you're basically not reasoning at all, here's what you will do:
    *   rest a bit, get your ideas clear, then come back and update this god forsaken activity,
    *   load the entire project on create no matter the memory drawback, then modify the update scrollers functions
    *   to read from the project object, if necessary add a more advanced loading and saving option to the database.
    *   The burnout is real with this one
     */

    private lateinit var project: Project
    private lateinit var database: GameDatabaseHelper
    private lateinit var prj_name: String

    private lateinit var charScroller: RecyclerView
    private lateinit var itemScroller: RecyclerView
    private lateinit var nFacScroller: RecyclerView
    private lateinit var nLocScroller: RecyclerView
    private lateinit var nEvtScroller: RecyclerView

    private val ALL_S = Int.MAX_VALUE
    private val CHAR_S = 0
    private val ITEM_S = 1
    private val NAR_S = 2

    private fun loadProject(): Boolean
    {
        if (AppCommons.getCurrentProject() != null && AppCommons.getCurrentProject()!!.getName().equals(prj_name))
        {
            return true
        }

        // TODO: Probably can be made better rather than having to redo the query
        database = GameDatabaseHelper.getInstance(this)
        val cursor: Cursor = database.getProjectFromName(prj_name)

        Log.d("DB", "Projects found with name $prj_name: ${cursor.count}")

        if (!cursor.moveToFirst())
        {
            return false
        }

        var projDescription: String = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        project = Project(prj_name.toString(), projDescription) // Converting string to string, WELCOME TO KOTLIN
        AppCommons.setCurrentProject(project) // Added afterwards to communicate a project between activities, should probably refactor, but I'm in burnout
        project.setCharacters(database.getAllCharactersFrom(project))
        project.setItems(database.getAllItemsIn(project))

        return true
    }

    private fun onRecyclerClick(scroller: Int, name: String)
    {
        val intent: Intent
        when (scroller)
        {
            CHAR_S -> intent = Intent(this, CharacterViewerActivity()::class.java)
            ITEM_S -> intent = Intent(this, ItemViewerActivity()::class.java)
            NAR_S  -> intent = Intent(this, NarrativeViewerActivity()::class.java)
            else -> return
        }

        //intent.putExtra("prj_name", prj_name) may be unnecessary, just look for the character in the global project
        intent.putExtra("name", name)
        startActivity(intent)
    }

    private fun updateScroller(scroller: Int): Boolean
    {
        if (scroller == ALL_S)
        {
            return updateScroller(CHAR_S) && updateScroller(ITEM_S) && updateScroller(NAR_S)
        }

        // This is a bit sketchy because NAR_S handles 3 different Recycler Views, and since all narrative elements
        // are grouped together in the database I have to do a custom search, overall implementing this
        // shouldn't be too hard, figuring out what to do though ate up lots of time TwT
        if (scroller == NAR_S)
        {
            var factions:   MutableList<String> = mutableListOf()
            var locations:  MutableList<String> = mutableListOf()
            var events:     MutableList<String> = mutableListOf()

            for (n in project.getNarrativeElements())
            {
                val name:   String  = n.getName()
                val type:   Int     = n.getType()

                when (type)
                {
                    0 -> locations.add(name)
                    1 -> factions.add(name)
                    2 -> events.add(name)
                    else ->
                    {
                        Log.e("SCR_UPD", "Invalid narrative element type read: $type")
                        throw RuntimeException("Read Invalid N. Element type: $type")
                    }
                }
            }

            val factionRecyclerAdapter: FactionRecyclerAdapter = FactionRecyclerAdapter(factions.toTypedArray()) { name ->
                onRecyclerClick(scroller, name)
            }

            val locationRecyclerAdapter: LocationRecyclerAdapter = LocationRecyclerAdapter(factions.toTypedArray()) { name ->
                onRecyclerClick(scroller, name)
            }

            val eventRecyclerAdapter: EventRecyclerAdapter = EventRecyclerAdapter(factions.toTypedArray()) { name ->
                onRecyclerClick(scroller, name)
            }

            nFacScroller.layoutManager = LinearLayoutManager(this)
            nLocScroller.layoutManager = LinearLayoutManager(this)
            nEvtScroller.layoutManager = LinearLayoutManager(this)

            nFacScroller.adapter = factionRecyclerAdapter
            nLocScroller.adapter = locationRecyclerAdapter
            nEvtScroller.adapter = eventRecyclerAdapter
        }

        var elements: MutableList<String> = mutableListOf()
        when (scroller)
        {
            CHAR_S -> {
                Log.d("SCR_UPD", "Loading ${project.getCharacters().size} characters")
                for (c in project.getCharacters())
                {
                    Log.d("SCR_UPD", "Loading character ${c.getName()}")
                    elements.add(c.getName())
                }
            }
            ITEM_S -> {
                Log.d("SCR_UPD", "Loading ${project.getCharacters().size} items")
                for (i in project.getItems())
                {
                    elements.add(i.getName())
                }
            }
        }

        // On each update, each scroller is assigned the same function with a different scroller parameter value
        when (scroller)
        {
            CHAR_S -> {
                val characterRecyclerAdapter: CharacterRecyclerAdapter = CharacterRecyclerAdapter(elements.toTypedArray()) { name ->
                    onRecyclerClick(scroller, name)
                }

                charScroller.layoutManager = LinearLayoutManager(this)
                charScroller.adapter = characterRecyclerAdapter
            }
            ITEM_S -> {
                val itemsRecyclerAdapter: ItemsRecyclerAdapter = ItemsRecyclerAdapter(elements.toTypedArray()) { name ->
                    onRecyclerClick(scroller, name)
                }

                itemScroller.layoutManager = LinearLayoutManager(this)
                itemScroller.adapter = itemsRecyclerAdapter
            }
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_project_viewer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bckgr)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = getIntent()

        // Imagine passing null as the critical parameter on which the entire activity depends on
        prj_name = intent.getStringExtra("name")!!

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
        charScroller                            = findViewById(R.id.charRecView)

        // Item collection elements
        val newItemBtn:             Button      = findViewById(R.id.newItemBtn)
        val clearItemsBtn:          Button      = findViewById(R.id.clearItemsBtn)
        itemScroller                            = findViewById(R.id.itemRecView)

        // Narrative Framework elements
        val newLocBtn:              Button      = findViewById(R.id.newLocationBtn)
        val newFacBtn:              Button      = findViewById(R.id.newFactionBtn)
        val newEvtBtn:              Button      = findViewById(R.id.newEventBtn)
        val clearLocBtn:            Button      = findViewById(R.id.clearLocationsBtn)
        val clearFacBtn:            Button      = findViewById(R.id.clearFactionsBtn)
        val clearEvtBtn:            Button      = findViewById(R.id.clearEventsBtn)
        nLocScroller                            = findViewById(R.id.locRecView)
        nFacScroller                            = findViewById(R.id.facRecView)
        nEvtScroller                            = findViewById(R.id.evtRecView)


        // Standard actions

        projectHeader.text = prj_name

        if (!loadProject())
        {
            Toast.makeText(this, "Could not load project from memory", Toast.LENGTH_SHORT).show()
            finish()
        }

        descriptionEditView.setText(if (project.getDescription().isEmpty()) "Project description goes here..." else project.getDescription())

        saveButton.setOnClickListener {
            // NOTE: The fields that aren't that easy to update automatically, like those of EditTexts for some reason, must be saved here
            project.setDescription(descriptionEditView.text.toString())

            if (database.updateObject(project))
            {
                for (c in project.getCharacters())
                {
                    if (database.getCharacter(project, c.getName()) == null)
                    {
                        Log.d("DB", "Adding character ${c.getName()}")
                        database.addObject(c)
                        continue
                    }
                    database.updateObject(c)
                }
                for (i in project.getItems())
                {
                    // TODO: Add equivalent of if getCharacter
                    database.updateObject(i)
                }
                for (n in project.getNarrativeElements())
                {
                    // TODO: Add equivalent of if getCharacter
                    database.updateObject(n)
                }
                Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show()
            }
        }

        loadButton.setOnClickListener {
            loadProject()
            updateScroller(ALL_S)
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
            val newCharIntent: Intent = Intent(this, CreateCharacterActivity::class.java)
            startActivity(newCharIntent)
        }

        clearCharBtn.setOnClickListener {
            val clearCharIntent: Intent = Intent(this, CharClearConfirmDialog::class.java)
            startActivity(clearCharIntent)
        }

        // Item collection actions

        newItemBtn.setOnClickListener {
            val newItemIntent: Intent = Intent(this, CreateItemActivity::class.java)
            startActivity(newItemIntent)
        }

        clearItemsBtn.setOnClickListener {

        }

        // Narrative Framework actions

        newLocBtn.setOnClickListener {
            val newLocationIntent: Intent = Intent(this, CreateNarrativeElementActivity::class.java)
            newLocationIntent.putExtra("type", LOCATION)
        }

        clearLocBtn.setOnClickListener {

        }

        newFacBtn.setOnClickListener {
            val newFactionIntent: Intent = Intent(this, CreateNarrativeElementActivity::class.java)
            newFactionIntent.putExtra("type", FACTION)
        }

        clearFacBtn.setOnClickListener {

        }

        newEvtBtn.setOnClickListener {
            val newEventIntent: Intent = Intent(this, CreateNarrativeElementActivity::class.java)
            newEventIntent.putExtra("type", EVENT)
        }

        clearEvtBtn.setOnClickListener {

        }
    }

    override fun onResume()
    {
        super.onResume()

        updateScroller(ALL_S)
    }
}