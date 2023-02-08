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


class Homefragment: Fragment() {


    private lateinit var adaptor : homeRecycleAdaptor
    private lateinit var recycleView : RecyclerView
    private lateinit var buildingList : ArrayList<buildingname>

    lateinit var buildingName : Array<String>

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
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvReservation)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = homeRecycleAdaptor(buildingList){

            //Place to put actions for when recycle view is clicked
            //Toast.makeText(activity,it.component1(),Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, ReservationDateSelection::class.java)
            intent.putExtra("building name",it.component1())
            startActivity(intent)
        }
        recycleView.adapter = adaptor
    }

    //initializes building name data to be displayed
    @RequiresApi(Build.VERSION_CODES.N)
    private fun dataInitialize(){
        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)
        val url = URL("http://3.132.20.107:3000")


        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"

            buildingList = arrayListOf()

            buildingName = arrayOf(
                "IESB",
                "NETH",
                "COBB",
                "BOGH",
                "GTM",
                "CTH",
                "$responseCode"
            )
            //url.close()
        }


        for (i in buildingName.indices){

            val building = buildingname(buildingName[i])
            buildingList.add(building)
        }

    }


}