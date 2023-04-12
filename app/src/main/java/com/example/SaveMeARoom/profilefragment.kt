package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.profilefragment.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class Profilefragment: Fragment(R.layout.profilefragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayUsername = view.findViewById<TextView>(R.id.tvUsername)
        val displayEmail = view.findViewById<TextView>(R.id.tvEmail)
        val displayCollege = view.findViewById<TextView>(R.id.tvCollege)
        val displayReservation = view.findViewById<TextView>(R.id.tvReservationsLeft)

        val resStatic = view.findViewById<TextView>(R.id.tvReservationsLeftStatic)

        val bundle = arguments

        val username = bundle!!.getString("username")

        val ip = "http://3.132.20.107:3000"

        var query = "/search?query=SELECT%20Number_of_Reservations%20FROM%20users%20WHERE%20Email=%27" + bundle!!.getString("email") + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val numRes = text.substringAfter(":").substringAfter("\"").substringBefore("\"")

        val admin = bundle!!.getString("admin")

        if(admin.equals("1")){
            displayReservation.visibility = View.INVISIBLE
            resStatic.visibility = View.INVISIBLE
        }

        displayUsername?.text = bundle!!.getString("username")
        displayEmail?.text = bundle!!.getString("email")
        displayCollege?.text = bundle!!.getString("college")
        query = "/search?query=SELECT%20Club_Leader%20FROM%20users%20WHERE%20Email=%27" + bundle!!.getString("email") + "%27"
        url = URL(ip.plus(query))
        text = url.readText()
        var club = text.substringAfter(":").substringAfter('"').substringBefore('"').toInt()
        if(club == 0){
            displayReservation?.text = numRes + "/3"
        } else {
            displayReservation?.text = numRes + "/6"
        }




        btnLogout.setOnClickListener {
            val curTime = LocalTime.now()
            val curDate = LocalDate.now()
            val logTime = curTime.toString() + " " + curDate.toString()
            //log logout
            val ip = "http://3.132.20.107:3000"

            val query = "/search?query=INSERT%20INTO%20userlogs%20VALUES(%27" + username + "%27,%27NULL%27,%27" + logTime + "%27,%27NULL%27,%27" + bundle!!.getString("college") + "%27)"

            val url = URL(ip.plus(query))

            val text = url.readText()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        btnEditProfile.setOnClickListener {
            val intent = Intent(activity, editProfile::class.java)
            intent.putExtra("olduser", bundle!!.getString("username"))
            startActivity(intent)
        }
        btnClubRequest.setOnClickListener {
            val intent = Intent(activity, clubConfirm::class.java)
            intent.putExtra("username", bundle!!.getString("username"))
            startActivity(intent)
        }
    }
}