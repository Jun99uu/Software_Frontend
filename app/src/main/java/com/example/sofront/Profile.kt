package com.example.sofront

import java.sql.Blob

data class Profile (
    val uid:String, //해당 프로필 주인 uid
    val profileImg: Blob, //프로필 사진
    val backgroundImg: Blob, //배경 이미지
    val name:String, //해당 프로필 주인 이름
    val subTitle:String, //해당 프로필 들어갔을 떄 문구
    val subscribeNum:Int //구독자 수
)

data class Subscribe(
    val hostUid:String, //해당 프로필 주인 uid
    val subscriberUid:ArrayList<String> //해당 프로필 구독자 uid 리스트
)