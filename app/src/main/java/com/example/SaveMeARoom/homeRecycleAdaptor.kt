package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reservation_items.view.*

class homeRecycleAdaptor (
    var buildings: List<buildingname>
        ) : RecyclerView.Adapter<homeRecycleAdaptor.BuildingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reservation_items, parent, false)
        return BuildingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val currentItem = buildings[position]
        holder.tvBuildingname.text = currentItem.building
    }

    override fun getItemCount(): Int {
        return buildings.size
    }

    class BuildingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tvBuildingname : TextView = itemView.findViewById(R.id.tvBuildingName)
    }

}