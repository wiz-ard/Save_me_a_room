package com.example.SaveMeARoom

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reservation_items.view.*

class myReservationRecycleAdaptor (var Reservations: List<myReservationData>, private val clickListener: (myReservationData) -> Unit) : RecyclerView.Adapter<myReservationRecycleAdaptor.MyReservationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReservationViewHolder {
        val vh = MyReservationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_reservation_items, parent, false)){

            clickListener(Reservations[it])
        }
        return vh
    }

    override fun onBindViewHolder(holder: MyReservationViewHolder, position: Int) {
        val currentItem = Reservations[position]
        holder.tvMyReservations.text = currentItem.myReservations
    }

    override fun getItemCount(): Int {
        return Reservations.size
    }

    class MyReservationViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val tvMyReservations : TextView = itemView.findViewById(R.id.tvMyReservations)

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

}