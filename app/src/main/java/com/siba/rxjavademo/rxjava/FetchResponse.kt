package com.siba.rxjavademo.rxjava

import com.google.gson.annotations.SerializedName

data class FetchResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("ui_json")
	val uiJson: UiJson? = null,

	@field:SerializedName("version")
	val version: String? = null
)

data class APIs(

	@field:SerializedName("Commission2")
	val commission2: String? = null,

	@field:SerializedName("Commission1")
	val commission1: String? = null,

	@field:SerializedName("retailerBankFetch")
	val retailerBankFetch: String? = null,

	@field:SerializedName("matm2")
	val matm2: String? = null,

	@field:SerializedName("wallet1")
	val wallet1: String? = null,

	@field:SerializedName("wallettopup")
	val wallettopup: String? = null,

	@field:SerializedName("wallet2")
	val wallet2: String? = null,

	@field:SerializedName("walletcashout")
	val walletcashout: String? = null,

	@field:SerializedName("dmt")
	val dmt: String? = null,

	@field:SerializedName("emailReport")
	val emailReport: String? = null,

	@field:SerializedName("upi")
	val upi: String? = null,

	@field:SerializedName("updateWalletTopupTxn")
	val updateWalletTopupTxn: String? = null,

	@field:SerializedName("liveLong")
	val liveLong: String? = null,

	@field:SerializedName("recharge")
	val recharge: String? = null,

	@field:SerializedName("UnifiedAeps")
	val unifiedAeps: String? = null,

	@field:SerializedName("AadhaarPay")
	val aadhaarPay: String? = null,

	@field:SerializedName("walletInterchange")
	val walletInterchange: String? = null,

	@field:SerializedName("bbpsreport")
	val bbpsreport: String? = null,

	@field:SerializedName("update_wallettopup_request")
	val updateWallettopupRequest: String? = null
)

data class UiJson(

	@field:SerializedName("APIs")
	val aPIs: APIs? = null,

	@field:SerializedName("primaryColor")
	val primaryColor: String? = null
)
