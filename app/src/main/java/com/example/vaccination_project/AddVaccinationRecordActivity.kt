package com.example.vaccination_project

import android.app.DatePickerDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination_project.db_connection.DBconnection
import com.google.firebase.auth.FirebaseAuth
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class AddVaccinationRecordActivity : AppCompatActivity() {

    private lateinit var vaccineIdEditText: EditText
    private lateinit var dateScheduledEditText: EditText
    private lateinit var inputStatus: Spinner
    private lateinit var doseEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccination_record)

        vaccineIdEditText = findViewById(R.id.vaccineIdEditText)
        dateScheduledEditText = findViewById(R.id.dateAdministeredEditText)
        doseEditText = findViewById(R.id.doseEditText)
        submitButton = findViewById(R.id.submitButton)
        inputStatus = findViewById(R.id.inputStatus)

        dateScheduledEditText.isFocusable = false
        dateScheduledEditText.setOnClickListener {
            showDatePickerDialog()
        }

        val statusOptions = arrayOf("Select status", "Completed", "Ongoing")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputStatus.adapter = adapter

        submitButton.setOnClickListener {
            addVaccinationRecord()
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
                dateScheduledEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun addVaccinationRecord() {
        val vaccineId = vaccineIdEditText.text.toString().toIntOrNull() ?: 0
        val dateScheduledStr = dateScheduledEditText.text.toString()
        val status = inputStatus.selectedItem.toString()
        val dose = doseEditText.text.toString().toIntOrNull() ?: 0

        if (vaccineId == 0 || dateScheduledStr.isEmpty() || status.isEmpty() || dose == 0) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val dateScheduled = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateScheduledStr)
        val userId = getUserId()
        val id = generateUniqueId()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val conn = DBconnection.getConnection()
                val insertStatement = conn.prepareStatement(
                    "INSERT INTO Schedule_table (id, vaccineId, userId, doctorId, scheduledTime, status, scheduledDate, dose, intervalBetweenDoses) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                )


                val defaultTime = Time(System.currentTimeMillis())
                val defaultInterval = 0

                insertStatement.setInt(1, id)
                insertStatement.setInt(2, vaccineId)
                insertStatement.setString(3, userId)
                insertStatement.setNull(4, java.sql.Types.INTEGER)
                insertStatement.setTime(5, defaultTime) // Set default value for scheduledTime
                insertStatement.setString(6, status)
                insertStatement.setDate(7, Date(dateScheduled.time))
                insertStatement.setInt(8, dose)
                insertStatement.setInt(9, defaultInterval) // Set default value for intervalBetweenDoses

                val rowsAffected = insertStatement.executeUpdate()
                if (rowsAffected > 0) {
                    showToast("Vaccination record added successfully")
                    clearFields()
                } else {
                    showToast("Failed to add vaccination record")
                }

                conn.close()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error occurred")
            }
        }
    }


    private fun generateUniqueId(): Int {
        // Generating a unique ID using timestamp and random number
        return System.currentTimeMillis().toInt() + Random.nextInt(1000)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun getUserId(): String? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return userId
    }

    private fun clearFields() {
        vaccineIdEditText.text.clear()
        dateScheduledEditText.text.clear()
        doseEditText.text.clear()
    }
}
