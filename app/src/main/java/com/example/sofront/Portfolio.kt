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
//    var portfolioWriterProfile:Blob, //포포 작성자 프사
    var content:String, //포폴 내용
//    var contentImage:ArrayList<Blob>, //포폴 내용 안 사진(최대 5장)
    var date:String, //yyyy-mm-dd-hh-mm
    var hashTagList:ArrayList<String>, //해쉬태그리스트 (최대 5개)
    @SerializedName("commentN")
    var commentNum : Int,
    @SerializedName("likeN")
    var likeNum : Int
) : Serializable