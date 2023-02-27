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
            val username = username.text.toString()
            val password = password.text.toString()
            val hashed = password.hashCode()

            if(validInput(username) && validInput(password)){
                val ip = "http://3.132.20.107:3000"

                val query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username +"%27%20AND%20Password=" + hashed

                val url = URL(ip.plus(query))

                val text = url.readText()

                infoList = arrayListOf()


                if (text.length > 2) {
                    val userInfo = text.split(",")
                    for (i in userInfo.indices){
                        val info = userData(userInfo[i].substringAfter(":").substringAfter('"').substringBefore('"')).toString()
                        val info2 = info.substringAfter("=").substringBefore(")")
                        infoList.add(info2)

                    }
                    var nextPage = Intent(this, home::class.java)
                    if(infoList[4].equals("1")){
                        nextPage = Intent(this, AdminHome::class.java)
                    }
                    nextPage.putExtra("username", infoList[0].toString())
                    nextPage.putExtra("email", infoList[2].toString())
                    nextPage.putExtra("college", infoList[3].toString())
                    nextPage.putExtra("admin", infoList[4].toString())

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(nextPage)
                    finish()

                } else {
                    Toast.makeText(this, "Incorrect Login", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Banned characters are < > = ' and \"", Toast.LENGTH_SHORT).show()
            }
        }
        createbtn.setOnClickListener {
            val nextPage = Intent(this, createacc::class.java)
            startActivity(nextPage)
        }
    }
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