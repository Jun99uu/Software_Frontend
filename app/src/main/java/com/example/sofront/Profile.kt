package com.example.sofront

import com.google.gson.annotations.SerializedName

data class Profile (
    val UID:String, //해당 프로필 주인 uid
    val profileImg: String, //프로필 사진
    val backgroundImg: String, //배경 이미지
    val name:String, //해당 프로필 주인 이름
    val subTitle:String, //해당 프로필 들어갔을 떄 문구
    val subscribeNum:Int, //구독자 수
    val subscribed: Boolean  //구독 했는지 안했는지
)

data class Subscribe(
    val hostUid:String, //해당 프로필 주인 uid
    val subscriberUid:ArrayList<String> //해당 프로필 구독자 uid 리스트
)

data class editProfile (
    val uid:String,
    val profileImg: String,
    val backgroundImg: String,
    val name: String,
    val subTitle: String,
)

data class subscribeProfile(
    @SerializedName("pro_user")
    var hostUid:String,
    @SerializedName("ss_user")
    var myUid:String
)

data class certainProfile(
    var name:String,
    var profileImg: String
)