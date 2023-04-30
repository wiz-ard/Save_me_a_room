package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adminLogRecycleViewAdapter (var acceptedRes: List<adminLogData>, private val clickListener: (adminLogData) -> Unit) : RecyclerView.Adapter<adminLogRecycleViewAdapter.BuildingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val vh = BuildingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.admin_log_items, parent, false)){

            clickListener(acceptedRes[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val currentItem = acceptedRes[position]
        holder.tvAdminLogItem.text = currentItem.logData
    }

    override fun getItemCount(): Int {
        return acceptedRes.size
    }

    class BuildingViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvAdminLogItem : TextView = itemView.findViewById(R.id.tvAdminLogItem)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }
}