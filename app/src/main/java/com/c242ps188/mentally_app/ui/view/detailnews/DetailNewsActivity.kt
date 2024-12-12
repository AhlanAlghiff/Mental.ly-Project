package com.c242ps188.mentally_app.ui.view.detailnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityDetailNewsBinding

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val title = intent.getIntExtra(EXTRA_TITLE, 0)
        val image = intent.getIntExtra(EXTRA_IMAGE, 0)
        val desc = intent.getIntExtra(EXTRA_DESC, 0)
        val url = intent.getStringExtra(EXTRA_URL)

        binding.tvNewsHeader.setText(title)
        binding.ivNews.setImageResource(image)
        binding.tvNewsDesc.setText(desc)
        binding.viewMore.setOnClickListener {
            binding.viewMore.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        setListener()
    }

    private fun setListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object{
        const val EXTRA_ID = "id"
        const val EXTRA_IMAGE = "image"
        const val EXTRA_TITLE = "title"
        const val EXTRA_DESC = "desc"
        const val EXTRA_URL = "url"
    }
}