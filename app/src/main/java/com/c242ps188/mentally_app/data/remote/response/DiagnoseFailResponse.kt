package com.c242ps188.mentally_app.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiagnoseFailResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
