package com.prolificinteractive.materialcalendarview;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CalendarEntity.class}, version = 6,exportSchema = false)
public abstract class CalendarDatabase extends RoomDatabase {
    private static CalendarDatabase instance;
    private static String DATABASE_NAME = "database";
    public synchronized static CalendarDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,CalendarDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract CalendarDao calendarDao();
}
