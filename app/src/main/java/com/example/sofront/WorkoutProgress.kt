package com.example.sofront

import android.app.Application

class WorkoutProgress : Application() {
    private var setCount = 0
    private var workoutCount = 0
    fun getSetCount() : Int{
        return setCount
    }
    fun plusSetCount(){
        this.setCount++
    }
    fun getWorkoutCount() : Int{
        return workoutCount
    }
    fun plusWorkoutCount(){
        this.workoutCount++
    }
    fun resetSetCount(){
        this.setCount = -1
    }
}