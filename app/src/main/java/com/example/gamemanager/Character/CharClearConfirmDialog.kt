package com.example.gamemanager.Character

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gamemanager.AppCommons
import com.example.gamemanager.GameDatabaseHelper
import com.example.gamemanager.R

class CharClearConfirmDialog : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_char_clear_confirm_dialog)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Declare UI elements
        val confirmButton:  Button = findViewById(R.id.confirmButton)
        val denyButton:     Button = findViewById(R.id.denyButton)

        // Declare database
        confirmButton.setOnClickListener {
            val database: GameDatabaseHelper = GameDatabaseHelper.getInstance(this)

            AppCommons.getCurrentProject()!!.clearCharacters()  // We don't clear them from the DB because we await a save

            finish()
        }

        denyButton.setOnClickListener {
            Toast.makeText(this, "Your projects and the characters inside them thank you", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}