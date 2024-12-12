package com.c242ps188.mentally_app.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiagnoseResponse(

	@field:SerializedName("risks_factors")
	val risksFactors: List<String?>? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("diagnosis")
	val diagnosis: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
