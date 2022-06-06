package com.example.sofront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityDailyRoutineDetailBinding
import com.prolificinteractive.materialcalendarview.CalendarDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyRoutineDetailActivity : AppCompatActivity() {
    val routineDetailAdapter = DailyRoutineDetailAdapter()
    var count = 0;
    var nowSet = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDailyRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val workout = intent.getSerializableExtra("workout") as Workout
        for(set in workout.setList){
            routineDetailAdapter.addItem(set)
        }
        val rv = binding.routineDetailRc
        val title = binding.workoutName
        title.text = workout.workoutName
        val startButton = binding.routineStartBtn
        startButton.setOnClickListener{
            if(nowSet==workout.setNum-1){
                CoroutineScope(Dispatchers.IO).launch{
                    val instance = CalendarDatabase.getInstance(applicationContext)
                    val dao = instance.calendarDao()
                    (application as WorkoutProgress).plusWorkoutCount()
                    dao.updateWorkoutCount((application as WorkoutProgress).getWorkoutCount())
                    finish()
                }

            }
            val intent = Intent(this,DailyRoutinePlayActivity::class.java)
            intent.putExtra("workoutName",workout.workoutName)
//            intent.putExtra("currCount",workout.setList[nowSet].count)
//            intent.putExtra("currWeight",workout.setList[nowSet].weight)
            intent.putExtra("set",workout.setList[nowSet])
            startActivity(intent)
        }
        rv.adapter = routineDetailAdapter
        rv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }
    override fun onResume(){
        super.onResume()
        Log.d("DailyRoutineDetailActivity","onResume")
        nowSet = (application as WorkoutProgress).getSetCount()
        Log.d("DailyRoutineDetailActivity",count.toString())
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("DailyRoutineDetailActivity","onActivityResult")
//        if(requestCode == 101){
//            if(resultCode == RESULT_OK){
//                count++
//            }
//        }
//    }
}