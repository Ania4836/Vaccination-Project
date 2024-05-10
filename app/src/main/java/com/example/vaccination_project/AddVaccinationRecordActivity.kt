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

/**
 * An activity that facilitates the addition of vaccination records into the database. This includes scheduling details such as the vaccine ID,
 * the date the vaccine was administered, the dose, and the status of the vaccination. The activity provides a user interface for inputting these details,
 * validates them, and submits them to the database if they are correct.
 *
 * Inherits from [AppCompatActivity] and provides methods to interact with UI components like EditTexts for input, a Spinner for status selection,
 * and a Button to submit the data.
 */
class AddVaccinationRecordActivity : AppCompatActivity() {

    private lateinit var vaccineIdEditText: EditText
    private lateinit var dateScheduledEditText: EditText
    private lateinit var inputStatus: Spinner
    private lateinit var doseEditText: EditText
    private lateinit var submitButton: Button

    /**
     * Initializes the activity, sets up the content view and binds the UI components. It also initializes a DatePickerDialog
     * for the date input and sets an ArrayAdapter for the status spinner.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
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

    /**
     * Displays a DatePicker dialog allowing users to select a date for the vaccination record. The selected date
     * is then displayed in the dateScheduledEditText.
     */
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

    /**
     * Gathers the data from input fields, validates them, and submits the record to the database.
     * Displays a Toast message based on the success of the operation.
     */
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

    /**
     * Generates a unique integer ID using a combination of the current timestamp and a random number.
     *
     * @return An integer representing a unique identifier.
     */
    private fun generateUniqueId(): Int {
        // Generating a unique ID using timestamp and random number
        return System.currentTimeMillis().toInt() + Random.nextInt(1000)
    }

    /**
     * Utility method to display Toast messages on the UI thread.
     *
     * @param message The message to display in the Toast.
     */
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Retrieves the Firebase user ID of the currently authenticated user.
     *
     * @return The Firebase user ID as a string, or null if no user is authenticated.
     */
    private fun getUserId(): String? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        return userId
    }

    /**
     * Clears all input fields after successful data submission or when resetting the form.
     */
    private fun clearFields() {
        vaccineIdEditText.text.clear()
        dateScheduledEditText.text.clear()
        doseEditText.text.clear()
    }
}
