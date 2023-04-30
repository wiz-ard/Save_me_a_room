package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomRequestRecycleAdapter (var acceptedRes: List<roomResData>, private val clickListener: (roomResData) -> Unit) : RecyclerView.Adapter<RoomRequestRecycleAdapter.BuildingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val vh = BuildingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_request_items, parent, false)){

            clickListener(acceptedRes[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val currentItem = acceptedRes[position]
        holder.tvRoomRequestItem.text = currentItem.reqData
    }

    override fun getItemCount(): Int {
        return acceptedRes.size
    }

    class BuildingViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvRoomRequestItem : TextView = itemView.findViewById(R.id.tvRoomRequestItem)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }
}