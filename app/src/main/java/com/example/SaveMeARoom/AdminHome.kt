package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.admin_home.*
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.homefragment.*
import java.net.URL

class AdminHome : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_home)

        val username = intent.getStringExtra("username")

        // setting variables to each of the fragment layout views
        val homeFragment = AdminHomefragment()
        val calendarFragment = AdminCalendarFragment()
        val profileFragment = Profilefragment()

        //sets the initial fragment for when first launched

        setCurrentFragment(homeFragment,username.toString())

        // bottom navigation bar click listener
        adminBottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.AdminHome -> setCurrentFragment(homeFragment,username.toString())
                R.id.AdminCalandar -> setCurrentFragment(calendarFragment,username.toString())
                R.id.AdminProfile -> setCurrentFragment(profileFragment,username.toString())
            }
            true
        }
    }

    //function for navigation bar to change fragments when clicked
    private fun setCurrentFragment(fragment: Fragment, username: String){
        val ip = "http://3.132.20.107:3000"

        val query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username +"%27"

        val url = URL(ip.plus(query))

        val text = url.readText()

        infoList = arrayListOf()

        val userInfo = text.split(",")
        for (i in userInfo.indices){
            val info = userData(userInfo[i].substringAfter(":").substringAfter('"').substringBefore('"')).toString()
            val info2 = info.substringAfter("=").substringBefore(")")
            infoList.add(info2)
        }

        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = fragment

        val mBundle = Bundle()
        mBundle.putString("college",infoList[3])
        mBundle.putString("username", username)
        mFragment.arguments = mBundle
        mFragmentTransaction.add(R.id.flAdminFragment, mFragment)
        mFragmentTransaction.replace(R.id.flAdminFragment,mFragment).commit()

    }
}