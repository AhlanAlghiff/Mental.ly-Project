package com.c242ps188.mentally_app.ui.view.detailnews

import com.C242PS188.mentally_app.R
import com.c242ps188.mentally_app.data.local.model.News

object ListNews {
    val list = listOf(
        News(
            1,
            R.string.world_mental_health_day,
            R.drawable.detail_news_example,
            R.string.mental_health_day,
            "https://www.kompas.tv/tag/hari-kesehatan"
        ),
        News(
            2,
            R.string.our_best_mental_health_tips_backed_by_research,
            R.drawable.mental_heatl_tips,
            R.string.mental_health_tips,
            "https://www.mentalhealth.org.uk/explore-mental-health/publications/our-best-mental-health-tips"
        ),
        News(
            3,
            R.string.mental_health_disorders,
            R.drawable.mental_health_disorder,
            R.string.mental_disorders,
            "https://www.who.int/news-room/fact-sheets/detail/mental-disorders"
        )
    )
}