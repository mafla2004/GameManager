package com.example.gamemanager.Character

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gamemanager.AppCommons
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.Project
import com.example.gamemanager.R

class CharacterViewerActivity : AppCompatActivity()
{
    private val project: Project = AppCommons.getCurrentProject()!!
    private val database: GameDatabaseHelper = GameDatabaseHelper.getInstance(this)
    private var character: Character? = null

    private lateinit var charHeader:     TextView
    private lateinit var speciesEditor:  EditText
    private lateinit var birthEditor:    EditText
    private lateinit var ageEditor:      EditText

    private lateinit var aliasEditor:    EditText
    private lateinit var aspectEditor:   EditText
    private lateinit var persEditor:     EditText
    private lateinit var storyEditor:    EditText

    // Views for GameCharacter
    private lateinit var mHealthEditor:  EditText
    private lateinit var invEditor:      EditText


    private fun loadValues(): Boolean
    {
        if (character == null)
        {
            return false
        }

        charHeader.setText(character!!.getName())

        speciesEditor.setText(character!!.getSpecies())
        birthEditor.setText(character!!.getBirth())
        ageEditor.setText(character!!.getAge())

        aliasEditor.setText(character!!.getAliases())
        aspectEditor.setText(character!!.getAspect())
        persEditor.setText(character!!.getPersonality())
        storyEditor.setText(character!!.getBackstory())

        return true
    }

    private fun updateCharValues(): Boolean
    {
        if (speciesEditor.text.isBlank() ||
            birthEditor.text.isBlank() ||
            ageEditor.text.isBlank())
        {
            return false
        }

        character!!.setSpecies(speciesEditor.text.toString())
        character!!.setBirth(birthEditor.text.toString())
        character!!.setAge(ageEditor.text.toString())

        character!!.setAliases(aliasEditor.text.toString())
        character!!.setAspect(aspectEditor.text.toString())
        character!!.setPersonality(persEditor.text.toString())
        character!!.setBackstory(storyEditor.text.toString())

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val charName: String = getIntent().getStringExtra("name")!!
        character = AppCommons.getCurrentProject()!!.getCharacter(charName)

        enableEdgeToEdge()
        // BIG BRAIN MOMENT: Multiple layouts in just one activity
        when (character)
        {
            is RPGCharacter -> {
                Log.d("CV", "Creating view for RPG character")
                setContentView(R.layout.activity_game_character_viewer) // Temporary, either change to different layout or remove entry
            }
            is GameCharacter -> {
                Log.d("CV", "Creating view for game character")
                setContentView(R.layout.activity_game_character_viewer)
            }
            else -> {
                Log.d("CV", "Creating view for standard character")
                setContentView(R.layout.activity_character_viewer)
            }
        }

        Log.d("CV", "Character is game character: ${character is GameCharacter}")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bckgr)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (character == null)
        {
            Log.e("CV", "NULL CHARACTER EXTRACTED")
            finish()
        }

        val saveBtn:    Button = findViewById(R.id.saveBtn)
        val loadBtn:    Button = findViewById(R.id.loadBtn)
        val delBtn:      Button = findViewById(R.id.delBtn)

        charHeader      = findViewById(R.id.characterHeader)

        speciesEditor   = findViewById(R.id.speciesEditor)
        birthEditor     = findViewById(R.id.birthEditor)
        ageEditor       = findViewById(R.id.ageEditor)

        aliasEditor     = findViewById(R.id.aliasEditor)
        aspectEditor    = findViewById(R.id.aspectEditor)
        persEditor      = findViewById(R.id.persEditor)
        storyEditor     = findViewById(R.id.storyEditor)

        // Load all values from memory
        if (!loadValues())
        {
            Log.e("CV", "Something went wrong while loading - check that character is not null")
        }

        saveBtn.setOnClickListener {
            updateCharValues()

            if (database.getCharacter(project, character!!.getName()) == null)
            {
                if (database.addObject(character))
                {
                    Log.d("DB", "Save succeeded")
                }
                else
                {
                    Log.e("DB", "Save failed")
                }
            }
            else
            {
                if (database.updateObject(character))
                {
                    Log.d("DB", "Save succeeded")
                }
                else
                {
                    Log.e("DB", "Save failed")
                }
            }
        }

        loadBtn.setOnClickListener {
            loadValues()
        }

        delBtn.setOnClickListener {
            // TODO: Add confirm activity maybe and move logic there
            project.removeCharacter(charName)
            database.removeObject(character)
            finish()
        }
    }
}