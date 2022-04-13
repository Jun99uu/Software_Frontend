package com.example.sofront

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("user_UID")
    var UID: String = "",
    @SerializedName("user_name")
    var name: String = "",
    @SerializedName("user_age")
    var age:String = "",
    @SerializedName("user_level")
    var level:String = "",
    @SerializedName("user_purpose")
    var purpose:String ="",
    @SerializedName("user_type")
    var type:String="",
    @SerializedName("user_time")
    var time:String="",
    @SerializedName("user_number")
    var number:String=""
)
