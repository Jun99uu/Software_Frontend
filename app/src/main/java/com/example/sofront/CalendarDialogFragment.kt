package com.example.sofront

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentCalendarDialogBinding
import com.example.sofront.databinding.FragmentCalendarPlanBottomSheetListDialogBinding
import com.prolificinteractive.materialcalendarview.CalendarDay

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarDialogFragment(val date : CalendarDay) : DialogFragment(){
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
//        val adapter =  RoutineRecyclerViewAdapter()
        val adapter = CalendarDialogRecyclerViewAdapter()

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarDialogFragment.
         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            CalendarDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}