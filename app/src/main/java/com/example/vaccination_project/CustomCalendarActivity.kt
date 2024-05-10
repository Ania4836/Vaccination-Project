package com.example.vaccination_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * An activity that displays a custom calendar using a RecyclerView configured with a GridLayoutManager.
 * This activity is responsible for setting up the RecyclerView to display days of a month in a grid format
 * representing a standard calendar layout.
 *
 * It initializes the RecyclerView with a CalendarAdapter and dynamically generates a list of days for the current month
 * to demonstrate a custom calendar implementation.
 */
class CustomCalendarActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter

    /**
     * Called when the activity is starting. This is where most initialization should go: calling setContentView(int)
     * to inflate the activity's UI, using findViewById to programmatically interact with widgets in the UI,
     * setting up any adapters and view components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle
     * contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)

        recyclerView = findViewById(R.id.recyclerViewCalendar)
        recyclerView.layoutManager = GridLayoutManager(this, 7) // 7 columns for days of the week
        calendarAdapter = CalendarAdapter(getDaysOfMonth())
        recyclerView.adapter = calendarAdapter
    }

    /**
     * Generates a list of days for the current month. This simplistic version generates days numbered from 1 to 31,
     * assuming every month has 31 days which may not be accurate for all scenarios. This method can be enhanced to
     * calculate the actual number of days based on the current month and year.
     *
     * @return A list of strings where each string represents a day in the month.
     */
    private fun getDaysOfMonth(): List<String> {
        val daysOfMonth = mutableListOf<String>()
        for (i in 1..31) {
            daysOfMonth.add(i.toString())
        }
        return daysOfMonth
    }
}
