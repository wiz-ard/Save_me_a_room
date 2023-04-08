package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.admin_log_fragment.*
import kotlinx.android.synthetic.main.create_account.*


class AdminLogFragment: Fragment() {


    // needed to display and fill in the data when its created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_log_fragment, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        val college = bundle!!.getString("college")

        btnUser.setOnClickListener {
            val intent = Intent(activity, adminUserLogs::class.java)
            intent.putExtra("college", college)
        }
        btnRes.setOnClickListener {
            val intent = Intent(activity, adminReservationLogs::class.java)
            intent.putExtra("college", college)
        }
    }
}