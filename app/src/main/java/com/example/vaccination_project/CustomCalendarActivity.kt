package com.example.vaccination_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomCalendarActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)

        recyclerView = findViewById(R.id.recyclerViewCalendar)
        recyclerView.layoutManager = GridLayoutManager(this, 7) // 7 columns for days of the week
        calendarAdapter = CalendarAdapter(getDaysOfMonth())
        recyclerView.adapter = calendarAdapter
    }

    private fun getDaysOfMonth(): List<String> {
        val daysOfMonth = mutableListOf<String>()
        for (i in 1..31) {
            daysOfMonth.add(i.toString())
        }
        return daysOfMonth
    }
}
