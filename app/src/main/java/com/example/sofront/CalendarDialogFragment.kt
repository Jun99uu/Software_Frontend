package com.example.sofront

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentCalendarDialogBinding
import com.example.sofront.databinding.FragmentCalendarPlanBottomSheetListDialogBinding
import com.prolificinteractive.materialcalendarview.CalendarDatabase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarEntity
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarDialogFragment(private val date : CalendarDay,private val planEntity: CalendarEntity) : DialogFragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCalendarDialogBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarDialogBinding.inflate(inflater,container,false)
        val recyclerView = binding.calendarDialogRv
        val titleText = date.month.toString() + "월 " + date.day.toString() + "일의 운동"
        binding.title.text = titleText
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val adapter = CalendarDialogRecyclerViewAdapter()
        val db = CalendarDatabase.getInstance(requireContext())
        val dao = db.calendarDao()
        var clickTime = 0L
        binding.calendarDeleteBtn.setOnClickListener {

            if(System.currentTimeMillis() - clickTime < 3000) {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.deletePlanByPlanId(planEntity.planId)
                    CoroutineScope(Dispatchers.Main).launch {
                        val parentFragment = requireParentFragment()
                        parentFragment.parentFragmentManager.beginTransaction().replace(R.id.frame_layout,ListFragment()).commit()
                        dialog?.dismiss()
                    }
                }
            }
            else{
                Toast.makeText(requireContext(),"한번 더 누르면 플랜 전체가 삭제",Toast.LENGTH_LONG).show()
            }
            clickTime = System.currentTimeMillis()
        }

        CoroutineScope(Dispatchers.Main).launch {
            val plan = CoroutineScope(Dispatchers.IO).async {
                RetrofitService._getPlanByPlanName(planEntity.planName)
            }.await()

            if(plan.routineList.size>0)
            for(workout in plan.routineList[planEntity.count].workoutList)
                adapter.addItem(workout)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        //테스트용
        val workoutList = ArrayList<Workout>()
        workoutList.add(Workout("운동이름1",1,ArrayList<Set>()))
        workoutList.add(Workout("운동이름2",11,ArrayList<Set>()))
        workoutList.add(Workout("운동이름3",21,ArrayList<Set>()))

        val routine = Routine(true,workoutList)
        //테스트용 끝
        Log.d("size of workoutList",workoutList.size.toString())
        for(workout in routine.workoutList)
        adapter.addItem(workout)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("CalendarDialogFragment","onResume")
    }
}