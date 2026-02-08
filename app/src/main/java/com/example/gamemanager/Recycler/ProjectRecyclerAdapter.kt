package com.example.gamemanager.Recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.Project
import com.example.gamemanager.R

class ProjectRecyclerAdapter(private val projects: Array<String>, private val onClick: (String) -> Unit): RecyclerView.Adapter<ProjectRecyclerHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectRecyclerHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selector_button_layout, parent, false)

        return ProjectRecyclerHolder(view, onClick)
    }

    override fun onBindViewHolder(
        holder: ProjectRecyclerHolder,
        position: Int
    )
    {
        holder.textView.text = projects[position]
        Log.d("PRH", "Creating character button for ${projects[position]}")
    }

    override fun getItemCount(): Int = projects.size

    init
    {

    }
}