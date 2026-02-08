package com.example.gamemanager

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gamemanager.Character.*

class CreateCharacterActivity : AppCompatActivity()
{
    private lateinit var selTypeBtn: Button
    private lateinit var selText: TextView

    private val project: Project = AppCommons.getCurrentProject()
    private var selection: String = R.string.STD_CHAR.toString()

    // So many things to do...
    // So little energy to do them...

    private fun openPopUpMenu(v: View)
    {
        val menu: PopupMenu = PopupMenu(this, v)

        menu.menuInflater.inflate(R.menu.menu_character_type_selection, menu.menu)
        menu.setOnMenuItemClickListener { menuItem ->
            selection = menuItem.title.toString()

            Toast.makeText(this, "Selected type: $selection", Toast.LENGTH_SHORT).show()
            selText.setText(selection)

            true
        }

        menu.show()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_create_character)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bckgr)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        selTypeBtn = findViewById(R.id.selectTypeBtn)
        selText = findViewById(R.id.selectionText) // STFU Android Studio, the XML DOES contain that ID, stop bitching

        val charNameEditor:     EditText    = findViewById(R.id.charNameEditor)
        val charSpeciesEditor:  EditText    = findViewById(R.id.charSpeciesEditor)
        val charBirthEditor:    EditText    = findViewById(R.id.charBirthEditor)
        val charAgeEditor:      EditText    = findViewById(R.id.charAgeEditor)
        val goBackBtn:          Button      = findViewById(R.id.goBackButton)
        val createCharBtn:      Button      = findViewById(R.id.createChar)

        selTypeBtn.setOnClickListener { v: View ->
            openPopUpMenu(v)
        }

        goBackBtn.setOnClickListener {
            finish()
        }

        createCharBtn.setOnClickListener {
            val charName: String  = charNameEditor.text.toString()
            val charSpecies: String = charSpeciesEditor.text.toString()
            val charBirth: String = charBirthEditor.text.toString()
            val charAge: String = charAgeEditor.text.toString()

            if (charName.isEmpty() ||
                charSpecies.isEmpty() ||
                charBirth.isEmpty() ||
                charAge.isEmpty())
            {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var char: Character

                when (selection)
                {
                    R.string.GAME_CHAR.toString() -> char = GameCharacter(
                        AppCommons.getCurrentProject(),
                        charName,
                        charSpecies,
                        charBirth,
                        charAge)
                    R.string.RPG_CHAR.toString() -> char = RPGCharacter(
                        AppCommons.getCurrentProject(),
                        charName,
                        charSpecies,
                        charBirth,
                        charAge)
                    else -> char = Character(
                        AppCommons.getCurrentProject(),
                        charName,
                        charSpecies,
                        charBirth,
                        charAge)
                }

                if (AppCommons.getCurrentProject().addCharacter(char))
                {
                    Toast.makeText(this, "Character successfully added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else
                {
                    Toast.makeText(this, "Character with same name already present, please pick another name", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}