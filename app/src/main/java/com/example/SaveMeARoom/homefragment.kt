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
import java.net.HttpURLConnection
import java.net.URL
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_account.*


class Homefragment: Fragment() {
    private lateinit var infoList: ArrayList<String>

    private lateinit var adaptor: homeRecycleAdaptor
    private lateinit var recycleView: RecyclerView
    private lateinit var buildingList: ArrayList<buildingname>


    // needed to display and fill in the data when its created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.homefragment, container, false)
    }

    // fills in the data
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        dataInitialize()

        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvReservation)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = homeRecycleAdaptor(buildingList) {
            //Place to put actions for when recycle view is clicked
            val intent = Intent(activity, ReservationDateSelection::class.java)

            intent.putExtra("building name", it.component1())
            val ip = "http://3.132.20.107:3000"
            //queries for college based on building name
            var query = "/search?query=SELECT%20DISTINCT%20Associated_College%20FROM%20locations%20WHERE%20Building_Name=%27" + it.component1()  + "%27"
            var url = URL(ip.plus(query))
            var text = url.readText()
            val college = text.substringAfter(":").substringAfter("\"").substringBefore("\"")
            //passses username and college to next page
            intent.putExtra("username", bundle!!.getString("username"))
            intent.putExtra("college", college)
            startActivity(intent)
        }
        recycleView.adapter = adaptor
    }

    //initializes building name data to be displayed
    @RequiresApi(Build.VERSION_CODES.N)
    private fun dataInitialize() {
        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val bundle = arguments

        val username = bundle!!.getString("username")

        val ip = "http://3.132.20.107:3000"
        //gets user information based on username
        var query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username  + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        infoList = arrayListOf()

        val userInfo = text.split(",")
        //adds userinfo elements to the infoList
        for (i in userInfo.indices) {
            val info = userData(userInfo[i].substringAfter(":").substringAfter('"').substringBefore('"')).toString()
            val info2 = info.substringAfter("=").substringBefore(")")
            infoList.add(info2)
        }


        val club = infoList[5].toInt()
        // check if the user is a club leader
        if(club == 0){
            val college = infoList[3]
            //queries for building names based on your college
            query = "/search?query=SELECT%20DISTINCT%20Building_Name%20FROM%20locations%20WHERE%20Associated_College=%27" + college + "%27%20OR%20Associated_College=%27General%27%20ORDER%20BY%20Building_Name"

            url = URL(ip.plus(query))

            text = url.readText()

            val buildingName = text.split(",")

            buildingList = arrayListOf()
            //adds building names to the list to go to the recycler view
            for (i in buildingName.indices){

                val building = buildingname(buildingName[i].substringAfter(":").substringAfter('"').substringBefore('"'))
                buildingList.add(building)
            }
        } else{
            val college = infoList[3]
            //queries for building names based on your college
            query = "/search?query=SELECT%20DISTINCT%20Building_Name%20FROM%20locations%20ORDER%20BY%20Building_Name"

            url = URL(ip.plus(query))

            text = url.readText()

            val buildingName = text.split(",")

            buildingList = arrayListOf()
            //adds building names to the list to go to the recycler view
            for (i in buildingName.indices){

                val building = buildingname(buildingName[i].substringAfter(":").substringAfter('"').substringBefore('"'))
                buildingList.add(building)
            }
        }
    }
}