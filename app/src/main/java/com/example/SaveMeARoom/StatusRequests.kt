package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.status_request.*
import java.net.URL

class StatusRequests : AppCompatActivity() {

    private lateinit var adaptor: StatusRequestRecycleAdaptor
    private lateinit var StatusReqRecyler: RecyclerView
    private lateinit var requestList: ArrayList<statusReqData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.status_request)

        // get the lists for the spinner values (building, date, time, status)
        val college = intent.getStringExtra("college")

        // set the button
        btnClubReq.setOnClickListener {
            requestList = arrayListOf()

            val ip = "http://3.132.20.107:3000"

            val query = "/search?query=SELECT%20Email,Name_Of_Club%20FROM%20statusrequests%20WHERE%20Club_Leader_Request=%271%27%20AND%20College=%27" + college + "%27"

            val url = URL(ip.plus(query))

            var text = url.readText()

            var requests = text.split(",")

            var track = 1

            for (i in requests.indices){
                if(requests[i] != "[]" && track % 2 == 0){
                    val email = requests[i-1].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    val clubName = requests[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    requestList.add(statusReqData(email + ", " + clubName))
                    track += 1
                }else{
                    track += 1
                }
            }

            StatusReqRecyler = findViewById(R.id.rvStatusRequests)
            StatusReqRecyler.layoutManager = LinearLayoutManager(this)
            StatusReqRecyler.setHasFixedSize(true)
            adaptor = StatusRequestRecycleAdaptor(requestList){
                val intent = Intent(this, adminClubConfirmation::class.java)
                intent.putExtra("request info", it.component1())

                val vals = it.component1().split(",")

                val email = vals[0]

                val ip = "http://3.132.20.107:3000"

                val query = "/search?query=UPDATE%20statusrequests%20SET%20Viewing=1%20WHERE%20Email=%27" + email + "%27"

                val url = URL(ip.plus(query))

                url.readText()

                startActivity(intent)
                finish()
            }
            StatusReqRecyler.adapter = adaptor
        }
        btnCollegeReq.setOnClickListener {
            requestList = arrayListOf()

            val ip = "http://3.132.20.107:3000"

            var query = "/search?query=SELECT%20Email,New_College%20FROM%20statusrequests%20WHERE%20College_Request=%271%27%20AND%20College=%27" + college + "%27"

            var url = URL(ip.plus(query))

            var text = url.readText()

            var requests = text.split(",")

            var track = 1

            for (i in requests.indices){
                if(requests[i] != "[]" && track % 2 == 0){
                    val userName = requests[i-1].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    val clubName = requests[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    requestList.add(statusReqData(userName + ", " + clubName))
                    track += 1
                }else{
                    track += 1
                }
            }

            StatusReqRecyler = findViewById(R.id.rvStatusRequests)
            StatusReqRecyler.layoutManager = LinearLayoutManager(this)
            StatusReqRecyler.setHasFixedSize(true)
            adaptor = StatusRequestRecycleAdaptor(requestList){
                val intent = Intent(this, adminCollegeConfirmation::class.java)
                intent.putExtra("request info", it.component1())
                val vals = it.component1().split(",")

                val email = vals[0]

                val ip = "http://3.132.20.107:3000"

                val query = "/search?query=UPDATE%20statusrequests%20SET%20Viewing=1%20WHERE%20Email=%27" + email + "%27"

                val url = URL(ip.plus(query))

                url.readText()

                intent.putExtra("college", college)
                startActivity(intent)
                finish()
            }
            StatusReqRecyler.adapter = adaptor
        }
    }
}
