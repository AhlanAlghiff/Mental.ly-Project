package com.c242ps188.mentally_app.ui.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivitySettingsBinding
import com.c242ps188.mentally_app.ui.view.login.LoginActivity
import com.c242ps188.mentally_app.ui.viewmodel.SettingsViewModel
import com.c242ps188.mentally_app.ui.viewmodel.ViewModelFactory

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val settingsViewModel: SettingsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observe()
        setListener()
    }

    private fun observe() {
        settingsViewModel.getTheme.observe(this) { isDarkMode ->
            binding.switchDarkMode.setOnCheckedChangeListener(null)
            binding.switchDarkMode.isChecked = isDarkMode
            binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                updateTheme(isChecked)
            }
        }
    }

    private fun setListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            settingsViewModel.clearAllData()
            startActivity(intent)
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            updateTheme(isChecked)
        }
    }

    private fun updateTheme(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        settingsViewModel.setTheme(isChecked)
    }
}