package com.mionix.baseapp.model

import com.google.gson.annotations.SerializedName

data class LovePercentModel(
    @SerializedName("fname")
    val fname: String,
    @SerializedName("sname")
    val sname: String,
    @SerializedName("percentage")
    val percentage: Int,
    @SerializedName("result")
    val result: String
)