package com.example.sofront

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    initCalendarDeco()
    CoroutineScope(Dispatchers.IO).launch{
        val db = CalendarDatabase.getInstance(requireContext())
        val calendarDao = db!!.calendarDao()
        val plan = TestFactory.getSomePlan()
//        calendarDao.deletePlan(CalendarEntity(plan.planName,calendarView.currentDate.toString()))
        calendarDao.deletePlan(CalendarEntity(plan.planName,""+calendarView.currentDate.year+"-"+calendarView.currentDate.month+"-"+calendarView.currentDate.day,plan.routineList.size))
        calendarDao.insertPlan(CalendarEntity(plan.planName,""+calendarView.currentDate.year+"-"+calendarView.currentDate.month+"-"+calendarView.currentDate.day,plan.routineList.size))
        calendarDao.deletePlan(CalendarEntity("하","2022-05-15",5))
        calendarDao.insertPlan(CalendarEntity("하","2022-05-15",5))
        val list = calendarDao.getAll()
        for(i in list){
            Log.d("캘린더 디비 저장 확인",i.planName + " "+i.planDay)
            decorateDay(i)
        }
        }

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

    private fun initCalendarDeco(){

    }
    fun decorateDay(entity: CalendarEntity){
        val set:HashSet<CalendarDay> = HashSet()
        val d = entity.planDay
        val parse = d.split("-")
        var calendarDay = CalendarDay.from(parse[0].toInt(),parse[1].toInt(),parse[2].toInt())
        for(i in 1..entity.planLength){
            set.add(calendarDay)
//            Log.d("for",d.toString())
            calendarDay = addOnetoCalendarDay(calendarDay)
        }
        Log.d("Set",set.toString())
        calendarView.addDecorator(EventDecorator(MonthView.colors[MonthView.colorIndex],set))
        MonthView.colorIndex = (MonthView.colorIndex + 1) % MonthView.colors.size
    }

    fun addOnetoCalendarDay(date: CalendarDay):CalendarDay{
        val days = arrayListOf<Int>(0,31,28,31,30,31,30,31,31,30,31,30,31)


        var day = date.day+1
        var month = date.month
        var year = date.year
        if( year % 4 == 0 && ( year % 100 !=0 || year % 400 == 0)){
            days[2] = 29
        }
        if(date.day >= days[date.month]){
            day = 1
            month = month+1
            if(month > 12){
                month = 1
                year = year+1
            }

        }
        return CalendarDay.from(year,month,day)
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
            val planList = TestFactory.getSomePlan(10)
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