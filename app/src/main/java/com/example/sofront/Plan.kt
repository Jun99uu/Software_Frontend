package com.example.sofront

data class Plan(
    val plan:List<Routine>
)
data class Routine(
    val routine:List<Workout>
)
data class Workout(
    val type:WorkoutType,
    val set:Int,
    val count:Int,
    val weight:Int
)
data class WorkoutType(
    val name:String
)