package com.example.sofront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityDailyRoutineDetailBinding

class DailyRoutineDetailActivity : AppCompatActivity() {
    val routineDetailAdapter = DailyRoutineDetailAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDailyRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val workout = intent.getSerializableExtra("workout") as Workout
        val nowSet = intent.getIntExtra("nowSet",0)
        for(set in workout.setList){
            routineDetailAdapter.addItem(set)
        }
        val rv = binding.routineDetailRc
        val title = binding.workoutName
        title.text = workout.workoutName
        val startButton = binding.routineStartBtn
        startButton.setOnClickListener{
            val intent = Intent(this,DailyRoutinePlayActivity::class.java)
            intent.putExtra("workoutName",workout.workoutName)
            intent.putExtra("totalCount",workout.setList[0].count)
            intent.putExtra("set",workout.setList[nowSet])
            startActivity(intent)
        }
        rv.adapter = routineDetailAdapter
        rv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }
}