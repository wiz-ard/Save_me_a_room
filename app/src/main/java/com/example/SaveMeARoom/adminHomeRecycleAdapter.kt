package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adminHomeRecycleAdapter (var pendingReservations: List<adminPendingData>, private val clickListener: (adminPendingData) -> Unit) : RecyclerView.Adapter<adminHomeRecycleAdapter.PendingResViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingResViewHolder {
        val vh = PendingResViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.admin_pending_reservation_items, parent, false)){

            clickListener(pendingReservations[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: PendingResViewHolder, position: Int) {
        val currentItem = pendingReservations[position]
        holder.tvAdminPendingRes.text = currentItem.PendingReservation
    }

    override fun getItemCount(): Int {
        return pendingReservations.size
    }

    class PendingResViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvAdminPendingRes : TextView = itemView.findViewById(R.id.tvAdminPendingReservation)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

}