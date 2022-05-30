package com.example.sofront

import com.google.gson.annotations.SerializedName

data class PortfolioLike (
    @SerializedName("postN")
    val postN : String,
    @SerializedName("like_user")
    val userId : String
    )