package com.example.SaveMeARoom

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.room_request.*
import java.net.URL

class RoomRequests : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var pendingResList: ArrayList<adminPendingData>
    private lateinit var groupBy: ArrayList<String>
    private lateinit var adaptor: ArrayList<String>
    private lateinit var spinnerChanges: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_request)

        // get the lists for the spinner values (building, date, time, status)
        val college = intent.getStringExtra("college")

        val ip = "http://3.132.20.107:3000"

        // building list
        var query = "/search?query=SELECT%20DISTINCT%20Building_Name%20FROM%20reservations%20WHERE%20College=%27"+college+"%27"

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
        query = "/search?query=SELECT%20DISTINCT%20Start_Date_Time%20FROM%20reservations%20WHERE%20College=%27"+college+"%27"

        url = URL(ip.plus(query))

        text = url.readText()

        val dateTimes = text.split(',')

        var dateTimeList = arrayListOf<String>()

        dateTimeList.add("any")

        for(i in dateTimes.indices){
            dateTimeList.add(dateTimes[i].substringAfter(':').substringAfter('"').substringBefore('"'))
        }

        val spDateTime = findViewById<Spinner>(R.id.spDateSelect)
        spDateTime.onItemSelectedListener = this

        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, dateTimeList as List<Any?>)

        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spDateTime.adapter = ad2

        // status list (accepted, updating, pending)
        val statList = arrayListOf<String>()
        statList.add("any")
        statList.add("Pending")
        statList.add("Updating")
        statList.add("Accepted")

        val spStatus = findViewById<Spinner>(R.id.spTimeSelect)
        spStatus.onItemSelectedListener = this

        val ad3: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, statList as List<Any?>)

        ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spStatus.adapter = ad3

        // club list
        val clubList = arrayListOf<String>()
        clubList.add("any")
        clubList.add("General")
        clubList.add("Club Leader")

        val spClub = findViewById<Spinner>(R.id.spStatusSelect)
        spClub.onItemSelectedListener = this

        val ad4: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, clubList as List<Any?>)

        ad4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spClub.adapter = ad4

        // set spinner changes to be prepared for things changing
        spinnerChanges = arrayListOf()
        spinnerChanges.add("any") // for building list
        spinnerChanges.add("any") // for date time list
        spinnerChanges.add("any") // for status list
        spinnerChanges.add("any") // for club list

        // set the button
        btnFilter.setOnClickListener {
            val ip = "http://3.132.20.107:3000"

            var query = "/search?query=SELECT%20*%20FROM%20reservations"

            var url:URL

            var text:String

            // if buildings spinner was modified
            if(!(spinnerChanges[0].equals("any"))){
                query += "%20WHERE%20Building_Name=%27" + spinnerChanges[0] + "%27"

                // if date time spinner was also modified
                if(!(spinnerChanges[1].equals("any"))){
                    query += "%20AND%20Start_Date_Time=%27" + spinnerChanges[1] + "%27"
                }

                // if status spinner was also modified
                if(!(spinnerChanges[2].equals("any"))){
                    if(spinnerChanges[2].equals("Pending")){
                        query += "%20AND%20Pending=1"
                    }
                    else if(spinnerChanges[2].equals("Updating")){
                        query += "%20AND%20Updating=1"
                    }
                    else{
                        query += "%20AND%20Pending=0%20AND%20Updating=0"
                    }
                }

                // if club spinner was also modified
                if(!(spinnerChanges[3].equals("any"))){
                    if(spinnerChanges[3].equals("General")){
                        query += "%20AND%20Club_Request=%27" + 0 + "%27"
                    }
                    else{
                        query += "%20AND%20Club_Request=%27" + 1 + "%27"
                    }
                }
            }

            // if date spinner was modified
            else if(!(spinnerChanges[1].equals("any"))){
                query += "%20WHERE%20Start_Date_Time=%27" + spinnerChanges[1] + "%27"

                // if status spinner was also modified
                if(!(spinnerChanges[2].equals("any"))){
                    if(spinnerChanges[2].equals("Pending")){
                        query += "%20AND%20Pending=1"
                    }
                    else if(spinnerChanges[2].equals("Updating")){
                        query += "%20AND%20Updating=1"
                    }
                    else{
                        query += "%20AND%20Pending=0%20AND%20Updating=0"
                    }
                }

                // if club spinner was also modified
                if(!(spinnerChanges[3].equals("any"))){
                    if(spinnerChanges[3].equals("General")){
                        query += "%20AND%20Club_Request=%27" + 0 + "%27"
                    }
                    else{
                        query += "%20AND%20Club_Request=%27" + 1 + "%27"
                    }
                }
            }

            // if time spinner was modified
            else if(!(spinnerChanges[2].equals("any"))) {
                if (spinnerChanges[2].equals("Pending")) {
                    query += "%20AND%20Pending=1"
                } else if (spinnerChanges[2].equals("Updating")) {
                    query += "%20AND%20Updating=1"
                } else {
                    query += "%20AND%20Pending=0%20AND%20Updating=0"
                }

                // if club spinner was also modified
                if (!(spinnerChanges[3].equals("any"))) {
                    if (spinnerChanges[3].equals("General")) {
                        query += "%20AND%20Club_Request=%27" + 0 + "%27"
                    } else {
                        query += "%20AND%20Club_Request=%27" + 1 + "%27"
                    }
                }
            }

            // if club spinner was modified
            else if(!(spinnerChanges[3].equals("any"))){
                if(spinnerChanges[3].equals("General")){
                    query += "%20WHERE%20Club_Request=%27" + 0 + "%27"
                }
                else{
                    query += "%20WHERE%20Club_Request=%27" + 1 + "%27"
                }
            }

            // after deciding what the query is based on the list, grab the correct requests
            url = URL(ip.plus(query))

            text = url.readText()

            // convert the big string into individual requests
            val requests = text.split(',')

            var requestList = arrayListOf<String>()

            for(i in requests.indices){
                requestList.add(requests[i].substringAfter(':').substringAfter('"').substringBefore('"'))
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        if(parent!!.id == R.id.spBuildingSelect){
            val item = parent!!.getItemAtPosition(position)
            spinnerChanges[0] = item.toString()
        }
        else if(parent!!.id == R.id.spDateSelect){
            val item = parent!!.getItemAtPosition(position)
            spinnerChanges[1] = item.toString()
        }
        else if(parent!!.id == R.id.spTimeSelect){
            val item = parent!!.getItemAtPosition(position)
            spinnerChanges[2] = item.toString()
        }
        else if(parent!!.id == R.id.spStatusSelect){
            val item = parent!!.getItemAtPosition(position)
            spinnerChanges[3] = item.toString()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}
