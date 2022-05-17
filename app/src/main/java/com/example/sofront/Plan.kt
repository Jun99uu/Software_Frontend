package com.example.sofront

data class Plan(
    var planName:String,
    var hashTagList:ArrayList<String>,
    var routineList:ArrayList<Routine>, //이게 뷰의 근원
    var makerUid:String,
    var liked:Boolean,
    var likeNum:Int,
    var commentNum: Int,
    var downLoadNum: Int
)

data class Routine(
    var isExpanded: Boolean, //토글 애니메이션용 변수
    var workoutList:ArrayList<Workout>
)

data class Workout(
    var workoutName:String,
    var setNum:Int,
    var setList:ArrayList<Set>
)
data class Set(
    var count:Int,
    var weight:Int
)

data class planDownload(
    var planName:String,
    var downloaderUid:String,
)

data class planLike(
    var planName:String,
    var likerUid:String
)

data class planComment(
    var planName:String,
    var writerUid:String,
    var commentContent:String
)