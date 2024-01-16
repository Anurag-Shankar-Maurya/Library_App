package com.babumushai.library.activity


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.babumushai.library.R

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var phoneForgot: EditText
    lateinit var emailForgot: EditText
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        phoneForgot = findViewById(R.id.etPhoneForgot)
        emailForgot = findViewById(R.id.etEmailForgot)
        submitButton = findViewById(R.id.btnSubmit)

        submitButton.setOnClickListener {
            val phone = phoneForgot.text.toString()
            val email = emailForgot.text.toString()

            val sharedPreferences = getSharedPreferences("registerSharedPref", Context.MODE_PRIVATE)
            val savedPhone = sharedPreferences.getString("registerPhone", null)
            val savedEmail = sharedPreferences.getString("registerEmail", null)
            val savedPassword = sharedPreferences.getString("registerPassword", null)

            if (phone == savedPhone && email == savedEmail) {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Your Credentials!")
                dialog.setMessage("Your Password: ${savedPassword}")
                dialog.setCancelable(false)
                dialog.setPositiveButton("OK") { text, listner ->
                    val intent = Intent(this@ForgotPasswordActivity, LogInActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
                dialog.create()
                dialog.show()
            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("No such user!")
                dialog.setCancelable(false)
                dialog.setPositiveButton("OK") { text, listner ->
                    val intent = Intent(this@ForgotPasswordActivity, LogInActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
                dialog.create()
                dialog.show()
            }

        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ForgotPasswordActivity, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }
}