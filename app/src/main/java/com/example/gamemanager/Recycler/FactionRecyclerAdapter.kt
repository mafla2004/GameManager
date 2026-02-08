package com.example.gamemanager.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.R

class FactionRecyclerAdapter(private val factions: Array<String>, private val onClick: (String) -> Unit): RecyclerView.Adapter<FactionRecyclerHolder>()
{
    init
    {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FactionRecyclerHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selector_button_layout, parent, false)

        return FactionRecyclerHolder(view, onClick)
    }

    override fun onBindViewHolder(
        holder: FactionRecyclerHolder,
        position: Int
    )
    {
        holder.textView.text = factions[position]
    }

    override fun getItemCount(): Int = factions.size
}