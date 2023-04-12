package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.time_occupancy_selection.*
import java.net.URL

class UpdateTimeSelection : AppCompatActivity() {

    private lateinit var adaptor : UpdateTimeAdapter
    private lateinit var timeRecycleView : RecyclerView
    private lateinit var timeList : ArrayList<timeData>
    private lateinit var finalTimeList : ArrayList<timeData>
    private lateinit var timeText : Array<String>
    private lateinit var banTimeList : ArrayList<timeData>
    private lateinit var time : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_time_selection)

        val date = intent.getStringExtra("date")
        val buildingName = intent.getStringExtra("building name")
        val occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")
        val resId = intent.getStringExtra("resId")
        val updating = intent.getStringExtra("updating")
        val pending = intent.getStringExtra("pending")
        val email = intent.getStringExtra("email")
        val college = intent.getStringExtra("college")


        val topBuildingTitle : TextView = findViewById(R.id.tvUpdateTimeTitle)
        topBuildingTitle.text = buildingName +" reservation"

        getTimeRange()

        timeRecycleView = findViewById(R.id.rvUpdateTime)
        timeRecycleView.layoutManager = LinearLayoutManager(this)
        timeRecycleView.setHasFixedSize(true)
        adaptor = UpdateTimeAdapter(finalTimeList){

            //place to put click action of the recycle view item
            val intent = Intent(this, UpdateConfirmation::class.java)
            time = it.component1()
            intent.putExtra("building name", buildingName)
            intent.putExtra("occupancy", occupancy)
            intent.putExtra("time", time)
            intent.putExtra("date", date)
            intent.putExtra("room", room)
            intent.putExtra("resId",resId)
            intent.putExtra("pending",pending)
            intent.putExtra("updating",updating)
            intent.putExtra("email",email)
            intent.putExtra("college", college)
            startActivity(intent)
            finish()

        }
        timeRecycleView.adapter = adaptor
    }


    private fun getTimeRange(){
        timeList = arrayListOf()
        banTimeList = arrayListOf()
        finalTimeList = arrayListOf()
        val date = intent.getStringExtra("date")
        val buildingName = intent.getStringExtra("building name")
        val room = intent.getStringExtra("room")
        val email = intent.getStringExtra("email")
        timeList.add(timeData("5:00pm - 7:00pm"))
        timeList.add(timeData("7:00pm - 9:00pm"))
        timeList.add(timeData("9:00pm - 11:00pm"))
        finalTimeList.add(timeData("5:00pm - 7:00pm"))
        finalTimeList.add(timeData("7:00pm - 9:00pm"))
        finalTimeList.add(timeData("9:00pm - 11:00pm"))
        //check to see which times are available before displaying them
        val ip = "http://3.132.20.107:3000"

        var query = "/search?query=SELECT%20Club_Leader%20FROM%20users%20WHERE%20Email=%27" + email + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        var club = text.substringAfter(":").substringAfter('"').substringBefore('"').toInt()

        if(club == 0){
            query = "/search?query=SELECT%20Start_Date_Time%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            var times = text.split(",")

            for (i in times.indices){
                val resdate = times[i].substringAfter(":").substringAfter("\"").substringBefore(" ")
                if(resdate.equals(date)){
                    var restime = times[i].substringAfter(" ").substringBefore("\"")
                    if(restime.equals("17:00:00")){
                        restime = "5:00pm - 7:00pm"
                    }else if(restime.equals("19:00:00")){
                        restime = "7:00pm - 9:00pm"
                    }else if(restime.equals("21:00:00")){
                        restime = "9:00pm - 11:00pm"
                    }
                    banTimeList.add(timeData(restime))
                }
            }
            for (i in banTimeList.indices){
                for (j in timeList.indices){
                    if(banTimeList.get(i).equals(timeList.get(j))){
                        finalTimeList.remove(banTimeList.get(i))
                    }
                }
            }


            if(finalTimeList.size == 0){
                Toast.makeText(this, "No other times available for that day", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else{
            query = "/search?query=SELECT%20Start_Date_Time%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20(Club_Request%20=%201%20OR%20(Pending%20=%200%20AND%20Updating%20=%200))"

            url = URL(ip.plus(query))

            text = url.readText()

            var times = text.split(",")


            for (i in times.indices){
                val resdate = times[i].substringAfter(":").substringAfter("\"").substringBefore(" ")
                if(resdate.equals(date)){
                    var restime = times[i].substringAfter(" ").substringBefore("\"")
                    if(restime.equals("17:00:00")){
                        restime = "5:00pm - 7:00pm"
                    }else if(restime.equals("19:00:00")){
                        restime = "7:00pm - 9:00pm"
                    }else if(restime.equals("21:00:00")){
                        restime = "9:00pm - 11:00pm"
                    }
                    banTimeList.add(timeData(restime))
                }
            }
            for (i in banTimeList.indices){
                for (j in timeList.indices){
                    if(banTimeList.get(i).equals(timeList.get(j))){
                        finalTimeList.remove(banTimeList.get(i))
                    }
                }
            }


            if(finalTimeList.size == 0){
                Toast.makeText(this, "No other times available for that day", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}