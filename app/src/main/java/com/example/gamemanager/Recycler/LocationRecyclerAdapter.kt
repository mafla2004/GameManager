package com.example.gamemanager.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.R

class LocationRecyclerAdapter(private val locations: Array<String>, private val onClick: (String) -> Unit): RecyclerView.Adapter<LocationRecyclerHolder>()
{
    init
    {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationRecyclerHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selector_button_layout, parent, false)

        return LocationRecyclerHolder(view, onClick)
    }

    override fun onBindViewHolder(
        holder: LocationRecyclerHolder,
        position: Int
    )
    {
        holder.textView.text = locations[position]
    }

    override fun getItemCount(): Int = locations.size
}