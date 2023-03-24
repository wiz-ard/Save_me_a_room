package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reservation_items.view.*

class adminCalendarRecycleViewAdaptor (var acceptedRes: List<adminCalendarAcceptResData>, private val clickListener: (adminCalendarAcceptResData) -> Unit) : RecyclerView.Adapter<adminCalendarRecycleViewAdaptor.BuildingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val vh = BuildingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.admin_calendar_items, parent, false)){

            clickListener(acceptedRes[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val currentItem = acceptedRes[position]
        holder.tvAdminCalendarItem.text = currentItem.acceptedRes
    }

    override fun getItemCount(): Int {
        return acceptedRes.size
    }

    class BuildingViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvAdminCalendarItem : TextView = itemView.findViewById(R.id.tvAdminCalendarItem)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }
}