package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class adminStatusConfirm : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_status_confirm)

        val ip = "http://3.132.20.107:3000"
        val requestInfo = intent.getStringExtra("request info").toString()
        val adminEmail = intent.getStringExtra("adminemail").toString()
        val college = intent.getStringExtra("college").toString()
        val infoSplit = requestInfo.split(",")
        val email = infoSplit[0]
        val newCollege = infoSplit[1].substringAfter(" ")
        val flag = intent.getStringExtra("flag")
        val type = intent.getStringExtra("type")

        var tvStatusConfirmText = findViewById<TextView>(R.id.tvStatusConfirmText)
        val btnYesStatusConfirm = findViewById<Button>(R.id.btnYesStatusConfirm)
        val btnNoStatusConfirm = findViewById<Button>(R.id.btnNoStatusConfirm)

        if(flag == "1"){
            if(type == "club"){
                tvStatusConfirmText.text = "Are you sure you want to accept this club request?"
            }else{
                tvStatusConfirmText.text = "Are you sure you want to accept this college request?"
            }
        }else{
            if(type == "club"){
                tvStatusConfirmText.text = "Are you sure you want to deny this club request?"
            }else{
                tvStatusConfirmText.text = "Are you sure you want to deny this college request?"
            }
        }

        btnYesStatusConfirm.setOnClickListener {
            if(flag == "1"){
                if(type == "club"){
                    val curTime = LocalTime.now()
                    val curDate = LocalDate.now()
                    val logTime = curTime.toString().substringBefore(".") + " " + curDate.toString()

                    var query = "/search?query=UPDATE%20users%20SET%20Club_Leader=%271%27%20WHERE%20Email=%27" + email + "%27"
                    var url = URL(ip.plus(query))
                    url.readText()

                    query =
                        "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20Club_Leader_Request=%271%27"
                    url = URL(ip.plus(query))
                    url.readText()
                    //update status logs
                    query =
                        "/search?query=INSERT%20INTO%20statuslogs%20VALUES(%27" + email + "%27,%27" + college + "%27,%27" + adminEmail + "%27,%27" + logTime + "%27,%270%27,%270%27,%271%27,%270%27)"
                    url = URL(ip.plus(query))
                    url.readText()

                    Toast.makeText(this, "Club request accepted.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, StatusRequests::class.java)
                    intent.putExtra("email", adminEmail).toString()
                    intent.putExtra("college", college)
                    startActivity(intent)
                    finish()
                }else{
                    val curTime = LocalTime.now()
                    val curDate = LocalDate.now()
                    val logTime = curTime.toString().substringBefore(".") + " " + curDate.toString()

                    var query = "/search?query=UPDATE%20users%20SET%20College=%27" + newCollege + "%27%20WHERE%20Email=%27" + email + "%27"
                    var url = URL(ip.plus(query))
                    url.readText()

                    query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"
                    url = URL(ip.plus(query))
                    url.readText()

                    Toast.makeText(this, "College change request accepted.", Toast.LENGTH_SHORT).show()

                    //update status logs
                    query = "/search?query=INSERT%20INTO%20statuslogs%20VALUES(%27" + email + "%27,%27" + college + "%27,%27" + adminEmail + "%27,%27" + logTime + "%27,%270%27,%270%27,%271%27,%270%27)"
                    url = URL(ip.plus(query))
                    url.readText()

                    val intent = Intent(this, StatusRequests::class.java)
                    intent.putExtra("email", adminEmail).toString()
                    intent.putExtra("college", college)
                    startActivity(intent)
                    finish()
                }

            }else{
                if(type == "club"){
                    val curTime = LocalTime.now()
                    val curDate = LocalDate.now()
                    val logTime = curTime.toString().substringBefore(".") + " " + curDate.toString()

                    var query =
                        "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20Club_Leader_Request=%271%27"
                    var url = URL(ip.plus(query))
                    url.readText()

                    //update status logs
                    query =
                        "/search?query=INSERT%20INTO%20statuslogs%20VALUES(%27" + email + "%27,%27" + college + "%27,%27" + adminEmail + "%27,%27" + logTime + "%27,%270%27,%270%27,%270%27,%271%27)"
                    url = URL(ip.plus(query))
                    url.readText()

                    Toast.makeText(this, "Club request denied.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, StatusRequests::class.java)
                    intent.putExtra("email", adminEmail).toString()
                    intent.putExtra("college", college)
                    startActivity(intent)
                    finish()
                }else{
                    val curTime = LocalTime.now()
                    val curDate = LocalDate.now()
                    val logTime = curTime.toString().substringBefore(".") + " " + curDate.toString()

                    var query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"
                    var url = URL(ip.plus(query))
                    url.readText()

                    //update status logs
                    query = "/search?query=INSERT%20INTO%20statuslogs%20VALUES(%27" + email + "%27,%27" + college + "%27,%27" + adminEmail + "%27,%27" + logTime + "%27,%270%27,%270%27,%270%27,%271%27)"
                    url = URL(ip.plus(query))
                    url.readText()

                    Toast.makeText(this, "College change request denied.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, StatusRequests::class.java)
                    intent.putExtra("email", adminEmail).toString()
                    intent.putExtra("college", college)
                    startActivity(intent)
                    finish()
                }
            }
        }
        btnNoStatusConfirm.setOnClickListener {
            val intent = Intent(this, StatusRequests::class.java)
            intent.putExtra("email", adminEmail).toString()
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
    }
}