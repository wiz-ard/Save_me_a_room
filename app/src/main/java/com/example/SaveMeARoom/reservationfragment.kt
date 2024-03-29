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
import java.net.URL

class Reservationfragment: Fragment() {
    private lateinit var infoList: ArrayList<String>

    private lateinit var adaptor: myReservationRecycleAdapter
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

        reservationLoad()
    }

    override fun onResume() {
        super.onResume()
        reservationLoad()
    }

    fun reservationLoad(){


        val bundle = arguments

        val ip = "http://3.132.20.107:3000"

        val email = bundle!!.getString("email").toString()

        //gets user college
        var query = "/search?query=SELECT%20College%20FROM%20users%20WHERE%20Email=%27" + email + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val college = text.substringAfter(":").substringAfter("\"").substringBefore("\"")
        //gets user reservations
        getReservations(email)

        val layoutManager = LinearLayoutManager(context)
        recycleView = requireView().findViewById(R.id.rvMyReservations)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = myReservationRecycleAdapter(myReservationList){
            //sends appropriate reservation information and email to next page
            val intent = Intent(activity, myReservationConfirmation::class.java)
            intent.putExtra("reservation info", it.component1())
            intent.putExtra("email", email)
            intent.putExtra("college", college)
            startActivity(intent)

        }
        recycleView.adapter = adaptor
    }

    private fun getReservations(email:String) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"
        //queries for reservations based on user email
        var query = "/search?query=SELECT%20Updating,%20Pending,%20Building_Name,%20Room_Number,%20Start_Date_Time,%20End_Date_Time%20FROM%20Capstone.reservations%20WHERE%20Reserver_Email=%27" + email + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val reservations = text.split(",")

        myReservationList = arrayListOf()

        groupBy = arrayListOf()

        var track = 1
        //loop to add reservations to recycler view
        for(i in reservations.indices){
            val reservation = myReservationData(reservations[i].substringAfter(":").substringAfter('"').substringBefore('"'))
            val reservationtrim = reservation.toString().substringAfter("=").substringBefore(")")
            groupBy.add(reservationtrim)
            if(track % 6 == 0){
                var finalreservation:myReservationData
                val update = groupBy[i-5]
                val pending = groupBy[i-4]
                if(update.toInt() == 1){
                    finalreservation = myReservationData(groupBy[i-3] + ", " + groupBy[i-2] + ", " + ((groupBy[i-1].substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i].substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm," + groupBy[i].substringAfter("").substringBefore(" ") + " Updating")
                }
                else{
                    if(pending.toInt() == 1){
                        finalreservation = myReservationData(groupBy[i-3] + ", " + groupBy[i-2] + ", " + ((groupBy[i-1].substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i].substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm," + groupBy[i].substringAfter("").substringBefore(" ") + ", Pending")
                    }
                    else{
                        finalreservation = myReservationData(groupBy[i-3] + ", " + groupBy[i-2] + ", " + ((groupBy[i-1].substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i].substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm," + groupBy[i].substringAfter("").substringBefore(" ") + ", Accepted")
                    }
                    }

                myReservationList.add(finalreservation)
            }
            track += 1
        }
    }
}