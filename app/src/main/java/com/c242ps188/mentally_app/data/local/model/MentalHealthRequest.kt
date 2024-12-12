package com.c242ps188.mentally_app.data.local.model

data class MentalHealthRequest(
    val user_id: String,
    val data: MentalHealthData
)

data class MentalHealthData(
    val age: Float? = null,
    val gender: Int? = null,
    val work_pressure: Float? = null,
    val job_satisfaction: Float? = null,
    val sleep_duration: Float? = null,
    val dietary_habits: Float? = null,
    val have_you_ever_had_suicidal_thoughts: Float? = null,
    val work_hours: Float? = null,
    val financial_stress: Float? = null,
    val family_history_of_mental_illness: Float? = null
)
