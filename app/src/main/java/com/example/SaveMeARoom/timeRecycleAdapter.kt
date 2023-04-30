package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class timeRecycleAdapter (var Time: List<timeData>, private val clickListener: (timeData) -> Unit) : RecyclerView.Adapter<timeRecycleAdapter.TimeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val vh = TimeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.time_items, parent, false)){

            clickListener(Time[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val currentItem = Time[position]
        holder.tvTime.text = currentItem.TimeString
    }

    override fun getItemCount(): Int {
        return Time.size
    }

    class TimeViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvTime : TextView = itemView.findViewById(R.id.tvTime)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

}