package com.example.sofront

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("UID")
    var UID: String ="",
    @SerializedName("user_name")
    var name: String = "",
    @SerializedName("user_age")
    var age:Int = 0,
    @SerializedName("user_level")
    var level:String = "",
    @SerializedName("user_purpose")
    var purpose:String ="",
    @SerializedName("user_type")
    var type:String="",
    @SerializedName("workout_time")
    var time:Int=0,
    @SerializedName("workout_per_week")
    var number:Int = 0
)