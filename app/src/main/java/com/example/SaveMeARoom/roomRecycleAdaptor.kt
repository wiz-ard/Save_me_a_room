package com.example.SaveMeARoom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reservation_items.view.*

class roomRecycleAdaptor (var Room: List<roomData>, private val clickListener: (roomData) -> Unit) : RecyclerView.Adapter<roomRecycleAdaptor.RoomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val vh = RoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_items, parent, false)){

            clickListener(Room[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentItem = Room[position]
        holder.tvRoom.text = currentItem.RoomString
    }

    override fun getItemCount(): Int {
        return Room.size
    }

    class RoomViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvRoom : TextView = itemView.findViewById(R.id.tvRoom)
        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

}