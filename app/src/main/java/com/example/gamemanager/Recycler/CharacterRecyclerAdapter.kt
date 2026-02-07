package com.example.gamemanager.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.R
import com.example.gamemanager.Character.Character

class CharacterRecyclerAdapter(private val characters: Array<Character> /* TODO: Add lambda for onClick */): RecyclerView.Adapter<CharacterRecyclerHolder>()
{
    init
    {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterRecyclerHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selector_button_layout, parent, false)

        return CharacterRecyclerHolder(view /* TODO: Add lambda call here*/)
    }

    override fun onBindViewHolder(
        holder: CharacterRecyclerHolder,
        position: Int
    )
    {
        holder.textView.text = characters[position].getName()
    }

    override fun getItemCount(): Int = characters.size
}