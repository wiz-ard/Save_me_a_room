package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatusRequestRecycleAdaptor (var acceptedRes: List<statusReqData>, private val clickListener: (statusReqData) -> Unit) : RecyclerView.Adapter<StatusRequestRecycleAdaptor.BuildingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val vh = BuildingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.status_request_items, parent, false)){

            clickListener(acceptedRes[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val currentItem = acceptedRes[position]
        holder.tvStatusRequestItem.text = currentItem.statReqData
    }

    override fun getItemCount(): Int {
        return acceptedRes.size
    }

    class BuildingViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvStatusRequestItem : TextView = itemView.findViewById(R.id.tvStatusRequestItem)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }
}