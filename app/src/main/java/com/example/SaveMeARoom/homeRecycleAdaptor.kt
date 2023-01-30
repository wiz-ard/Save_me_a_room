package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reservation_items.view.*

class homeRecycleAdaptor (
    var buildings: List<buildingname>
        ) : RecyclerView.Adapter<homeRecycleAdaptor.BuildingViewHolder>() {

    inner class BuildingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reservation_items, parent, false)
        return BuildingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        holder.itemView.apply{
            tvBuildingName.text = buildings[position].building
        }
    }

    override fun getItemCount(): Int {
        return buildings.size
    }
}