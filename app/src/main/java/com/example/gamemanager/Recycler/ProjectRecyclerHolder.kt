package com.example.gamemanager.Recycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.Project
import com.example.gamemanager.R

class ProjectRecyclerHolder(itemView: View, val onClick: (String) -> Unit): RecyclerView.ViewHolder(itemView)
{
    lateinit var textView: TextView

    init
    {
        textView = itemView.findViewById(R.id.btnText)

        itemView.setOnClickListener {   // QUELL'UOMO ERA PADRONE DI KOTLIN
            onClick(textView.text.toString())
        }
    }
}