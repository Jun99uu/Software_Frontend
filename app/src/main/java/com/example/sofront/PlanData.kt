package com.example.sofront

data class PlanData( //이게 Plan.kt의 Routine
    var isExpanded: Boolean = false,
    var planInfoList: ArrayList<PlanWorkout>
)

data class PlanWorkout(
    var name:String,
    var setNum:Int, //서버로는 전달안해도 되는 데이터. 뷰에서 표시할때만 씀
    var set:ArrayList<PlanSet>
    )

data class PlanSet(
    var count:Int,
    var weight:Int
)