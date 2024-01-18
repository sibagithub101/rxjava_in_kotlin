package com.siba.rxjavademo.rxjava

import com.google.gson.annotations.SerializedName

data class FetchReqest(

	@field:SerializedName("bank_code")
	val bankCode: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null
)
