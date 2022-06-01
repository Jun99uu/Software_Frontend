package com.example.sofront

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Plan(
    var planName:String,
    var hashTagList:ArrayList<String>,
    var routineList:ArrayList<Routine>, //이게 뷰의 근원
    @SerializedName("UID")
    var makerUid:String,
    var liked:Boolean,
    var likeNum:Int,
    var commentNum: Int,
    var downLoadNum: Int
) : Serializable

data class Routine(
    var isExpanded: Boolean, //토글 애니메이션용 변수
    var workoutList:ArrayList<Workout>
) : Serializable

data class Workout(
    var workoutName:String,
    var setNum:Int,
    var setList:ArrayList<Set>
) : Serializable

data class Set(
    var count:Int,
    var weight:Int
) : Serializable

data class planDownload(
    var planName:String,
    var downloaderUid:String,
)

data class PlanLike(
    var UID:String,
    var planName:String
)

data class Comment(
    @SerializedName("postN")
    var planName:String, //플랜 이름
    @SerializedName("commentWriter")
    var writerUid:String, //댓글 작성자 uid
    @SerializedName("commentWriterName")
    var writerName:String, //댓글 작성자 이름
    var commentDate:String, // yyyy-mm-dd-hh-mm
    @SerializedName("commentWriterProfile")
    var writerProfileImg:String, //댓글 작성자 프사
    @SerializedName("comContent")
    var commentContent:String, //댓글 내용
    @SerializedName("commentN")
    var commentN:String
) : Serializable


data class summaryPlan(
    var planName:String,
    var hashTagList:ArrayList<String>,
    @SerializedName("UID")
    var makerUid:String,
    var liked:Boolean,
    var likeNum:Int,
    var commentNum: Int,
    var downLoadNum: Int,
    var planDay: Int
): Serializable

data class planComment(
    var comment:String,
    var UID:String,
    var planName:String
)