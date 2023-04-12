package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.admin_home_fragment.*

class AdminHomefragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_home_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        val college = bundle!!.getString("college")

        val email = bundle!!.getString("email")

        resReq.setOnClickListener {
            val intent = Intent(activity, RoomRequests::class.java)
            intent.putExtra("college", college)
            startActivity(intent)
        }
        statReq.setOnClickListener {
            val intent = Intent(activity, StatusRequests::class.java)
            intent.putExtra("college", college)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}
