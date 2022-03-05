package com.example.sofront

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("user_name")
    var user_name: String = "",
    @SerializedName("user_age")
    var user_age:String = "",
    @SerializedName("user_level")
    var user_level:String = "",
    @SerializedName("user_purpose")
    var user_purpose:String ="",
    @SerializedName("user_type")
    var user_type:String="",
    @SerializedName("user_time")
    var user_time:String="",
    @SerializedName("user_number")
    var user_number:String=""
)
