package com.example.sofront

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview:RecyclerView
    private lateinit var calendarView: MaterialCalendarView
    private val adapter = PlanRecyclerViewAdapter()
    val planArray  = ArrayList<Plan>()
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
        MonthView.context = requireContext()

//        val bottomSheet = CalendarPlanBottomSheet.newInstance(1)
//        bottomSheet.show(requireActivity().supportFragmentManager, "CalendarPlanBottomSheet")
        initCalendarDeco()

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
        CoroutineScope(Dispatchers.IO).launch{
            val db = CalendarDatabase.getInstance(requireContext())
            val calendarDao = db!!.calendarDao()
//            calendarDao.deleteAll()

//        calendarDao.deletePlan(CalendarEntity(plan.planName,calendarView.currentDate.toString()))
//            calendarDao.deletePlan(CalendarEntity(plan.planName,""+calendarView.currentDate.year+"-"+calendarView.currentDate.month+"-"+calendarView.currentDate.day,plan.routineList.size,1))
//            calendarDao.insertPlan(CalendarEntity(plan.planName,""+calendarView.currentDate.year+"-"+calendarView.currentDate.month+"-"+calendarView.currentDate.day,plan.routineList.size,2))
//            calendarDao.deletePlan(CalendarEntity("하","2022-05-15",5,1))
//            calendarDao.insertPlan(CalendarEntity("하","2022-05-15",5,2))
            val tmp = calendarDao.planName
            for(item in tmp) {
                val list = calendarDao.getEntityByName(item)
                for(i in list){
                    Log.d("캘린더 디비 저장 확인",i.planName + " "+i.planDay)
                    CoroutineScope(Dispatchers.Main).launch {
                        decorateDay(list)
                    }
                }
            }

        }
    }
    fun decorateDay(list : List<CalendarEntity>){
        val set = HashSet<CalendarDay>()
        for(item in list){
            val parser = item.planDay.split("-")
            val calendarDay = CalendarDay.from(parser[0].toInt(),parser[1].toInt(),parser[2].toInt())
            set.add(calendarDay)
        }
        calendarView.addDecorator(EventDecorator(MonthView.colors[MonthView.colorIndex],set))
        MonthView.colorIndex = (MonthView.colorIndex + 1) % MonthView.colors.size
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

        initRecyclerViewList(adapter)
        setRecyclerViewAdapter(adapter)
    }
    private fun setRecyclerViewAdapter(adapter: PlanRecyclerViewAdapter){
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this.context)
    }
    private fun initRecyclerViewList(adapter:PlanRecyclerViewAdapter){
        val auth = FirebaseAuth.getInstance()
        if(auth.uid == null){
            val planList = TestFactory.getSomePlan(10)
            for(plan in planList){
                adapter.addItem(plan)
            }
        }

////        CoroutineScope(Dispatchers.Main).launch{
//            runBlocking<Unit> {
//                getDownloadPlanByUid("류승민")
//                Log.d("planArraysize","${planArray.size}")
//                for(plan in planArray) {
//                    Log.d("Plan Class", plan.toString())
//                    adapter.addItem(plan)
//                }
////            }
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                Log.d("firebase uid",FirebaseAuth.getInstance().uid.toString())
                val planList = RetrofitService._getDownloadPlanUsingExecute(FirebaseAuth.getInstance().uid.toString())
                for(plan in planList){
                    adapter.addItem(plan)
                }
            }.await()
//            Log.d("ListFragment,다운로드 플랜",planList.size.toString())

        }


    }
//    fun getDownloadPlan(uid : String):PlanRecyclerViewAdapter{
//        RetrofitService.retrofitService.getDownloadPlan(uid).enqueue(object : Callback<ArrayList<Plan>> {
//            override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
//                if (response.isSuccessful) {
//                    Log.d("getDownLoadPlan test success", response.body().toString())
//                    Log.d("getDownLoadPlan test success", response.body()!!.size.toString())
//                    for(plan in response.body() as ArrayList<Plan>) {
//                        adapter.addItem(plan)
//                        adapter.notifyDataSetChanged()
//                    }
//                } else {
//                    Log.d("getDownLoadPlan test", "success but something error")
//                }
//                Log.d("getDownloadPlan code",response.code().toString())
//            }
//
//            override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
//                Log.d("getDownLoadPlan test", "fail")
//                Log.d("getDownloadPlan error code",t.message.toString())
//            }
//        })
//        return adapter
//    }


    private fun setCalendarView(){
        calendarView.setOnDateChangedListener { _, date, selected ->
            val db = CalendarDatabase.getInstance(requireContext())
            val dao = db.calendarDao()
            CoroutineScope(Dispatchers.Main).launch {

                val plan = CoroutineScope(Dispatchers.IO).async {
                    dao.getPlanByDay(date.year.toString() + "-" + date.month.toString() + "-" + date.day)
                }.await()

//                Toast.makeText(requireContext(),plan.planName + ", "+plan.count,Toast.LENGTH_SHORT).show()
                if(plan!=null){
                    val calendarDialogFragment = CalendarDialogFragment(date,plan.planName,plan.count)
                    calendarDialogFragment.show(childFragmentManager,"CalendarDialogFragment")
                }
            }
        }
        calendarView.setWeekDayTextAppearance(R.font.nixgonm)
        calendarView.setDateTextAppearance(R.font.nixgonm)
        calendarView.setHeaderTextAppearance(R.font.nixgonm)
        calendarView.setTileHeightDp(40)
    }

    private fun callSetPlanActivity(){
        //TODO: plan을 생성하는 액티비티를 호출하고 플랜을 받아옴
        val intent = Intent(requireContext(),MakePlanActivity::class.java)
        startActivity(intent)
    }
}