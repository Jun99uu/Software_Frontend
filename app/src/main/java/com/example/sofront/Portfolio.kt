package com.example.sofront

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Blob

data class Portfolio(
    @SerializedName("postN")
    var id:Int,
    var title:String, //포폴 제목
    var portfolioWriter:String, //포폴 작성자
    var portfolioWriterName:String,
    var liked : Boolean,
    var content:String, //포폴 내용
    var date:String, //yyyy-mm-dd-hh-mm
    @SerializedName("commentN")
    var commentNum : Int,
    @SerializedName("likeN")
    var likeNum : Int,
    var portfolioWriterProfile:String, //포포 작성자 프사
    var contentImage:String //포폴 내용 안 사진
) : Serializable

data class SendPortfolio(
    var portfolioWriter:String,
    var title:String,
    var content:String,
    var file:String //이미지
) : Serializable