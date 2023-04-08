package com.example.SaveMeARoom


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class adminReservationLogs : AppCompatActivity() {
    private lateinit var adaptor : adminLogRecycleViewAdaptor
    private lateinit var resLogRecycleView : RecyclerView
    private lateinit var resLogList : ArrayList<adminLogData>
    private lateinit var resLogText : ArrayList<String>
    private lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_date_selection)

        //taking in the passed building name data from home fragment
        val buildingName = intent.getStringExtra("building name")
        val username = intent.getStringExtra("username")
        val college = intent.getStringExtra("college")

        val topBuildingTitle : TextView = findViewById(R.id.tvDateTitle)
        topBuildingTitle.text = buildingName+" reservation"

        getDate()

        resLogRecycleView = findViewById(R.id.rvDate)
        resLogRecycleView.layoutManager = LinearLayoutManager(this)
        resLogRecycleView.setHasFixedSize(true)
        adaptor = adminLogRecycleViewAdaptor(resLogList){

            //place to put click action
            val intent = Intent(this, TimeOccupancySelection::class.java)
            date = it.component1()
            intent.putExtra("building name", buildingName)
            intent.putExtra("date", date)
            intent.putExtra("username", username)
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
        resLogRecycleView.adapter = adaptor
    }

    private fun getDate(){
        resLogList = arrayListOf()

        resLogText = arrayListOf()

        val startTime = LocalDate.now()

        val scheduleDays = 14

        resLogText.add(startTime.toString())

        for (i: Int in 1..scheduleDays ){
            resLogText.add(startTime.plusDays(i.toLong()).toString())
        }

        for(i in resLogText.indices){
            val resLog = adminLogData(resLogText[i])
            resLogList.add(resLog)
        }

    }
}