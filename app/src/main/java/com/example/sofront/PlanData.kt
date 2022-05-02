package com.example.sofront

data class PlanData( //이게 Plan.kt의 Routine
    var isExpanded: Boolean = false,
    var planInfoList: ArrayList<PlanWorkout>
)

data class PlanWorkout(
    var name:String,
    var set:ArrayList<PlanSet>
    )

data class PlanSet(
    val count:Int,
    val weight:Int
)