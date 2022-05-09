package com.example.sofront

data class Plan(
    var planName:String,
    var hashTagList:ArrayList<String>,
    val routineList:ArrayList<Routine> //이게 뷰의 근원
)

data class Routine(
    var isExpanded: Boolean,
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