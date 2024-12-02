package com.c242ps188.mentally_app.ui.view.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityHomeBinding
import com.c242ps188.mentally_app.ui.view.login.LoginActivity
import com.c242ps188.mentally_app.ui.view.login.RegisterActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var bindig: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindig = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindig.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setListener()
    }

    private fun setListener() {
        bindig.settings.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}