package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), View.OnClickListener {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null
    private var registerTextView: TextView? = null

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

    private fun goToMenuActivity() {
        val user = FirebaseAuth.getInstance().currentUser
        val mail = user?.email.toString()

        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("Email", mail)
        startActivity(intent)
    }
}


