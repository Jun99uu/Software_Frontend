package com.prolificinteractive.materialcalendarview;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.List;

@Entity
public class CalendarEntity {
    private String planName;
    @PrimaryKey
    @NonNull
    private String planDay;
    private int planLength;
    private int count;
    private String planId;
    private int workoutCount;
    public String getPlanId(){ return planId;}
    public void setPlanId(String planId){
        this.planId = planId;
    }
    public String getPlanName(){
        return planName;
    }
    @NonNull
    public String getPlanDay(){
        return planDay;
    }
    public int getPlanLength(){
        return planLength;
    }
    public int getCount(){
        return count;
    }
    public void setPlanName(String planName){
        this.planName = planName;
    }
    public void setPlanDay(@NonNull String planDay){
        this.planDay = planDay;
    }
    public void setPlanLength(int length){
        planLength = length;
    }
    public void setCount(int count){
        this.count = count;
    }
    public int getWorkoutCount(){ return workoutCount; }
    public void setWorkoutCount(int workoutCount){ this.workoutCount = workoutCount; }
    public CalendarEntity(String planName, @NonNull String planDay, int planLength, int count, String planId, int workoutCount){
        this.planName = planName;
        this.planDay = planDay;
        this.planLength = planLength;
        this.count = count;
        this.planId = planId;
        this.workoutCount = workoutCount;
    }
}


