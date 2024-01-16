package com.babumushai.library.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.babumushai.library.R

class RegisterActivity : AppCompatActivity() {
    lateinit var registerName: EditText
    lateinit var registerEmail: EditText
    lateinit var registerPhone: EditText
    lateinit var registerAddress: EditText
    lateinit var registerPassword: EditText
    lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.etRegisterName)
        registerEmail = findViewById(R.id.etRegisterEmail)
        registerPhone = findViewById(R.id.etRegisterPhone)
        registerAddress = findViewById(R.id.etRegisterAddress)
        registerPassword = findViewById(R.id.etRegisterPassword)
        registerButton = findViewById(R.id.btnRegisterButton)

        registerButton.setOnClickListener {
            val registerNameField = registerName.text.toString()
            val registerEmailField = registerEmail.text.toString()
            val registerPhoneField = registerPhone.text.toString()
            val registerAddressField = registerAddress.text.toString()
            val registerPasswordField = registerPassword.text.toString()

            // store data locally
            val sharedPreferences = getSharedPreferences("registerSharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.apply {
                putString("registerName", registerNameField)
                putString("registerEmail", registerEmailField)
                putString("registerPhone", registerPhoneField)
                putString("registerAddress", registerAddressField)
                putString("registerPassword", registerPasswordField)
            }.apply()

            val intent = Intent(this@RegisterActivity, LogInActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()

            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@RegisterActivity, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }
}