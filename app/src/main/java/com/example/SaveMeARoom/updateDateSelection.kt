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

class UpdateDateSelection : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>

    private lateinit var adaptor : dateRecycleAdaptor
    private lateinit var dateRecycleView : RecyclerView
    private lateinit var dateList : ArrayList<dateData>
    private lateinit var dateText : ArrayList<String>
    private lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_date_selection)

        //taking in the passed building name data from home fragment
        val buildingName = intent.getStringExtra("building name")
        val occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")
        val resId = intent.getStringExtra("resId")
        val oldTime = intent.getStringExtra("oldtime")
        val oldDate = intent.getStringExtra("olddate")
        val pending = intent.getStringExtra("pending")
        val updating = intent.getStringExtra("updating")
        val email = intent.getStringExtra("email")
        val topBuildingTitle : TextView = findViewById(R.id.tvUpdateDateTitle)
        topBuildingTitle.text = buildingName +" reservation"

        getDate()

        dateRecycleView = findViewById(R.id.rvUpdateDate)
        dateRecycleView.layoutManager = LinearLayoutManager(this)
        dateRecycleView.setHasFixedSize(true)
        adaptor = dateRecycleAdaptor(dateList){

            //place to put click action
            val intent = Intent(this, UpdateTimeSelection::class.java)
            date = it.component1()
            intent.putExtra("building name", buildingName)
            intent.putExtra("date", date)
            intent.putExtra("occupancy", occupancy)
            intent.putExtra("room", room)
            intent.putExtra("resId",resId)
            intent.putExtra("oldtime",oldTime.toString().substringAfter(" "))
            intent.putExtra("olddate",oldDate)
            intent.putExtra("pending",pending)
            intent.putExtra("updating",updating)
            intent.putExtra("email",email)
            startActivity(intent)
            finish()
        }
        dateRecycleView.adapter = adaptor
    }

    private fun getDate(){
        dateList = arrayListOf()

        dateText = arrayListOf()

        val startTime = LocalDate.now()

        val scheduleDays = 14

        dateText.add(startTime.toString())

        for (i: Int in 1..scheduleDays ){
            dateText.add(startTime.plusDays(i.toLong()).toString())
        }

        for(i in dateText.indices){
            val date = dateData(dateText[i])
            dateList.add(date)
        }
    }
}