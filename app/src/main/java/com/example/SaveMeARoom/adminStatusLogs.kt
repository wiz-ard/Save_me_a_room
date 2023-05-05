package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.admin_status_logs.*
import kotlinx.android.synthetic.main.admin_user_logs.*
import kotlinx.android.synthetic.main.admin_user_logs.btnSearch
import kotlinx.android.synthetic.main.admin_userlog_info.*
import java.net.URL

class adminStatusLogs : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var adaptor: adminLogRecycleViewAdapter
    private lateinit var userLogRecycleView: RecyclerView
    private lateinit var statusLogList: ArrayList<adminLogData>
    private lateinit var statusLogText: ArrayList<String>
    private lateinit var spinData: ArrayList<String>
    private lateinit var college: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_status_logs)
        college = intent.getStringExtra("college").toString()

        //gets list of emails for spinner
        val ip = "http://3.132.20.107:3000"
        var query =
            "/search?query=SELECT%20Email_Of_Request%20FROM%20statuslogs%20WHERE%20College=%27" + college + "%27%20UNION%20SELECT%20Email_Of_Action%20FROM%20statuslogs%20WHERE%20College=%27" + college + "%27"
        var url = URL(ip.plus(query))
        var text = url.readText()
        var emails = text.split(",")
        var emailList = ArrayList<String>()
        emailList.add("Any")
        for (i in emails.indices) {
            if(emails[i] != "[]") {
                val spEmail =
                    emails[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                emailList.add(spEmail)
            }
        }

        //gets list of types for spinner
        var typeList = ArrayList<String>()
        typeList.add("Any")
        typeList.add("Club_Request")
        typeList.add("College_Request")
        typeList.add("Accept")
        typeList.add("Deny")


        spinData = arrayListOf()
        spinData.add("")
        spinData.add("")
        userLogRecycleView = findViewById(R.id.rvStatusLogs)
        userLogRecycleView.layoutManager = LinearLayoutManager(this)
        userLogRecycleView.setHasFixedSize(true)

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin = findViewById<Spinner>(R.id.spTypeSelect)
        spin.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            typeList as List<Any?>
        )

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.adapter = ad

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin2 = findViewById<Spinner>(R.id.spStatusEmailSelect)
        spin2.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            emailList as List<Any?>
        )

        // set simple layout resource file
        // for each item of spinner
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin2.adapter = ad2

        btnSearch.setOnClickListener {
            getStatusLogs(spinData)

            adaptor = adminLogRecycleViewAdapter(statusLogList) {

                //place to put click action
                val intent = Intent(this, adminStatuslogInfo::class.java)
                intent.putExtra("log info", it.component1())
                startActivity(intent)
                finish()
            }
            userLogRecycleView.adapter = adaptor
        }

        btnBackSL.setOnClickListener{
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        if (parent!!.id == R.id.spTypeSelect) {
            // first spinner selected
            val item = parent.getItemAtPosition(position)
            spinData[0] = item.toString()
        } else if (parent.id == R.id.spStatusEmailSelect) {
            // second spinner selected
            val item = parent.getItemAtPosition(position)
            spinData[1] = item.toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun getStatusLogs(spinData: ArrayList<String>) {
        tvNoStatusLog.text = ""
        if (spinData.size == 0) {
        } else {
            val type = spinData[0]
            val email = spinData[1]
            if (type.equals("Any") && email.equals("Any")) {
                statusLogList = arrayListOf()

                statusLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20Email_Of_Request,Email_Of_Action,Time_Of_Action,Club_Request,College_Request,Accept,Deny%20FROM%20statuslogs%20WHERE%20College=%27" + college + "%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                if(text.length > 2){
                    var logs = text.split(",")

                    var track = 1
                    //loop to add reservations to the recycle view
                    for (i in logs.indices) {

                        val log = adminLogData(logs[i].substringAfter(":").substringAfter('"').substringBefore('"'))
                        val logtrim = log.toString().substringAfter("=").substringBefore(")")
                        statusLogText.add(logtrim)
                        if (track % 7 == 0) {
                            val clubRequest = statusLogText[i-3]
                            val collegeRequest = statusLogText[i-2]
                            val accept = statusLogText[i-1]
                            val deny = statusLogText[i]

                            if(clubRequest == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", Club Request")
                                statusLogList.add(finalLog)
                            }

                            if(collegeRequest == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", College Request")
                                statusLogList.add(finalLog)
                            }
                            if(accept == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", Accept")
                                statusLogList.add(finalLog)
                            }
                            if(deny == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", Deny")
                                statusLogList.add(finalLog)
                            }
                        }
                        track += 1
                    }
                }else{
                    tvNoStatusLog.text = "There are no status logs to see."
                }
            } else if (type.equals("Any") && !(email.equals("Any"))) {
                statusLogList = arrayListOf()

                statusLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20Email_Of_Request,Email_Of_Action,Time_Of_Action,Club_Request,College_Request,Accept,Deny%20FROM%20statuslogs%20WHERE%20College=%27" + college + "%27%20AND%20(Email_Of_Request=%27" + email + "%27%20OR%20Email_Of_Action=%27" + email + "%27)"
                var url = URL(ip.plus(query))
                var text = url.readText()
                if(text.length > 2){
                    var logs = text.split(",")

                    var track = 1
                    //loop to add reservations to the recycle view
                    for (i in logs.indices) {

                        val log = adminLogData(logs[i].substringAfter(":").substringAfter('"').substringBefore('"'))
                        val logtrim = log.toString().substringAfter("=").substringBefore(")")
                        statusLogText.add(logtrim)
                        if (track % 7 == 0) {
                            val clubRequest = statusLogText[i-3]
                            val collegeRequest = statusLogText[i-2]
                            val accept = statusLogText[i-1]
                            val deny = statusLogText[i]

                            if(clubRequest == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", Club Request")
                                statusLogList.add(finalLog)
                            }

                            if(collegeRequest == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", College Request")
                                statusLogList.add(finalLog)
                            }
                            if(accept == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", Accept")
                                statusLogList.add(finalLog)
                            }
                            if(deny == "1"){
                                var finalLog =
                                    adminLogData(statusLogText[i - 6] + ", " + statusLogText[i - 5] + ", " + (statusLogText[i - 4].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i - 4].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i - 4].substringAfter(" ") + ", Deny")
                                statusLogList.add(finalLog)
                            }
                        }
                        track += 1
                    }
                }else{
                    tvNoStatusLog.text = "There are no status logs to see based on these filters."
                }
            } else if (!(type.equals("Any")) && (email.equals("Any"))) {
                statusLogList = arrayListOf()

                statusLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20Email_Of_Request,Email_Of_Action,Time_Of_Action%20FROM%20statuslogs%20WHERE%20College=%27" + college + "%27%20AND%20" + type + "=%271%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                if(text.length > 2){
                    var logs = text.split(",")

                    var track = 1
                    //loop to add reservations to the recycle view
                    for (i in logs.indices) {

                        val log = adminLogData(
                            logs[i].substringAfter(":").substringAfter('"').substringBefore('"')
                        )
                        val logtrim = log.toString().substringAfter("=").substringBefore(")")
                        statusLogText.add(logtrim)
                        if (track % 3 == 0) {
                            var finalLog = adminLogData(statusLogText[i - 2] + ", " + statusLogText[i - 1] + ", " + (statusLogText[i].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i].substringAfter(" ") + ", " + type)
                            statusLogList.add(finalLog)
                        }
                        track += 1
                    }
                }else{
                    tvNoStatusLog.text = "There are no status logs to see based on these filters."
                }
            } else {
                statusLogList = arrayListOf()

                statusLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query = "/search?query=SELECT%20Email_Of_Request,Email_Of_Action,Time_Of_Action%20FROM%20statuslogs%20WHERE%20College=%27" + college + "%27%20AND%20" + type + "=%271%27%20AND%20(Email_Of_Request=%27" + email + "%27%20OR%20Email_Of_Action=%27" + email + "%27)"
                var url = URL(ip.plus(query))
                var text = url.readText()
                if(text.length > 2){
                    var logs = text.split(",")

                    var track = 1
                    //loop to add reservations to the recycle view
                    for (i in logs.indices) {

                        val log = adminLogData(logs[i].substringAfter(":").substringAfter('"').substringBefore('"'))

                        val logtrim = log.toString().substringAfter("=").substringBefore(")")
                        statusLogText.add(logtrim)
                        if (track % 3 == 0) {
                            var finalLog = adminLogData(statusLogText[i - 2] + ", " + statusLogText[i - 1] + ", " + (statusLogText[i].substringBefore(" ").substringBefore(":").toInt() - 12).toString() + ":" + statusLogText[i].substringBefore(" ").substringAfter(":") + "pm" + " " + statusLogText[i].substringAfter(" ") + ", " + type)
                            statusLogList.add(finalLog)
                        }
                        track += 1
                    }
                }else{
                    tvNoStatusLog.text = "There are no status logs to see based on these filters."
                }
            }
        }
    }
}