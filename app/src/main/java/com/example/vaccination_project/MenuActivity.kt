package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViewById<View>(R.id.btnAddRecord).setOnClickListener(this)
        findViewById<View>(R.id.btnViewHistory).setOnClickListener(this)
        findViewById<View>(R.id.btnSchedule).setOnClickListener(this)
        findViewById<View>(R.id.btnViewCalendar).setOnClickListener(this) // Add this line
    }

    override fun onClick(view: View?) {
        when (view?.id) {
//            R.id.btnAddRecord -> {
//                startActivity(Intent(this, AddRecordActivity::class.java))
//            }
//            R.id.btnViewHistory -> {
//                startActivity(Intent(this, ViewHistoryActivity::class.java))
//            }
            R.id.btnSchedule -> {
                startActivity(Intent(this, ScheduleActivity::class.java))
            }
            R.id.btnViewCalendar -> {
                startActivity(Intent(this, CustomCalendarActivity::class.java))
            }
        }
    }
}
