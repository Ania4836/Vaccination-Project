package com.example.vaccination_project

import android.app.TimePickerDialog
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
import kotlin.random.Random
import com.google.firebase.auth.FirebaseAuth

class ScheduleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var btnConfirmSchedule: Button // Button to confirm the schedule
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView
    private lateinit var tvNextDoseDate: TextView
    private lateinit var calendarView: CalendarView

    private var selectedDateInMillis: Long = 0
    private var selectedTime = "09:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        btnSelectDate = findViewById(R.id.btnSelectDate)
        btnSelectTime = findViewById(R.id.btnSelectTime)
        btnConfirmSchedule = findViewById(R.id.btnConfirmSchedule)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvSelectedTime = findViewById(R.id.tvSelectedTime)
        tvNextDoseDate = findViewById(R.id.tvNextDoseDate)
        calendarView = findViewById(R.id.calendarView)

        tvSelectedTime.text = "Selected Time: $selectedTime"

        btnSelectDate.setOnClickListener(this)
        btnSelectTime.setOnClickListener(this)
        btnConfirmSchedule.setOnClickListener(this) // Set onClick listener for the confirm button

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
                if (calendarView.visibility == View.GONE) {
                    calendarView.visibility = View.VISIBLE
                } else {
                    calendarView.visibility = View.GONE
                }
            }
            R.id.btnSelectTime -> {
                showTimePickerDialog()
            }
            R.id.btnConfirmSchedule -> {
                confirmSchedule()
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        // Default time initialization to current time if not previously selected
        val defaultHour = if (selectedTime == "09:00") calendar.get(Calendar.HOUR_OF_DAY) else selectedTime.split(":")[0].toInt()
        val defaultMinute = if (selectedTime == "09:00") calendar.get(Calendar.MINUTE) else selectedTime.split(":")[1].toInt()

        TimePickerDialog(this, { _, hour, minute ->
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
            tvSelectedTime.text = "Selected Time: $selectedTime"
        }, defaultHour, defaultMinute, true).show()
    }


    private fun confirmSchedule() {
        if (selectedDateInMillis == 0L || selectedTime == "09:00") {
            Toast.makeText(this, "Please select both date and time for the schedule.", Toast.LENGTH_LONG).show()
        } else {
            // Call to insert data only if both date and time have been selected
            insertDateIntoDatabase(vaccineId = 101, status = "Ongoing", dose = 1, intervalBetweenDoses = 30)
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

    private fun insertDateIntoDatabase(vaccineId: Int?, status: String?, dose: Int?, intervalBetweenDoses: Int?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val conn: Connection = DBconnection.getConnection()
                val randomId = Random.nextInt(1000000)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(selectedDateInMillis))
                val formattedDateTime = "$formattedDate $selectedTime"

                val sql =
                    "INSERT INTO Schedule_table (id, scheduledDate, vaccineId, userId, status, dose, intervalBetweenDoses, scheduledTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                val statement = conn.prepareStatement(sql)
                statement.setInt(1, randomId)
                statement.setString(2, formattedDateTime)
                statement.setObject(3, vaccineId)
                statement.setString(4, userId)
                statement.setString(5, status)
                statement.setObject(6, dose)
                statement.setObject(7, intervalBetweenDoses)
                statement.setString(8, selectedTime)
                val result = statement.executeUpdate()
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        Toast.makeText(
                            applicationContext,
                            "Data saved successfully with ID: $randomId",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to save the data.",
                            Toast.LENGTH_SHORT
                        ).show()
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
            Toast.makeText(applicationContext, "Error: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
        }
        exception.printStackTrace()
    }
}
