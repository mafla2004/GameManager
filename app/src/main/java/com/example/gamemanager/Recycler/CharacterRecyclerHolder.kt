package com.example.gamemanager.Recycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.R

class CharacterRecyclerHolder(itemView: View /* TODO: Add lambda */): RecyclerView.ViewHolder(itemView)
{
    lateinit var textView: TextView

    init
    {
        textView = itemView.findViewById(R.id.btnText)


    }
}