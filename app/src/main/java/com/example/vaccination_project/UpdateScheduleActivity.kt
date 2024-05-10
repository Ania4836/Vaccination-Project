package com.example.vaccination_project

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination_project.db_connection.schedule_date.ScheduleDate
import java.text.SimpleDateFormat
import java.util.Locale

class UpdateScheduleActivity : AppCompatActivity() {

    private lateinit var vaccineIdEditText: EditText
    private lateinit var dateScheduledEditText: EditText
    private lateinit var inputStatus: Spinner
    private lateinit var doseEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var scheduleItem: ScheduleDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_schedule)

        vaccineIdEditText = findViewById(R.id.vaccineIdEditText)
        dateScheduledEditText = findViewById(R.id.dateAdministeredEditText)
        doseEditText = findViewById(R.id.doseEditText)
        updateButton = findViewById(R.id.updateButton)
        inputStatus = findViewById(R.id.inputStatus)

        dateScheduledEditText.isFocusable = false
        dateScheduledEditText.setOnClickListener {
            showDatePickerDialog()
        }

        val statusOptions = arrayOf("Select status", "Completed", "Ongoing")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputStatus.adapter = adapter

        // Retrieve the scheduleItem passed from HistoryActivity
        scheduleItem = intent.getParcelableExtra("scheduleItem")!!

        // Populate EditText fields with existing data
        vaccineIdEditText.setText(scheduleItem.vaccineId.toString())
        dateScheduledEditText.setText(scheduleItem.scheduledDate.toString())
        doseEditText.setText(scheduleItem.dose.toString())
        inputStatus.setSelection(statusOptions.indexOf(scheduleItem.status))

        // Set click listener for updateButton
        updateButton.setOnClickListener {
            // Call a function to update the schedule item
            updateSchedule()
        }
    }

    private fun showDatePickerDialog() {
        // Implementation of DatePickerDialog remains unchanged from AddVaccinationRecordActivity
        // You can copy the existing code from there.
    }

    private fun updateSchedule() {
        val vaccineId = vaccineIdEditText.text.toString().toIntOrNull() ?: 0
        val dateScheduledStr = dateScheduledEditText.text.toString()
        val status = inputStatus.selectedItem.toString()
        val dose = doseEditText.text.toString().toIntOrNull() ?: 0

        if (vaccineId == 0 || dateScheduledStr.isEmpty() || status.isEmpty() || dose == 0) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val dateScheduled = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateScheduledStr)

        // Implement the update logic here, for example:
        // You can call a method in your data access layer to update the schedule item in the database
        // After updating, you can display a success message and finish the activity

        Toast.makeText(this, "Schedule updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}
