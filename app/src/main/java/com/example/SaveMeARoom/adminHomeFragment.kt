package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_account.*


class AdminHomefragment: Fragment() {


    private lateinit var adaptor: adminHomeRecycleAdaptor
    private lateinit var recycleView: RecyclerView
    private lateinit var pendingResList: ArrayList<adminPendingData>
    private lateinit var groupBy: ArrayList<String>


    // needed to display and fill in the data when its created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_home_fragment, container, false)
    }

    // fills in the data
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        dataInitialize()


        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvAdminReservation)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = adminHomeRecycleAdaptor(pendingResList) {

            //Place to put actions for when recycle view is clicked
            //Toast.makeText(activity,it.component1(),Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, AdminConfirmation::class.java)
            intent.putExtra("res info", it.component1())
            intent.putExtra("email", bundle!!.getString("email"))
            startActivity(intent)
        }
        recycleView.adapter = adaptor
    }

    //initializes building name data to be displayed
    @RequiresApi(Build.VERSION_CODES.N)
    private fun dataInitialize() {

        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val bundle = arguments
        val username = bundle!!.getString("username")

        val query = "/search?query=SELECT%20Building_name,Room_number,Start_Date_Time,End_Date_Time%20FROM%20reservations%20WHERE%20Pending='1'"

        val url = URL(ip.plus(query))

        val text = url.readText()

        val pendingreservations = text.split(",")

        pendingResList = arrayListOf()

        groupBy = arrayListOf()

        var track = 1

        for(i in pendingreservations.indices){
            val reservation = myReservationData(pendingreservations[i].substringAfter(":").substringAfter('"').substringBefore('"'))
            val reservationtrim = reservation.toString().substringAfter("=").substringBefore(")")
            groupBy.add(reservationtrim)
            if(track % 4 == 0){
                val finalreservation = adminPendingData(groupBy[i-3].toString() + ", " + groupBy[i-2].toString() + ", " + ((groupBy[i-1].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm, " + groupBy[i].toString().substringBefore(" ") + ", " + username)
                pendingResList.add(finalreservation)
            }
            track += 1
        }
    }
}
