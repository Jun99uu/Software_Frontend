package com.example.sofront

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentDailyRoutineBinding
import com.prolificinteractive.materialcalendarview.CalendarDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyRoutineFragment : Fragment() {
    lateinit var adapter : DailyRoutineAdater
    lateinit var todayRoutine : Routine
    lateinit var progressBar : ProgressBar
    lateinit var text : TextView
    lateinit var percent : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DailyRoutineAdater(this.requireContext())
        todayRoutine = arguments?.getSerializable("routine") as Routine
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
        progressBar = binding.allProgress
        text = binding.helpQuote
        percent = binding.percentNum


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

        val clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.metaBtn.setOnClickListener{
            val metaPwd = "9986"
            val clip = ClipData.newPlainText("label", metaPwd)
            clipboard.setPrimaryClip(clip)
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://link.ifland.ai/tNCX"))
            Toast.makeText(requireContext(), "메타버스 입장 비밀번호가 클립보드에 복사되었습니다!", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }

        return binding.root
    }

    private class Quotes {
        var title = arrayOf("헬스장에 가기만 하면\n반은 성공이다💪", "끈기있는 자만이\n득근할 수 있다🏋️‍♀️", "운동 할 생각에\n가슴이 득근두근🤩", "지금의 1RM이\n워밍업이 되도록🔥")
        var hashtag = arrayOf(arrayOf("#오운완", "#할수있다", "#가보자고"), arrayOf("#끈기", "#열정", "#득근"), arrayOf("#기분이", "#설렘", "#득근두근"), arrayOf("#화이팅", "#1RM", "#워밍업"))
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()

        progressBar.max = todayRoutine.workoutList.size
        CoroutineScope(Dispatchers.IO).launch {
            val instance = CalendarDatabase.getInstance(context)
            val dao = instance.calendarDao()
            progressBar.progress = dao.getWorkoutCount()
            if(progressBar.progress==progressBar.max){
                text.text = "잘했다!!!!"
                Toast.makeText(requireContext(), "끝났다!!!!!!!",Toast.LENGTH_LONG).show()
            }
            percent.text = ((progressBar.progress.toDouble() / progressBar.max.toDouble())*100).toString()
        }


    }
}