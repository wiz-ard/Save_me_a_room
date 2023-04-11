package com.example.SaveMeARoom

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.room_request.*
import java.net.URL

class RoomRequests : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var pendingResList: ArrayList<adminPendingData>
    private lateinit var groupBy: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_request)

        // get the lists for the spinner values (building, date, time, status)
        val college = intent.getStringExtra("college")

        val ip = "http://3.132.20.107:3000"

        // building list
        var query = "/search?query=SELECT%20DISTINCT%20Building_name%20FROM%20reservations%20WHERE%20College=%27"+college+"%27%"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val buildings = text.split(',')

        var buildingList = arrayListOf<String>()

        buildingList.add("any")

        for(i in buildings.indices){
            buildingList.add(buildings[i].substringAfter(':').substringAfter('"').substringBefore('"'))
        }

        val spBuild = findViewById<Spinner>(R.id.spBuildingSelect)
        spBuild.onItemSelectedListener = this

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, buildingList as List<Any?>)

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spBuild.adapter = ad

        // date list and time list
        query = "/search?query=SELECT%20DISTINCT%20Start_Date_Time%20FROM%20reservations%20WHERE%20College=%27"+college+"%27%"

        url = URL(ip.plus(query))

        text = url.readText()

        val dateTimes = text.split(',')

        var dateList = arrayListOf<String>()

        dateList.add("any")

        var timeList  = arrayListOf<String>()

        timeList.add("any")

        for(i in dateTimes.indices){
            dateList.add(dateTimes[i].substringAfter(':').substringAfter('"').substringBefore(' '))
            timeList.add(dateTimes[i].substringAfter(':').substringAfter('"').substringAfter(' ').substringBefore('"'))
        }

        val spDate = findViewById<Spinner>(R.id.spDateSelect)
        spDate.onItemSelectedListener = this

        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, dateList as List<Any?>)

        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spDate.adapter = ad2

        val spTime = findViewById<Spinner>(R.id.spTimeSelect)
        spTime.onItemSelectedListener = this

        val ad3: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, timeList as List<Any?>)

        ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spTime.adapter = ad3

        // status list
        val statusList = arrayListOf<String>()
        statusList.add("any")
        statusList.add("General")
        statusList.add("Club Leader")

        val spStat = findViewById<Spinner>(R.id.spStatusSelect)
        spStat.onItemSelectedListener = this

        val ad4: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, statusList as List<Any?>)

        ad4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spStat.adapter = ad4

                // figure out which ones have been changed, any vs something
        // make an array to see if any spinners have been selected
        val spinnerChanges = arrayListOf<String>()
        spinnerChanges.add("") // for building list
        spinnerChanges.add("") // for date list
        spinnerChanges.add("") // for time list
        spinnerChanges.add("") // for status list

        // set the button
        btnFilter.setOnClickListener {

        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun dataInitialize() {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val college = intent.getStringExtra("college")
        //query for reservations information based on your designated college
        val ip = "http://3.132.20.107:3000"

        var query = "/search?query=SELECT%20Club_Request,Updating,Building_name,Room_number,Start_Date_Time,End_Date_Time,Reserver_Email%20FROM%20reservations%20WHERE%20(Pending='1'%20OR%20Updating='1')%20AND%20(College=%27"+college+"%27%20OR%20College=%27General%27)"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val pendingreservations = text.split(",")

        pendingResList = arrayListOf()

        groupBy = arrayListOf()

        var track = 1
        //loop to add reservations to the recycle view
        for(i in pendingreservations.indices){
            val reservation = myReservationData(pendingreservations[i].substringAfter(":").substringAfter('"').substringBefore('"'))
            val reservationtrim = reservation.toString().substringAfter("=").substringBefore(")")
            groupBy.add(reservationtrim)
            if(track % 7 == 0){
                var finalreservation:adminPendingData
                val club = groupBy[i-6]
                val update = groupBy[i-5]
                if(update.toInt() == 1){
                    if(club.toInt() == 1){
                        finalreservation = adminPendingData(groupBy[i-4].toString() + ", " + groupBy[i-3].toString() + ", " + ((groupBy[i-2].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i-1].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm, " + groupBy[i-1].toString().substringBefore(" ") + ", " + groupBy[i].substringBefore("@") + ", Update Request, Club Request")

                    }
                    else{
                        finalreservation = adminPendingData(groupBy[i-4].toString() + ", " + groupBy[i-3].toString() + ", " + ((groupBy[i-2].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i-1].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm, " + groupBy[i-1].toString().substringBefore(" ") + ", " + groupBy[i].substringBefore("@") + ", Update Request")
                    }
                }
                else{
                    if(club.toInt() == 1){
                        finalreservation = adminPendingData(groupBy[i-4].toString() + ", " + groupBy[i-3].toString() + ", " + ((groupBy[i-2].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i-1].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm, " + groupBy[i-1].toString().substringBefore(" ") + ", " + groupBy[i].substringBefore("@")  + ", Club Request")
                    }
                    else {
                        finalreservation = adminPendingData(groupBy[i-4].toString() + ", " + groupBy[i-3].toString() + ", " + ((groupBy[i-2].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm-" + ((groupBy[i-1].toString().substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm, " + groupBy[i-1].toString().substringBefore(" ") + ", " + groupBy[i].substringBefore("@"))
                    }

                }
                pendingResList.add(finalreservation)
            }
            track += 1
        }
    }
}
