package com.prolificinteractive.materialcalendarview;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalendarDao{
    @Query("SELECT * FROM calendarentity")
    List<CalendarEntity> getAll();
    @Insert
    void insertPlan(CalendarEntity calendarEntity);
    @Delete
    void deletePlan(CalendarEntity calendarEntity);
    @Query("DELETE FROM calendarentity")
    void deleteAll();
    @Query("SELECT distinct planName From calendarentity ")
    List<String> getPlanName();
    @Query("SELECT * FROM calendarentity WHERE planName = :name")
    List<CalendarEntity> getEntityByName(String name);
//    @Query("SELECT * FROM calendarentity WHERE plan")
    @Query("SELECT * From calendarentity WHERE planDay = :planDay")
    CalendarEntity getPlanByDay(String planDay);
    @Query("DELETE FROM CalendarEntity WHERE planId = :planId")
    void deletePlanByPlanId(String planId);
}