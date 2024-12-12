package com.c242ps188.mentally_app.data.remote.response

import com.google.gson.annotations.SerializedName

data class Message(
    @field:SerializedName("msg")
    val msg: String? = null
)
