package com.example.sofront

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.*
import kotlinx.coroutines.*

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview:RecyclerView
    private lateinit var calendarView: MaterialCalendarView
//    var planLength =0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        val addPlanBtn = binding.addPlanBtn
        recyclerview = binding.planRv
        calendarView = binding.calendar
        MonthView.materialCalendarView = calendarView
//        val bottomSheet = CalendarPlanBottomSheet.newInstance(1)
//        bottomSheet.show(requireActivity().supportFragmentManager, "CalendarPlanBottomSheet")


        setRecyclerView()
        setCalendarView()
        addPlanBtn.setOnClickListener {
            callSetPlanActivity()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun setRecyclerView(){
        //TODO: 서버에서 플랜을 가져와서 리사이클러뷰로 띄워줌
        val adapter = PlanRecyclerViewAdapter()
        initRecyclerViewList(adapter)
        setRecyclerViewAdapter(adapter)
    }
    private fun initRecyclerViewList(adapter:PlanRecyclerViewAdapter){
        val auth = FirebaseAuth.getInstance()
        if(auth.uid == null){
            val planList = ArrayList<Plan>()

            val tmpHash = ArrayList<String>()
            tmpHash.add("안녕")
            tmpHash.add("그래")
            tmpHash.add("잘가")
            tmpHash.add("하하")
            tmpHash.add("깃허브")
            val routineList = ArrayList<Routine>()
            val initSet = ArrayList<Set>()
            initSet.add(Set(0,0))
            val initWorkout = ArrayList<Workout>()
            initWorkout.add(Workout("", 0, initSet))
            routineList.add(Routine(false, initWorkout))
            routineList.add(Routine(false,initWorkout))
            routineList.add(Routine(false,initWorkout))
            val tmpPlan = Plan("하이", tmpHash, routineList, "사람uid", false, 0,0 ,0)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            planList.add(tmpPlan)
            for(plan in planList){
                adapter.addItem(plan)
            }
        }

        CoroutineScope(Dispatchers.IO).launch{
            val planArray = RetrofitService._getPlanByUid("류승민")
            Log.d("plan Array size","${planArray.size}")
            for(plan in planArray) {
                Log.d("Plan Class", plan.toString())
                adapter.addItem(plan)
            }
        }
    }
    private fun setRecyclerViewAdapter(adapter: PlanRecyclerViewAdapter){
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    private fun setCalendarView(){
        calendarView.setOnDateChangedListener { _, date, selected ->
            Log.d("Changed",date.toString()+selected)
            val calendarDialogFragment = CalendarDialogFragment(date)
            calendarDialogFragment.show(childFragmentManager,"CalendarDialogFragment")
        }
        calendarView.setWeekDayTextAppearance(R.font.nixgonm)
        calendarView.setDateTextAppearance(R.font.nixgonm)
        calendarView.setHeaderTextAppearance(R.font.nixgonm)
        calendarView.setTileHeightDp(40)
    }

    private fun callSetPlanActivity(){
        //TODO: plan을 생성하는 액티비티를 호출하고 플랜을 받아옴
//        addPlanList()
    }
}