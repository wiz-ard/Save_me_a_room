package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime


class MainActivity : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //admin admin username password test
        loginbtn.setOnClickListener {
            //pulls values from text boxes
            val username = username.text.toString()
            val password = password.text.toString()
            val hashed = password.hashCode()

            val curTime = LocalTime.now()
            val curDate = LocalDate.now()
            val logTime = curTime.toString() + " " + curDate.toString()

            //checks for SQL injection characters
            if(validInput(username) && validInput(password)){
                if(username.length > 0 && password.length > 0) {
                    val ip = "http://3.132.20.107:3000"
                    //queries database to see if user exists
                    val query =
                        "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username + "%27%20AND%20Password=" + hashed

                    val url = URL(ip.plus(query))

                    val text = url.readText()

                    infoList = arrayListOf()


                    if (text.length > 2) {
                        val userInfo = text.split(",")
                        for (i in userInfo.indices) {
                            val info = userData(
                                userInfo[i].substringAfter(":").substringAfter('"')
                                    .substringBefore('"')
                            ).toString()
                            val info2 = info.substringAfter("=").substringBefore(")")
                            infoList.add(info2)

                        }
                        //sets default user intent
                        var nextPage = Intent(this, home::class.java)
                        //checks if admin to reset intent if so
                        if (infoList[4].equals("1")) {
                            nextPage = Intent(this, AdminHome::class.java)
                        }
                        nextPage.putExtra("username", infoList[0])
                        nextPage.putExtra("email", infoList[2])
                        nextPage.putExtra("college", infoList[3])
                        nextPage.putExtra("admin", infoList[4])
                        val college = infoList[3]

                        val ip = "http://3.132.20.107:3000"

                        val query =
                            "/search?query=INSERT%20INTO%20userlogs%20VALUES(%27" + username + "%27,%27" + logTime + "%27, %27NULL%27,%27True%27,%27" + college + "%27)"

                        val url = URL(ip.plus(query))

                        val text = url.readText()

                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        startActivity(nextPage)
                        finish()

                    } else {
                        Toast.makeText(this, "Incorrect Login", Toast.LENGTH_SHORT).show()
                        //show unsuccessful login
                        val ip = "http://3.132.20.107:3000"

                        val query =
                            "/search?query=INSERT%20INTO%20userlogs%20VALUES(%27" + username + "%27,%27NULL%27,%27" + logTime + "%27,%27False%27,%27NULL%27)"

                        val url = URL(ip.plus(query))

                        val text = url.readText()
                    }
                }else{
                    Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Banned characters are < > = ' and \"", Toast.LENGTH_SHORT).show()
            }
        }
        createbtn.setOnClickListener {
            val nextPage = Intent(this, createacc::class.java)
            startActivity(nextPage)
        }
        forgotbtn.setOnClickListener {
            val nextPage = Intent(this, forgotpass::class.java)
            startActivity(nextPage)
        }
    }
    //checks for SQL injection characters
    fun validInput(userText: String): Boolean{
        var invalidcount = 0
        for (letter in userText) {
            val ascii = letter.code
            if (ascii == 34 || ascii == 39 || ascii == 60 || ascii == 61 || ascii == 62) {
                invalidcount += 1
            }
        }
        return (invalidcount == 0)
    }
}