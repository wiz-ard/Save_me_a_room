package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Reservationfragment: Fragment() {

    private lateinit var adaptor: myReservationRecycleAdaptor
    private lateinit var recycleView: RecyclerView
    private lateinit var myReservationList: ArrayList<myReservationData>
    private lateinit var myReservationText: Array<String>

    // needed to display and fill in the data when its created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reservationfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        getReservations()

        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvMyReservations)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = myReservationRecycleAdaptor(myReservationList){

            val intent = Intent(activity, myReservationConfirmation::class.java)

            startActivity(intent)

        }

    }

    private fun getReservations() {

        myReservationText = arrayOf(
            "Reservation 1",
            "reservation 2",
            "reservation 3"
        )

        myReservationList = arrayListOf()

        for(i in myReservationText.indices){
            myReservationList.add(myReservationData(myReservationText[i]))
        }


    }

}