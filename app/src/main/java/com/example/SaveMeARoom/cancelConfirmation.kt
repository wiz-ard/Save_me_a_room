package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.book_confirmation.*
import kotlinx.android.synthetic.main.my_reservation_confirmation.*
import java.net.URL

class cancelConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cancel_confirmation)
        //pulls reservation information
        val resId = intent.getStringExtra("resid")
        val email = intent.getStringExtra("email")

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val btnYesCancel = findViewById<Button>(R.id.btnYesCancel)
        val btnNoCancel = findViewById<Button>(R.id.btnNoCancel)

        btnYesCancel.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            //gets updating value of reservation
            val query = "/search?query=SELECT%20Updating%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

            val url = URL(ip.plus(query))

            val text = url.readText()

            val updating = text.substringAfter(":").substringAfter("\"").substringBefore("\"")

            if(updating.equals("1")){
                val ip = "http://3.132.20.107:3000"
                //deletes reservation from updates database
                var query = "/search?query=DELETE%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

                var url = URL(ip.plus(query))

                var text = url.readText()
                //set updating = 0
                query = "/search?query=UPDATE%20reservations%20SET%20Updating='0'%20WHERE%20Reservation_Id=%27" + resId + "%27"

                url = URL(ip.plus(query))

                text = url.readText()

                Toast.makeText(this, "Update request cancelled.", Toast.LENGTH_SHORT).show()

                finish()
            }else{
                val ip = "http://3.132.20.107:3000"
                //deletes reservation from reservation database
                var query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

                var url = URL(ip.plus(query))

                var text = url.readText()
                //gets users number of reservations
                query = "/search?query=SELECT%20Number_of_Reservations%20FROM%20users%20WHERE%20Email=%27" + email + "%27"

                url = URL(ip.plus(query))

                text = url.readText()

                var numRes = text.substringAfter(":").substringAfter("\"").substringBefore("\"").toInt()

                numRes -= 1
                //updates number of reservations to the decremented value as the reservation was deleted
                query = "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=" + numRes + "%20WHERE%20Email=%27" + email + "%27"

                url = URL(ip.plus(query))

                text = url.readText()

                Toast.makeText(this, "Reservation cancelled.", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
        btnNoCancel.setOnClickListener {
            finish()
        }
    }
}