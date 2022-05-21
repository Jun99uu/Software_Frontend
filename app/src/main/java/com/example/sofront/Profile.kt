package com.example.sofront

import android.provider.ContactsContract
import java.sql.Blob

data class Profile (
    val uid:String, //해당 프로필 주인 uid
    val profileImg: String, //프로필 사진
    val backgroundImg: String, //배경 이미지
    val name:String, //해당 프로필 주인 이름
    val subTitle:String, //해당 프로필 들어갔을 떄 문구
    val subscribeNum:Int //구독자 수
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
    val subtitle: String,
)

data class subscribeProfile(
    var hostUid:String,
    var myUid:String
)