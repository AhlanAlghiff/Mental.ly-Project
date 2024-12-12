package com.c242ps188.mentally_app.ui.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityLoginBinding
import com.c242ps188.mentally_app.ui.view.home.HomeActivity
import com.c242ps188.mentally_app.ui.viewmodel.LoginViewModel
import com.c242ps188.mentally_app.ui.viewmodel.SettingsViewModel
import com.c242ps188.mentally_app.ui.viewmodel.UsersViewModel
import com.c242ps188.mentally_app.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var showPassword = false
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val loginViewModel: LoginViewModel by viewModels { factory }
    private val usersViewModel: UsersViewModel by viewModels { factory }
    private val settingsViewModel: SettingsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        observe()
        setListener()
        setAnimation()
    }

    private fun setAnimation() {
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).apply {
            duration = 400
        }
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).apply {
            duration = 400
        }
        val logo = ObjectAnimator.ofFloat(binding.appLogo, View.ALPHA, 1f).apply {
            duration = 800
        }
        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).apply {
            duration = 800
        }
        val showPassword = ObjectAnimator.ofFloat(binding.showPassword, View.ALPHA, 1f).apply {
            duration = 800
        }
        val btnLogin = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).apply {
            duration = 400
        }
        val tvOr = ObjectAnimator.ofFloat(binding.tvOr, View.ALPHA, 1f).apply {
            duration = 300
        }
        val dontHaveAccount = ObjectAnimator.ofFloat(binding.tvDontHaveAcount, View.ALPHA, 1f).apply {
            duration = 300
        }
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).apply {
            duration = 300
        }

        val playTogether1 = AnimatorSet().apply {
            playTogether(logo, welcome)
        }
        val playTogether2 = AnimatorSet().apply {
            playTogether(tvOr, dontHaveAccount, tvRegister)
        }
        val playTogether3 = AnimatorSet().apply {
            playTogether(password, showPassword)
        }

        AnimatorSet().apply {
            playSequentially(playTogether1, email, playTogether3, btnLogin, playTogether2)
            start()
        }
    }

    private fun observe() {
        var token: String? = null
        var name: String? = null
        var id: String? = null
        var email: String? = null

        settingsViewModel.getTheme.observe(this){ isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        loginViewModel.userToken.observe(this) { userToken ->
            userToken?.let {
                token = it
            }
        }

        loginViewModel.loginResult.observe(this) { user ->
            user?.let { data ->
                name = data.name
                id = data.id
                email = data.email
            }
        }

        loginViewModel.loginMessage.observe(this) { message ->
            if (message == "200") {
                val currentToken = token
                val currentName = name
                val currentId = id
                val currentEmail = email

                if (currentToken != null && currentName != null && currentId != null && currentEmail != null) {
                    usersViewModel.saveSession(currentToken, currentName, currentId, currentEmail)
                }

                val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)

                loginViewModel.removeLoginInfo()
            } else {
                if (message != null) {
                    showToast(message)
                }
            }
        }
    }

    private fun setListener() {
        binding.showPassword.setOnClickListener {
            showPassword = !showPassword

            if (showPassword) {
                binding.edLoginPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showPassword.setImageResource(R.drawable.ic_password)
            } else {
                binding.edLoginPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showPassword.setImageResource(R.drawable.ic_password_hiden)
            }

            binding.edLoginPassword.text?.let { binding.edLoginPassword.setSelection(it.length) }
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

        binding.loginButton.setOnClickListener {
            var valid = true

            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty()) {
                valid = false
                binding.edLoginEmail.error = getString(R.string.email_required)
            }

            if (password.isEmpty()) {
                valid = false
                binding.edLoginPassword.error = getString(R.string.password_required)
            }

            if (valid) {
                loginViewModel.login(email, password)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}