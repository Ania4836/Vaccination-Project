package com.example.vaccination_project

import android.widget.ArrayAdapter
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination_project.db_connection.DBconnection
import java.sql.Date
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class UpdateScheduleActivity : AppCompatActivity() {

    private lateinit var vaccineIdEditText: EditText
    private lateinit var scheduledDateEditText: EditText
    private lateinit var inputStatus: Spinner
    private lateinit var doseEditText: EditText
    private lateinit var updateButton: Button
    private var scheduleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_schedule)

        vaccineIdEditText = findViewById(R.id.vaccineIdEditText)
        scheduledDateEditText = findViewById(R.id.dateAdministeredEditText)
        inputStatus = findViewById(R.id.inputStatus)
        doseEditText = findViewById(R.id.doseEditText)
        updateButton = findViewById(R.id.updateButton)

        scheduleId = intent.getIntExtra("scheduleId", 0)

        populateFields()

        scheduledDateEditText.isFocusable = false
        scheduledDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        updateButton.setOnClickListener {
            updateSchedule()
        }
    }

    private fun populateFields() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val conn = DBconnection.getConnection()
                val statement = conn.prepareStatement("SELECT * FROM Schedule_table WHERE id = ?")
                statement.setInt(1, scheduleId)
                val resultSet = statement.executeQuery()

                if (resultSet.next()) {
                    withContext(Dispatchers.Main) {
                        vaccineIdEditText.setText(resultSet.getInt("vaccineId").toString())
                        scheduledDateEditText.setText(resultSet.getDate("scheduledDate").toString())
                        doseEditText.setText(resultSet.getInt("dose").toString())
                        val status = resultSet.getString("status")
                        val statusOptions = arrayOf("Select status", "Completed", "Ongoing")
                        val adapter = ArrayAdapter(this@UpdateScheduleActivity, android.R.layout.simple_spinner_item, statusOptions)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        inputStatus.adapter = adapter

                        val statusIndex = statusOptions.indexOf(status)
                        if (statusIndex != -1) {
                            inputStatus.setSelection(statusIndex)
                        }
                    }
                }

                conn.close()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error occurred while fetching schedule data")
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
                scheduledDateEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun updateSchedule() {
        val vaccineId = vaccineIdEditText.text.toString().toIntOrNull() ?: 0
        val dateAdministeredStr = scheduledDateEditText.text.toString()
        val status = inputStatus.selectedItem.toString()
        val dose = doseEditText.text.toString().toIntOrNull() ?: 0

        if (vaccineId == 0 || dateAdministeredStr.isEmpty() || status.isEmpty() || dose == 0) {
            showToast("Please fill in all fields")
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dateAdministered = Date.valueOf(dateAdministeredStr)
                val conn = DBconnection.getConnection()
                val statement = conn.prepareStatement(
                    "UPDATE Schedule_table SET vaccineId = ?, scheduledDate = ?, status = ?, dose = ? WHERE id = ?"
                )
                statement.setInt(1, vaccineId)
                statement.setDate(2, dateAdministered)
                statement.setString(3, status)
                statement.setInt(4, dose)
                statement.setInt(5, scheduleId)

                val rowsAffected = statement.executeUpdate()
                conn.close()
                withContext(Dispatchers.Main) {
                    if (rowsAffected > 0) {
                        showToast("Schedule updated successfully")
                        finish()
                    } else {
                        showToast("Failed to update schedule")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error occurred while updating schedule")
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
