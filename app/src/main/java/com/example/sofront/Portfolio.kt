package com.example.sofront

import java.io.Serializable
import java.sql.Blob

data class Portfolio(
    var title:String, //포폴 제목
    var portfolioWriter:String, //포폴 작성자
//    var portfolioWriterProfile:Blob, //포포 작성자 프사
    var content:String, //포폴 내용
//    var contentImage:ArrayList<Blob>, //포폴 내용 안 사진(최대 5장)
    var date:String, //yyyy-mm-dd-hh-mm
    var hashTagList:ArrayList<String>, //해쉬태그리스트 (최대 5개)
    var commentNum : Int
) : Serializable

data class portfolioComment(
    var commentWriter:String, //댓글 작성자
    var commenterUid:String, //댓글 작성자 uid
    var commentWriterProfile:String, //댓글 작성자 프사
    var commentDate:String, //yyyy-mm-dd-hh-mm
    var comContent:String, //댓글 내용
) : Serializable