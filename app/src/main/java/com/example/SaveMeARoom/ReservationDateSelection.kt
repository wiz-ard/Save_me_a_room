package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class ReservationDateSelection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_date_selection)

        val buildingHandle = intent.getStringExtra("building name")
        Log.d("LOG", buildingHandle!!)

    }
}