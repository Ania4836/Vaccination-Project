package com.example.vaccination_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ScheduleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSelectDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var calendarView: CalendarView

    private var selectedDateInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        btnSelectDate = findViewById(R.id.btnSelectDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        calendarView = findViewById(R.id.calendarView)

        btnSelectDate.setOnClickListener(this)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            selectedDateInMillis = selectedDate.timeInMillis
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvSelectedDate.text = "Selected Date: ${dateFormat.format(selectedDate.time)}"

        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSelectDate -> {
                // Show the calendar view for date selection
                calendarView.visibility = View.VISIBLE
            }
        }
    }
}
