package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
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
        //establish the bundle that was passed from previous page
        val bundle = arguments
        //gets all reservations and puts them into pendingResList
        dataInitialize()

        //puts values in pendingResList into the recycle view
        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvAdminReservation)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = adminHomeRecycleAdaptor(pendingResList) {

            //when element is clicked get its information and send it to AdminConfirmation
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

        val college = bundle!!.getString("college")
        //query for reservations information based on your designated college
        var query = "/search?query=SELECT%20Club_Request,Updating,Building_name,Room_number,Start_Date_Time,End_Date_Time,Reserver_Email%20FROM%20reservations%20WHERE%20(Pending='1'%20OR%20Updating='1')%20AND%20(College=%27"+college+"%27%20OR%20College=%27General%27)"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val pendingreservations = text.split(",")

        val email = bundle!!.getString("email")

        resReq.setOnClickListener {
            val intent = Intent(activity, RoomRequests::class.java)
            intent.putExtra("college", college)
            startActivity(intent)
        }
        statReq.setOnClickListener {
            val intent = Intent(activity, StatusRequests::class.java)
            intent.putExtra("college", college)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}
