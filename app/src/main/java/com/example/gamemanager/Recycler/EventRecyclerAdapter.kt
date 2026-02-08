package com.example.gamemanager.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.R

class EventRecyclerAdapter(private val events: Array<String>, private val onClick: (String) -> Unit): RecyclerView.Adapter<EventRecyclerHolder>()
{
    init
    {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventRecyclerHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selector_button_layout, parent, false)

        return EventRecyclerHolder(view, onClick)
    }

    override fun onBindViewHolder(
        holder: EventRecyclerHolder,
        position: Int
    )
    {
        holder.textView.text = events[position]
    }

    override fun getItemCount(): Int = events.size
}