package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reservation_items.view.*

class dateRecycleAdaptor (var Date: List<dateData>, private val clickListener: (dateData) -> Unit) : RecyclerView.Adapter<dateRecycleAdaptor.DateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val vh = DateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.date_items, parent, false)){

            clickListener(Date[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val currentItem = Date[position]
        holder.tvDate.text = currentItem.DateString
    }

    override fun getItemCount(): Int {
        return Date.size
    }

    class DateViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvDate : TextView = itemView.findViewById(R.id.tvDate)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

}