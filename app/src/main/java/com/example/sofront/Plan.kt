package com.example.sofront

data class Plan(
    val plan:List<Routine>
)
data class Routine(
    val routine:List<Workout>
)
data class Workout(
    val name:String,
    val set:List<Set>
)
data class Set(
    val count:Int,
    val weight:Int
)