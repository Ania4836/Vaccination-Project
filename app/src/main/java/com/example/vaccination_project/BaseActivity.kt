package com.example.vaccination_project

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

/**
 * Base activity that provides common functionality to all activities within the application.
 * This includes utility methods that can be reused across multiple activities, such as showing a snackbar for error or success notifications.
 *
 * Extending this class in other activities ensures consistent behavior and styling for common UI tasks such as error handling.
 */
open class BaseActivity : AppCompatActivity() {

    /**
     * Shows a snackbar with a custom message. The background color of the snackbar indicates whether it is an error message or a success message.
     *
     * @param message The text message to be displayed in the snackbar. This should clearly communicate the success or error state to the user.
     * @param errorMessage A Boolean indicating whether the message is an error message (`true`) or a success message (`false`).
     * If `true`, the snackbar background is set to the color designated for errors, otherwise for success.
     */
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackbar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view

        if (errorMessage) {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackBarSuccessful
                )
            )
        } else {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                            R.color.snackBarError
                )
            )
        }
        snackbar.show()
    }
}





