package com.example.sofront

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentDailyRoutineBinding
import com.prolificinteractive.materialcalendarview.CalendarDatabase

class DailyRoutineFragment : Fragment() {
    lateinit var adapter : DailyRoutineAdater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DailyRoutineAdater(this.requireContext())
        val todayRoutine = arguments?.getSerializable("routine") as Routine
        for(workout in todayRoutine.workoutList){
            adapter.addItem(workout)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDailyRoutineBinding.inflate(layoutInflater)

        val quotes = Quotes()
        val quoLen = quotes.title.size-1
        val range = (0..quoLen).random()
        val workoutRecyclerView = binding.dailyRc

        binding.title.text = quotes.title[range]
        for(i in 0..2){
            when(i){
                0 -> binding.firstUpperBtn.text = quotes.hashtag[range][i]
                1 -> binding.secondUpperBtn.text = quotes.hashtag[range][i]
                2 -> binding.thirdUpperBtn.text = quotes.hashtag[range][i]
            }
        }

        var firstUpper = false
        var secondUpper = false
        var thirdUpper = false

        binding.firstUpperBtn.setOnClickListener{
            if(firstUpper){
                binding.firstUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_no_select_btn)
                firstUpper = false
            }else{
                binding.firstUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_yes_select_btn)
                firstUpper = true
            }
        }
        binding.secondUpperBtn.setOnClickListener{
            if(secondUpper){
                binding.secondUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_yes_select_btn)
                secondUpper = false
            }else{
                binding.secondUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_no_select_btn)
                secondUpper = true
            }
        }
        binding.thirdUpperBtn.setOnClickListener{
            if(thirdUpper){
                binding.thirdUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_yes_select_btn)
                thirdUpper = false
            }else{
                binding.thirdUpperBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_no_select_btn)
                thirdUpper = true
            }
        }

        binding.hamburger.setOnClickListener{
            val intent = Intent(requireContext(), SettingActivity::class.java)
            this.startActivity(intent)
        }


        workoutRecyclerView.adapter = adapter
        workoutRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        return binding.root
    }

    private class Quotes {
        var title = arrayOf("헬스장에 가기만 하면\n반은 성공이다💪", "끈기있는 자만이\n득근할 수 있다🏋️‍♀️", "운동 할 생각에\n가슴이 득근두근🤩", "지금의 1RM이\n워밍업이 되도록🔥")
        var hashtag = arrayOf(arrayOf("#오운완", "#할수있다", "#가보자고"), arrayOf("#끈기", "#열정", "#득근"), arrayOf("#기분이", "#설렘", "#득근두근"), arrayOf("#화이팅", "#1RM", "#워밍업"))
    }
}