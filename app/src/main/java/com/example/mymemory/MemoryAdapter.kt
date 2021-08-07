package com.example.mymemory

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MemoryAdapter(private val context: MainActivity, private val numPieces: Int) :
    RecyclerView.Adapter<MemoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoryAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = numPieces

}
