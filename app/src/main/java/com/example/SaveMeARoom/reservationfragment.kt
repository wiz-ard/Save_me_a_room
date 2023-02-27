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
    private lateinit var groupBy: ArrayList<String>

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

        val email = bundle!!.getString("email")

        getReservations(email.toString())

        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvMyReservations)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = myReservationRecycleAdaptor(myReservationList){

            val intent = Intent(activity, myReservationConfirmation::class.java)
            intent.putExtra("reservation info", it.component1())
            intent.putExtra("email", email)
            startActivity(intent)

        }
        recycleView.adapter = adaptor

    }

    private fun getReservations(email:String) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val query = "/search?query=SELECT%20Building_Name,%20Room_Number,%20Start_Date_Time,%20End_Date_Time%20FROM%20Capstone.reservations%20WHERE%20Reserver_Email=%27" + email + "%27"

        val url = URL(ip.plus(query))

        val text = url.readText()

        val reservations = text.split(",")

        myReservationList = arrayListOf()

        groupBy = arrayListOf()

        var track = 1

        for(i in reservations.indices){
            val reservation = myReservationData(reservations[i].substringAfter(":").substringAfter('"').substringBefore('"'))
            val reservationtrim = reservation.toString().substringAfter("=").substringBefore(")")
            groupBy.add(reservationtrim)
            if(track % 4 == 0){
                val finalreservation = myReservationData(groupBy[i-3].toString() + ", " + groupBy[i-2].toString() + ", " + ((groupBy[i-1].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm," + groupBy[i].toString().substringAfter("").substringBefore(" "))
                myReservationList.add(finalreservation)
            }
            track += 1
        }
    }
}