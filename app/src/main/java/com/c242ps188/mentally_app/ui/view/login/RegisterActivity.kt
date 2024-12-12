package com.c242ps188.mentally_app.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityRegisterBinding
import com.c242ps188.mentally_app.ui.viewmodel.LoginViewModel
import com.c242ps188.mentally_app.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val loginViewModel: LoginViewModel by viewModels { factory }
    private var showPassword = false
    private var showPasswordConfirm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        observe()
        setListener()
    }

    private fun observe() {
        loginViewModel.registerMessage.observe(this) { message ->
            message?.let {
                if (it == "Register successful") {
                    showAlertDialog()
                } else {
                    showToast(it)
                }
            }
        }

        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setListener() {

        binding.tvLogin.setOnClickListener {
            finish()
        }

        binding.registerShowPassword.setOnClickListener {
            showPassword = !showPassword

            if (showPassword) {
                binding.edRegisterPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.registerShowPassword.setImageResource(R.drawable.ic_password)
            } else {
                binding.edRegisterPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.registerShowPassword.setImageResource(R.drawable.ic_password_hiden)
            }

            binding.edRegisterPassword.text?.let { binding.edRegisterPassword.setSelection(it.length) }
        }

        binding.registerShowPasswordConfirm.setOnClickListener {
            showPasswordConfirm = !showPasswordConfirm

            if (showPasswordConfirm) {
                binding.edRegisterPasswordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.registerShowPasswordConfirm.setImageResource(R.drawable.ic_password)
            } else {
                binding.edRegisterPasswordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.registerShowPasswordConfirm.setImageResource(R.drawable.ic_password_hiden)
            }

            binding.edRegisterPasswordConfirm.text?.let {
                binding.edRegisterPasswordConfirm.setSelection(
                    it.length
                )
            }
        }

        binding.edRegisterEmail.addTextChangedListener { text: Editable? ->
            text?.let {
                if (it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    binding.edRegisterEmail.error = getString(R.string.invalid_email)
                }
            }

        }

        binding.btnRegister.setOnClickListener {
            var valid = true

            val firstName = binding.edFirstName.text.toString().trim()
            val lastName = binding.edLastName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()
            val passwordConfirm = binding.edRegisterPasswordConfirm.text.toString().trim()

            if (firstName.isEmpty()) {
                valid = false
                binding.edFirstName.error = getString(R.string.name_is_required)
            }

            if (lastName.isEmpty()) {
                valid = false
                binding.edLastName.error = getString(R.string.name_is_required)
            }

            if (email.isEmpty()) {
                valid = false
                binding.edRegisterEmail.error = getString(R.string.email_required)
            }

            if (password.isEmpty()) {
                valid = false
                binding.edRegisterPassword.error = getString(R.string.password_required)
            }

            if (passwordConfirm.isEmpty()) {
                valid = false
                binding.edRegisterPasswordConfirm.error = getString(R.string.password_required)
            }

            if (valid) {
                loginViewModel.register("$firstName $lastName", email, password, passwordConfirm)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.registration_successful))
            .setMessage(getString(R.string.success_your_account_is_ready_please_log_in_to_access_your_mental_ly_experience))
            .setPositiveButton(getString(R.string.login)) { _, _ ->
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

        builder.create().show()
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
        }
    }
}