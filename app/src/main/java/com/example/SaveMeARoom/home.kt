package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.homefragment.*
import java.net.URL

class home : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val username = intent.getStringExtra("username")

        val ip = "http://3.132.20.107:3000"

        var query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        infoList = arrayListOf()

        val userInfo = text.split(",")
        for (i in userInfo.indices){
            val info = userData(userInfo[i].substringAfter(":").substringAfter('"').substringBefore('"')).toString()
            val info2 = info.substringAfter("=").substringBefore(")")
            infoList.add(info2)

        }

        // setting variables to each of the fragment layout views
        val homeFragment = Homefragment()
        val mapFragment = Mapfragment()
        val reservationFragment = Reservationfragment()
        val profileFragment = Profilefragment()

        //sets the initial fragment for when first launched

        setCurrentFragment(homeFragment,username.toString())

        // bottom navigation bar click listener
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.Home -> setCurrentFragment(homeFragment,username.toString())
                R.id.Map -> setCurrentFragment(homeFragment,username.toString())
                R.id.Reservations -> setCurrentFragment(homeFragment,username.toString())
                R.id.Profile -> setCurrentFragment(homeFragment,username.toString())
            }
            true
        }
    }

    //function for navigation bar to change fragments when clicked
    private fun setCurrentFragment(fragment: Fragment, username: String){

        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = fragment

        val mBundle = Bundle()
        mBundle.putString("username", username)
        mFragment.arguments = mBundle
        mFragmentTransaction.add(R.id.flFragment, mFragment)
        mFragmentTransaction.replace(R.id.flFragment,mFragment).commit()

    }
}