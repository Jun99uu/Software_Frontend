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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDatabase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarEntity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CalendarDialogFragment(private val date : CalendarDay,private val planEntity: CalendarEntity) : DialogFragment(){
    val user = Firebase.auth.currentUser
    val myUid = user!!.uid
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCalendarDialogBinding? = null
    val adapter = CalendarDialogRecyclerViewAdapter()


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
        getRoutineByPlanName(planEntity.planName)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("CalendarDialogFragment","onResume")
    }
    fun getRoutineByPlanName(planName : String){
        RetrofitService.retrofitService.getPlanByPlanName(planName, myUid).enqueue(object :
            Callback<Plan> {
            override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                if (response.isSuccessful) {
                    Log.d("getPlan test", "success")
                    Log.d("getPlan test success", response.body().toString())
                    for(workout in response.body()!!.routineList[planEntity.count].workoutList)
                    adapter.addItem(workout)
                    for(i in 0..adapter.itemCount)
                    adapter.notifyItemInserted(i)
                } else {
                    Log.d("getPlan test", "success but something error")
                }
            }

            override fun onFailure(call: Call<Plan>, t: Throwable) {
                Log.d("getPlan test", "fail")
            }
        })
    }
}