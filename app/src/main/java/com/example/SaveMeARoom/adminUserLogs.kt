package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.admin_user_logs.*
import kotlinx.android.synthetic.main.admin_userlog_info.*
import java.net.URL
import java.time.LocalDate

class adminUserLogs : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var adaptor: adminLogRecycleViewAdaptor
    private lateinit var userLogRecycleView: RecyclerView
    private lateinit var userLogList: ArrayList<adminLogData>
    private lateinit var userLogText: ArrayList<String>
    private lateinit var spinData: ArrayList<String>
    private lateinit var college: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_user_logs)
        college = intent.getStringExtra("college").toString()

        //gets list of usernames for spinner
        val ip = "http://3.132.20.107:3000"
        var query =
            "/search?query=SELECT%20DISTINCT%20Username%20FROM%20userlogs%20WHERE%20College=%27" + college + "%27"
        var url = URL(ip.plus(query))
        var text = url.readText()
        var usernames = text.split(",")
        var usernameList = ArrayList<String>()
        usernameList.add("Any")
        for (i in usernames.indices) {
            val spCollege =
                usernames[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
            usernameList.add(spCollege)
        }

        //gets list of Successfuls for spinner
        query =
            "/search?query=SELECT%20DISTINCT%20Successful%20FROM%20userlogs%20WHERE%20College=%27" + college + "%27%20OR%20College=%27NULL%27"
        url = URL(ip.plus(query))
        text = url.readText()
        var successfuls = text.split(",")
        var successfulsList = ArrayList<String>()
        successfulsList.add("Any")
        successfulsList.add("Logouts")
        for (i in successfuls.indices) {
            val spSuccessful =
                successfuls[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
            if (!(spSuccessful.equals("NULL"))) {
                successfulsList.add(spSuccessful)
            }
        }
        spinData = arrayListOf()
        spinData.add("")
        spinData.add("")
        getUserLogs(spinData)
        userLogRecycleView = findViewById(R.id.rvAdminLogs)
        userLogRecycleView.layoutManager = LinearLayoutManager(this)
        userLogRecycleView.setHasFixedSize(true)

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin = findViewById<Spinner>(R.id.spUserSelect)
        spin.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            usernameList as List<Any?>
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
        val spin2 = findViewById<Spinner>(R.id.spSuccessSelect)
        spin2.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            successfulsList as List<Any?>
        )

        // set simple layout resource file
        // for each item of spinner
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin2.adapter = ad2

        btnSearch.setOnClickListener {
            getUserLogs(spinData)

            adaptor = adminLogRecycleViewAdaptor(userLogList) {

                //place to put click action
                val intent = Intent(this, adminUserlogInfo::class.java)
                intent.putExtra("log info", it.component1())
                startActivity(intent)
                finish()
            }
            userLogRecycleView.adapter = adaptor
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        if (parent!!.id == R.id.spUserSelect) {
            // first spinner selected
            val item = parent!!.getItemAtPosition(position)
            spinData[0] = item.toString()
        } else if (parent!!.id == R.id.spSuccessSelect) {
            // second spinner selected
            val item = parent!!.getItemAtPosition(position)
            spinData[1] = item.toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun getUserLogs(spinData: ArrayList<String>) {
        if (spinData.size == 0) {
        } else {
            val user = spinData[0]
            val successful = spinData[1]
            if (user.equals("Any") && !(successful.equals("Any")) && !(successful.equals("Logouts"))) {
                userLogList = arrayListOf()

                userLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20*%20FROM%20userlogs%20WHERE%20(College=%27" + college + "%27%20OR%20College=%27NULL%27)%20AND%20Successful=%27" + successful + "%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                var logs = text.split(",")

                var track = 1
                //loop to add reservations to the recycle view
                for (i in logs.indices) {

                    val log = adminLogData(
                        logs[i].substringAfter(":").substringAfter('"').substringBefore('"')
                    )
                    val logtrim = log.toString().substringAfter("=").substringBefore(")")
                    userLogText.add(logtrim)
                    if (track % 5 == 0) {
                        var finalLog =
                            adminLogData(userLogText[i - 4] + ", " + userLogText[i - 3] + ", " + userLogText[i - 2] + ", " + userLogText[i - 1])
                        userLogList.add(finalLog)
                    }
                    track += 1
                }
            } else if (successful.equals("Any") && !(user.equals("Any"))) {
                userLogList = arrayListOf()

                userLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20*%20FROM%20userlogs%20WHERE%20(College=%27" + college + "%27%20OR%20College=%27NULL%27)%20AND%20Username=%27" + user + "%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                var logs = text.split(",")

                var track = 1
                //loop to add reservations to the recycle view
                for (i in logs.indices) {

                    val log = adminLogData(
                        logs[i].substringAfter(":").substringAfter('"').substringBefore('"')
                    )
                    val logtrim = log.toString().substringAfter("=").substringBefore(")")
                    userLogText.add(logtrim)
                    if (track % 5 == 0) {
                        var finalLog =
                            adminLogData(userLogText[i - 4] + ", " + userLogText[i - 3] + ", " + userLogText[i - 2] + ", " + userLogText[i - 1])
                        userLogList.add(finalLog)
                    }
                    track += 1
                }
            } else if (successful.equals("Logouts") && user.equals("Any")) {
                userLogList = arrayListOf()

                userLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20*%20FROM%20userlogs%20WHERE%20(College=%27" + college + "%27%20OR%20College=%27NULL%27)%20AND%20Successful=%27NULL%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                var logs = text.split(",")

                var track = 1
                //loop to add reservations to the recycle view
                for (i in logs.indices) {

                    val log = adminLogData(
                        logs[i].substringAfter(":").substringAfter('"').substringBefore('"')
                    )
                    val logtrim = log.toString().substringAfter("=").substringBefore(")")
                    userLogText.add(logtrim)
                    if (track % 5 == 0) {
                        var finalLog =
                            adminLogData(userLogText[i - 4] + ", " + userLogText[i - 3] + ", " + userLogText[i - 2] + ", " + userLogText[i - 1])
                        userLogList.add(finalLog)
                    }
                    track += 1
                }
            } else if (successful.equals("Logouts") && !(user.equals("Any"))) {
                userLogList = arrayListOf()

                userLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20*%20FROM%20userlogs%20WHERE%20(College=%27" + college + "%27%20OR%20College=%27NULL%27)%20AND%20Successful=%27NULL%27%20AND%20Username=%27" + user + "%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                var logs = text.split(",")

                var track = 1
                //loop to add reservations to the recycle view
                for (i in logs.indices) {

                    val log = adminLogData(
                        logs[i].substringAfter(":").substringAfter('"').substringBefore('"')
                    )
                    val logtrim = log.toString().substringAfter("=").substringBefore(")")
                    userLogText.add(logtrim)
                    if (track % 5 == 0) {
                        var finalLog =
                            adminLogData(userLogText[i - 4] + ", " + userLogText[i - 3] + ", " + userLogText[i - 2] + ", " + userLogText[i - 1])
                        userLogList.add(finalLog)
                    }
                    track += 1
                }
            } else if (!(successful.equals("Any")) && !(user.equals("Any"))) {
                userLogList = arrayListOf()

                userLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query =
                    "/search?query=SELECT%20*%20FROM%20userlogs%20WHERE%20(College=%27" + college + "%27%20OR%20College=%27NULL%27)%20AND%20Username=%27" + user + "%27%20AND%20Successful=%27" + successful + "%27"
                var url = URL(ip.plus(query))
                var text = url.readText()
                var logs = text.split(",")

                var track = 1
                //loop to add reservations to the recycle view
                for (i in logs.indices) {

                    val log = adminLogData(
                        logs[i].substringAfter(":").substringAfter('"').substringBefore('"')
                    )
                    val logtrim = log.toString().substringAfter("=").substringBefore(")")
                    userLogText.add(logtrim)
                    if (track % 5 == 0) {
                        var finalLog =
                            adminLogData(userLogText[i - 4] + ", " + userLogText[i - 3] + ", " + userLogText[i - 2] + ", " + userLogText[i - 1])
                        userLogList.add(finalLog)
                    }
                    track += 1
                }
            } else {
                userLogList = arrayListOf()

                userLogText = arrayListOf()
                val ip = "http://3.132.20.107:3000"
                var query = "/search?query=SELECT%20*%20FROM%20userlogs%20WHERE%20(College=%27" + college + "%27%20OR%20College=%27NULL%27)"
                var url = URL(ip.plus(query))
                var text = url.readText()
                var logs = text.split(",")

                var track = 1
                //loop to add reservations to the recycle view
                for (i in logs.indices) {

                    val log = adminLogData(logs[i].substringAfter(":").substringAfter('"').substringBefore('"'))

                    val logtrim = log.toString().substringAfter("=").substringBefore(")")
                    userLogText.add(logtrim)
                    if (track % 5 == 0) {
                        var finalLog = adminLogData(userLogText[i - 4] + ", " + userLogText[i - 3] + ", " + userLogText[i - 2] + ", " + userLogText[i - 1])
                        userLogList.add(finalLog)
                    }
                    track += 1
                }
            }
        }
    }
}