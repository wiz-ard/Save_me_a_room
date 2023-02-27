package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.create_account.*
import java.net.URL

class Reservationfragment: Fragment() {

    private lateinit var adaptor: myReservationRecycleAdaptor
    private lateinit var recycleView: RecyclerView
    private lateinit var myReservationList: ArrayList<myReservationData>

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

        val bundle = arguments

        val email = bundle!!.getString("email")

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val query = "/search?query=SELECT%20Building_Name,%20Room_Number,%20Start_Date_Time,%20End_Date_Time%20FROM%20Capstone.reservations%20WHERE%20Reserver_Email=%27" + email + "%27"

        val url = URL(ip.plus(query))

        val text = url.readText()

        val reservations = text.split(",")

        myReservationList = arrayListOf()

        for(i in reservations.indices){
            val reservation = myReservationData(reservations[i].substringAfter(":").substringAfter('"').substringBefore('"'))
            myReservationList.add(reservation)
        }
    }
}