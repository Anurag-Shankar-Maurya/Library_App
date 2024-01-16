package com.babumushai.library.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.babumushai.library.R

class LogInActivity : AppCompatActivity() {
    // declaring relative variables
    lateinit var etPhone: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogIn: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var sharedPref: SharedPreferences   // creating variable of shared preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        title = "Log In"

        // creating variable of shared preferences class
        sharedPref =
            getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE)
        // mode private ensures thar preferences file is accessed by this app only
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        // setting default value to false before a person is logged in

        // if person is already logged in display main activity bypassing log in activity
        if (isLoggedIn) {
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
            // once we are logged in, the login activity is destroyed and not resumed when we hit back button
            finish()
        }

        etPhone = findViewById(R.id.etPhoneNo)
        etPassword = findViewById(R.id.etPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)


//      LOGIN BUTTON
        btnLogIn.setOnClickListener {
            // these two statements must be below or in onCreate method as input is not taken till app is window is created
            // these should also be in onClick as it will credentials to string when use press button
            val phoneNo = etPhone.text.toString()     // 'text' is used to extract text from R file
            val password = etPassword.text.toString()

            val registerSharedPreferences =
                getSharedPreferences("registerSharedPref", Context.MODE_PRIVATE)
            val registeredPhone = registerSharedPreferences.getString("registerPhone", "")
            val registeredPassword = registerSharedPreferences.getString("registerPassword", "")
            val registeredName = registerSharedPreferences.getString("registerName", "")

            if (registeredPhone == phoneNo && registeredPassword == password) {
                val nameOfAvenger = registeredName

                if (nameOfAvenger != null) {
                    savedPreferencesFun(nameOfAvenger)
                }  // to save preferences once logged in

                Toast.makeText(
                    this@LogInActivity, "Logging in as...$nameOfAvenger",
                    Toast.LENGTH_SHORT
                ).show()

                // bridge between two activities
                val intent = Intent(this@LogInActivity, MainActivity::class.java)
                startActivity(intent)   // starting the bridge

                // to send data from one activity to another, by putting something extra on bridge
                intent.putExtra("Name", nameOfAvenger)  // Name is being received at the other end

                // once we are logged in, the login activity is destroyed and not resumed when we hit back button
                finish()

            } else {
                Toast.makeText(this@LogInActivity, "Wrong Credentials", Toast.LENGTH_SHORT).show()
            }
        }


        // FORGOT PASSWORD ON CLICK
        txtForgotPassword.setOnClickListener {
            Toast.makeText(this@LogInActivity, "You should remember...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LogInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)

            finish()
        }

        // REGISTER YOURSELF
        txtRegister.setOnClickListener {
            Toast.makeText(this@LogInActivity, "Fill your details", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LogInActivity, RegisterActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    // function to check if person is already logged in
    fun savedPreferencesFun(nameOfAvenger: String) {
        sharedPref.edit().putBoolean("isLoggedIn", true).apply()
        sharedPref.edit().putString("nameOfAvengers", nameOfAvenger).apply()
    }
}