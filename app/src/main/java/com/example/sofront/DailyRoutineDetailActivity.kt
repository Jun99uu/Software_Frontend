package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sofront.databinding.ActivityDailyRoutineDetailBinding

class DailyRoutineDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDailyRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}