package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.admin_home.*
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.homefragment.*

class AdminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_home)

        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val college = intent.getStringExtra("college")
        val admin = intent.getStringExtra("admin")



        // setting variables to each of the fragment layout views
        val homeFragment = AdminHomefragment()
        val calendarFragment = AdminCalendarFragment()
        val profileFragment = Profilefragment()

        //sets the initial fragment for when first launched

        setCurrentFragment(homeFragment,college.toString(),email.toString(),username.toString(),admin.toString())

        // bottom navigation bar click listener
        adminBottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.AdminHome -> setCurrentFragment(homeFragment,college.toString(),email.toString(),username.toString(),admin.toString())
                R.id.AdminCalandar -> setCurrentFragment(calendarFragment,college.toString(),email.toString(),username.toString(),admin.toString())
                R.id.AdminProfile -> setCurrentFragment(profileFragment,college.toString(),email.toString(),username.toString(),admin.toString())
            }
            true
        }
    }

    //function for navigation bar to change fragments when clicked
    private fun setCurrentFragment(fragment: Fragment, college: String, email: String, username: String, admin: String){

        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = fragment

        val mBundle = Bundle()
        mBundle.putString("college",college)
        mBundle.putString("email", email)
        mBundle.putString("username", username)
        mBundle.putString("admin", admin)
        mFragment.arguments = mBundle
        mFragmentTransaction.add(R.id.flAdminFragment, mFragment)
        mFragmentTransaction.replace(R.id.flAdminFragment,mFragment).commit()

    }
}