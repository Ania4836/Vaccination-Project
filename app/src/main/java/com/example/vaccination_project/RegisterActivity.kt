package com.example.vaccination_project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccination_project.db_connection.DBconnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class RegisterActivity : BaseActivity() {

    private var inputEmail: EditText? = null
    private var inputName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepPass: EditText? = null
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.inputLEmaill)
        inputName = findViewById(R.id.inputName)
        inputPassword = findViewById(R.id.inputPassword2)
        inputRepPass = findViewById(R.id.inputPassword2repeat)

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(inputEmail?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(inputName?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_name), true)
                false
            }
            TextUtils.isEmpty(inputPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(inputRepPass?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_reppassword), true)
                false
            }
            inputPassword?.text.toString().trim { it <= ' ' } != inputRepPass?.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch), true)
                false
            }
            else -> true
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            val login: String = inputEmail?.text.toString().trim { it <= ' ' }
            val password: String = inputPassword?.text.toString().trim { it <= ' ' }
            val name: String = inputName?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        saveUserToDatabase(firebaseUser.uid, name, login)

                        showErrorSnackBar(
                            "You are registered successfully. Your user id is ${firebaseUser.uid}",
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

    private fun saveUserToDatabase(userId: String, name: String, email: String) {
        val connection: Connection = DBconnection.getConnection()

        try {
            val insertStatement: PreparedStatement = connection.prepareStatement(
                "INSERT INTO users (user_id, name, email) VALUES (?, ?, ?)"
            )
            insertStatement.setString(1, userId)
            insertStatement.setString(2, name)
            insertStatement.setString(3, email)

            val rowsAffected: Int = insertStatement.executeUpdate()
            if (rowsAffected > 0) {
                println("User data inserted successfully.")
            } else {
                println("Failed to insert user data.")
            }

            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun goToLogin(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun userRegistrationSuccess() {
        Toast.makeText(this@RegisterActivity, resources.getString(R.string.register_success), Toast.LENGTH_LONG)
            .show()
    }
}
