package com.c242ps188.mentally_app.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var showPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListener()
    }

    private fun setListener() {
        binding.showPassword.setOnClickListener {
            showPassword = !showPassword

            if (showPassword) {
                binding.edLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showPassword.setImageResource(R.drawable.ic_password)
            } else {
                binding.edLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showPassword.setImageResource(R.drawable.ic_password_hiden)
            }

            binding.edLoginPassword.text?.let {  binding.edLoginPassword.setSelection(it.length) }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.edLoginEmail.addTextChangedListener { text: Editable? ->
            text?.let {
                if (it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    binding.edLoginEmail.error = getString(R.string.invalid_email)
                }
            }

        }
    }
}