package com.example.sofront

import android.content.Context
import androidx.room.*
import com.prolificinteractive.materialcalendarview.CalendarDay
//
@Entity(primaryKeys = ["startYear","startMonth","startDay"])
data class CalendarEntity(
    var planName : String = "",
    var planLength : Int = 0,
    var startYear:Int = 1900,
    var startMonth:Int = 1,
    var startDay:Int = 1,
)

@Dao
interface CalendarDao{
    @Query("SELECT * FROM calendarentity")
    fun getAll() : List<CalendarEntity>
    @Insert
    fun insertPlan(vararg calendarEntity: CalendarEntity)
    @Delete
    fun deletePlan(calendar : CalendarEntity)
}

@Database(entities = [CalendarEntity::class],version = 3)
abstract class CalendarDatabase : RoomDatabase(){
    abstract fun calendarDao() : CalendarDao
    companion object{
        private var instance : CalendarDatabase? = null
        fun getInstance(context: Context) : CalendarDatabase?{
            if(instance==null){
                instance = Room.databaseBuilder(context, CalendarDatabase::class.java,"calendar-database").fallbackToDestructiveMigration().build()
            }
            return instance
        }
    }
}