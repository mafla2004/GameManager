package com.example.gamemanager.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamemanager.Items.GameItem
import com.example.gamemanager.R

class ItemsRecyclerAdapter(private val items: Array<GameItem> /* TODO: Add lambda for onClick */): RecyclerView.Adapter<ItemsRecyclerHolder>()
{
    init
    {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsRecyclerHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selector_button_layout, parent, false)

        return ItemsRecyclerHolder(view /* TODO: Add lambda call here*/)
    }

    override fun onBindViewHolder(
        holder: ItemsRecyclerHolder,
        position: Int
    )
    {
        holder.text.text = items[position].getName()
    }

    override fun getItemCount(): Int = items.size
}