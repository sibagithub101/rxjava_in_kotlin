package com.siba.rxjavademo.paging

import com.google.gson.annotations.SerializedName

data class Wallet1ReqModel(

    @field:SerializedName("$1")
    val jsonMember1: String? = null,

    @field:SerializedName("$2")
    val jsonMember2: String? = null,


    @field:SerializedName("$4")
    val jsonMember4: String? = null,

    @field:SerializedName("$5")
    val jsonMember5: String? = null,

    @field:SerializedName("$7")
    val jsonMember7: ArrayList<String>? = null,

    )

data class Wallet1RespModel(

    @field:SerializedName("Report")
    val report: List<WalletReportItem>? = null,

    @field:SerializedName("length")
    val length: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class WalletReportItem(

    @field:SerializedName("relationalAmount")
    val relationalAmount: Any? = null,

    @field:SerializedName("currentBalance")
    val currentBalance: String? = null,

    @field:SerializedName("updatedDate")
    val updatedDate: Long? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("distributerName")
    val distributerName: Any? = null,

    @field:SerializedName("previousBalance")
    val previousBalance: String? = null,

    @field:SerializedName("CrAmount")
    val crAmount: Double? = null,

    @field:SerializedName("DrAmount")
    val drAmount: Double? = null,

    @field:SerializedName("createdDate")
    val createdDate: Long? = null,

    @field:SerializedName("relationalOperation")
    val relationalOperation: String? = null,

    @field:SerializedName("Id")
    val id: String? = null,

    @field:SerializedName("relationalId")
    val relationalId: String? = null,

    @field:SerializedName("refundId")
    val refundId: Any? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("masterName")
    val masterName: Any? = null
)
