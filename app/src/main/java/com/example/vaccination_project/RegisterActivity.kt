package com.example.vaccination_project

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import com.example.vaccination_project.db_connection.DBconnection
import com.example.vaccination_project.db_connection.user.DBqueriesUsers
import com.example.vaccination_project.db_connection.user.Users
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.SQLException
import java.util.Calendar
import java.util.Locale

/**
 * An activity that handles user registration. It provides form inputs for user details including name, email,
 * password, date of birth, and gender. The activity validates the input fields, communicates with Firebase to
 * create a new user, and stores additional user information in a custom database.
 *
 * Inherits common functionality from [BaseActivity], such as displaying error or success notifications via Snackbar.
 */
class RegisterActivity : BaseActivity() {

    private var inputEmail: EditText? = null
    private var inputFirstName: EditText? = null
    private var inputLastName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepeatPassword: EditText? = null
    private lateinit var inputDOB: EditText
    private lateinit var inputSex: Spinner
    private lateinit var registerButton: Button

    /**
     * Sets up the activity layout and initializes UI components. It binds click listeners to the appropriate views
     * and configures an ArrayAdapter for the gender spinner.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state. It is null the first time
     * an activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.inputEmail)
        inputFirstName = findViewById(R.id.inputFirstName)
        inputLastName = findViewById(R.id.inputLastName)
        inputPassword = findViewById(R.id.inputPassword)
        inputRepeatPassword = findViewById(R.id.inputRepeatPassword)
        inputDOB = findViewById(R.id.inputDOB)
        inputSex = findViewById(R.id.inputSex)


        val genderOptions = arrayOf("Select Gender", "Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputSex.adapter = adapter

        inputDOB.isFocusable = false
        inputDOB.setOnClickListener {
            showDatePickerDialog()
        }

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    /**
     * Validates the form details entered by the user. Checks if any fields are empty and if the passwords entered match.
     * Displays error notifications if validations fail.
     *
     * @return Boolean indicating whether the form details are valid.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(inputEmail?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(inputFirstName?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(inputLastName?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(inputPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(inputRepeatPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_repeat_password), true)
                false
            }

            inputPassword?.text.toString()
                .trim { it <= ' ' } != inputRepeatPassword?.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch), true)
                false
            }

            TextUtils.isEmpty(inputDOB?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_dob), true)
                false
            }

            inputSex.selectedItemPosition == 0 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_sex), true)
                false
            }

            else -> true
        }
    }

    /**
     * Registers the user in Firebase, handles user authentication, and stores user details in a custom database.
     * Displays a success message upon successful registration or an error message if registration fails.
     */
    private fun registerUser() {
        if (validateRegisterDetails()) {
            val userId: Int = (100000..999999).random()
            val email: String = inputEmail?.text.toString().trim()
            val firstName: String = inputFirstName?.text.toString().trim()
            val lastName: String = inputLastName?.text.toString().trim()
            val password: String = inputPassword?.text.toString().trim()
            val dateOfBirth: Date = Date.valueOf(inputDOB.text.toString().trim())
            val sex = inputSex.selectedItem.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result!!.user!!

                        saveUserToDatabase(userId, firstName, lastName, dateOfBirth, sex)

                        showErrorSnackBar(
                            "You are registered successfully. Your user id is $userId.",
                            false
                        )

                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    /**
     * Stores additional user information in the database after successful Firebase authentication.
     *
     * @param userId The unique identifier for the user.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param dateOfBirth The user's date of birth.
     * @param sex The user's gender.
     */
    private fun saveUserToDatabase(
        userId: Int,
        firstName: String,
        lastName: String,
        dateOfBirth: Date,
        sex: String
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val connection = DBconnection.getConnection()
            connection.use { conn ->
                try {
                    val connection = DBconnection.getConnection()
                    val usersQueries = DBqueriesUsers(connection)
                    val newUser = Users(userId, firstName, lastName, dateOfBirth, sex)
                    val insertSuccessful = usersQueries.insertUser(newUser)
                    connection.close()

                    if (insertSuccessful) {
                        startActivity(
                            Intent(this@RegisterActivity, MainActivity::class.java)
                        )
                        finish()

                    } else {
                        showErrorSnackBar("User registration failed", true)
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun goToLogin(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Displays a DatePicker dialog to select the date of birth. Updates the DOB EditText with the chosen date.
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
                inputDOB.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}
