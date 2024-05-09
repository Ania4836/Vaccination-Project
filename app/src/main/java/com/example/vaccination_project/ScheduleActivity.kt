package com.example.vaccination_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.vaccination_project.db_connection.DBconnection
import kotlinx.coroutines.*
import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.*

class ScheduleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSelectDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvNextDoseDate: TextView
    private lateinit var calendarView: CalendarView

    private var selectedDateInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        btnSelectDate = findViewById(R.id.btnSelectDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvNextDoseDate = findViewById(R.id.tvNextDoseDate)
        calendarView = findViewById(R.id.calendarView)

        btnSelectDate.setOnClickListener {
            calendarView.visibility = if (calendarView.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            selectedDateInMillis = selectedDate.timeInMillis
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvSelectedDate.text = "Selected Date: ${dateFormat.format(selectedDate.time)}"
            displayNextDoseDate()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSelectDate -> {
                if (calendarView.visibility == View.VISIBLE) {
                    calendarView.visibility = View.GONE
                    insertDateIntoDatabase()
                    displayNextDoseDate()
                } else {
                    calendarView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun displayNextDoseDate() {
        val interval = 30
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateInMillis
        calendar.add(Calendar.DAY_OF_YEAR, interval)

        val nextDoseDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        tvNextDoseDate.text = "Next Dose: $nextDoseDate"
    }

    private fun insertDateIntoDatabase() {
        lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val conn: Connection = DBconnection.getConnection()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(selectedDateInMillis))
                val sql = "INSERT INTO VaccinationDates (date) VALUES (?)"
                val statement = conn.prepareStatement(sql)
                statement.setString(1, formattedDate)
                val result = statement.executeUpdate()
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        tvSelectedDate.text = "Date saved: $formattedDate"
                    } else {
                        tvSelectedDate.text = "Failed to save the date."
                    }
                }
                conn.close()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                e.printStackTrace()
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            lifecycleScope.launch {
                Toast.makeText(
                    applicationContext,
                    "Error: ${exception.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        exception.printStackTrace()
    }
}

