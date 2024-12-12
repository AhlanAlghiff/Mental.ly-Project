package com.c242ps188.mentally_app.ui.view.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityHomeBinding
import com.c242ps188.mentally_app.ui.view.detailnews.DetailNewsActivity
import com.c242ps188.mentally_app.ui.view.detailnews.ListNews
import com.c242ps188.mentally_app.ui.view.diagnose.DiagnoseFeelingActivity
import com.c242ps188.mentally_app.ui.view.diagnose.DiagnoseSymptomsActivity
import com.c242ps188.mentally_app.ui.view.home.adapter.NewsAdapter
import com.c242ps188.mentally_app.ui.view.settings.SettingsActivity
import com.c242ps188.mentally_app.ui.viewmodel.SettingsViewModel
import com.c242ps188.mentally_app.ui.viewmodel.UsersViewModel
import com.c242ps188.mentally_app.ui.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var bindig: ActivityHomeBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val settingsViewModel: SettingsViewModel by viewModels { factory }
    private val usersViewModel: UsersViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindig = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindig.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observe()
        setListener()
        setAdapter()
    }

    private fun observe() {
        settingsViewModel.getTheme.observe(this){ isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        usersViewModel.getName.observe(this){
            bindig.tvName.text = it
        }
    }

    private fun setListener() {
        bindig.settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        bindig.cvDiagnoseFromSymptomps.setOnClickListener {
            val intent = Intent(this, DiagnoseSymptomsActivity::class.java)
            startActivity(intent)
        }

        bindig.cvShareYourFeelings.setOnClickListener {
            val intent = Intent(this, DiagnoseFeelingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAdapter(){
        val adapter = NewsAdapter { newsItem ->
            val intent = Intent(this, DetailNewsActivity::class.java)
            intent.putExtra(DetailNewsActivity.EXTRA_TITLE, newsItem.title)
            intent.putExtra(DetailNewsActivity.EXTRA_ID, newsItem.id)
            intent.putExtra(DetailNewsActivity.EXTRA_IMAGE, newsItem.photo)
            intent.putExtra(DetailNewsActivity.EXTRA_DESC, newsItem.description)
            intent.putExtra(DetailNewsActivity.EXTRA_URL, newsItem.url)

            startActivity(intent)
        }

        adapter.submitList(ListNews.list)
        bindig.rvArticle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bindig.rvArticle.adapter = adapter
    }
}