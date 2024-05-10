package com.example.vaccination_project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination_project.db_connection.DBconnection
import java.text.SimpleDateFormat
import java.util.*

/**
 * Activity for adding a new vaccination record to the database. Provides user interface components
 * for entering the vaccine name, the date the vaccine was administered, and the due date for the next dose.
 * This activity handles form submission and inserts the vaccination data into a database.
 *
 * The activity ensures that all input fields are filled before allowing the record to be added
 * and provides feedback on the success or failure of the operation.
 */
class AddVaccinationRecordActivity : AppCompatActivity() {

    private lateinit var vaccineNameEditText: EditText
    private lateinit var dateAdministeredEditText: EditText
    private lateinit var nextDoseDueDateEditText: EditText
    private lateinit var submitButton: Button

    /**
     * Initializes the activity, sets the content view, and binds UI components.
     * It also sets up a click listener on the submit button to trigger the process of adding a vaccination record.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the most recent data supplied by onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccination_record)

        vaccineNameEditText = findViewById(R.id.vaccineNameEditText)
        dateAdministeredEditText = findViewById(R.id.dateAdministeredEditText)
        nextDoseDueDateEditText = findViewById(R.id.nextDoseDueDateEditText)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            addVaccinationRecord()
        }
    }

    /**
     * Handles the form submission by extracting data from input fields, validating them,
     * and attempting to add a new vaccination record into the database.
     * Displays toast messages to indicate success or failure.
     */
    private fun addVaccinationRecord() {
        val vaccineName = vaccineNameEditText.text.toString()
        val dateAdministeredStr = dateAdministeredEditText.text.toString()
        val nextDoseDueDateStr = nextDoseDueDateEditText.text.toString()

        if (vaccineName.isEmpty() || dateAdministeredStr.isEmpty() || nextDoseDueDateStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val dateAdministered = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateAdministeredStr)
            val nextDoseDueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(nextDoseDueDateStr)

            val userId = getUserId()

            val conn = DBconnection.getConnection()
            val insertStatement = conn.prepareStatement(
                "INSERT INTO vaccination_records (user_id, vaccine_name, date_administered, next_dose_due_date) VALUES (?, ?, ?, ?)"
            )
            insertStatement.setInt(1, userId)
            insertStatement.setString(2, vaccineName)
            insertStatement.setDate(3, java.sql.Date(dateAdministered.time))
            insertStatement.setDate(4, java.sql.Date(nextDoseDueDate.time))

            val rowsAffected = insertStatement.executeUpdate()
            if (rowsAffected > 0) {
                Toast.makeText(this, "Vaccination record added successfully", Toast.LENGTH_SHORT).show()
                // clear input field laterrr
                clearFields()
            } else {
                Toast.makeText(this, "Failed to add vaccination record", Toast.LENGTH_SHORT).show()
            }

            conn.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Retrieves the current user's ID. This function assumes the user is already registered and has a valid ID.
     *
     * @return The user ID.
     */
    private fun getUserId(): Int {
    //get id!!!!!!! do it after register id in databasee
        return 1
    }

    /**
     * Clears all input fields in the form after successfully adding a vaccination record or when preparing
     * the form for new input.
     */
    private fun clearFields() {
        vaccineNameEditText.text.clear()
        dateAdministeredEditText.text.clear()
        nextDoseDueDateEditText.text.clear()
    }
}
