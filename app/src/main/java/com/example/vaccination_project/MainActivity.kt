package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

/**
 * The main activity of the application, serving as the login screen. It allows users to enter their email
 * and password to log in to the application. If users are not registered, they can navigate to the registration screen.
 * This activity extends [BaseActivity] to inherit common UI functionality such as displaying error or success notifications
 * using Snackbar.
 *
 * Implements [View.OnClickListener] to handle click events on the login and register UI components.
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null
    private var registerTextView: TextView? = null

    /**
     * Initializes the activity with the necessary UI components and sets up click listeners for the login
     * and registration interactions.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword2)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)

        loginButton?.setOnClickListener {
            logInRegisteredUser()
        }
        registerTextView?.setOnClickListener(this)
    }

    /**
     * Handles click events on the UI components, specifically to navigate to the RegisterActivity when
     * the register option is clicked.
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.registerTextView -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * Validates the email and password input by the user. Displays a snackbar message if any field is empty
     * or if the details are validated successfully.
     *
     * @return A Boolean value indicating whether the login details are valid.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(inputEmail?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(inputPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    /**
     * Attempts to log in the user using the provided email and password. If the login is successful, navigates
     * to the MenuActivity; otherwise, displays an error message.
     */
    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            val email = inputEmail?.text.toString().trim { it <= ' ' }
            val password = inputPassword?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showErrorSnackBar("You are logged in successfully.", false)
                        goToMenuActivity()
                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    /**
     * Navigates to the MenuActivity and passes the logged-in user's email as an extra in the intent.
     */
    private fun goToMenuActivity() {
        val user = FirebaseAuth.getInstance().currentUser
        val mail = user?.email.toString()

        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("Email", mail)
        startActivity(intent)
    }
}


