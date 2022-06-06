package com.example.sofront

import android.content.Context
import android.content.Intent
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
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        MonthView.context = requireContext()

        initCalendarDeco()


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

    override fun onResume() {
        super.onResume()
        setRecyclerView()
        Log.d("ListFragment","onResume")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("ListFragment","onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("ListFragment","detached")
    }

    private fun initCalendarDeco(){
        CoroutineScope(Dispatchers.IO).launch{
            val db = CalendarDatabase.getInstance(requireContext())
            val calendarDao = db!!.calendarDao()
//            calendarDao.deleteAll()
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
    private fun decorateDay(list : List<CalendarEntity>){
        val set = HashSet<CalendarDay>()
        for(item in list){
            val parser = item.planDay.split("-")
            val calendarDay = CalendarDay.from(parser[0].toInt(),parser[1].toInt(),parser[2].toInt())
            set.add(calendarDay)
        }
        calendarView.addDecorator(EventDecorator(MonthView.colors[MonthView.colorIndex],set))
        MonthView.colorIndex = (MonthView.colorIndex + 1) % MonthView.colors.size
    }

    private fun setRecyclerView(){
        //TODO: 서버에서 플랜을 가져와서 리사이클러뷰로 띄워줌
        val adapter = PlanRecyclerViewAdapter()
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
        else{
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("firebase uid",FirebaseAuth.getInstance().uid.toString())
//                val planList = RetrofitService._getDownloadPlanUsingExecute(FirebaseAuth.getInstance().uid.toString())
//                for(plan in planList){
//                    adapter.addItem(plan)
//                }
                    RetrofitService.retrofitService.getDownloadPlan(auth.uid.toString()).enqueue(object : Callback<ArrayList<Plan>>{
                        override fun onResponse(
                            call: Call<ArrayList<Plan>>,
                            response: Response<ArrayList<Plan>>
                        ) {
                            if(response.isSuccessful) {
                                Log.d("getDownloadPlan","success")
                                Log.d("getDownloadPlan body",response.body().toString())
                                for (plan in response.body()!!) {
                                    adapter.addItem(plan)
                                    adapter.notifyItemInserted(adapter.itemCount-1)
                                }
                            }
                            else{
                                Log.e("getDownloadPlan","success but something error")
                                Log.e("getDownloadPlan error code",response.code().toString())
                            }
                        }

                        override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                            Log.e("getDownloadPlan",t.message!!)
                        }

                    })
                }
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
        calendarView.setOnDateChangedListener { _, date, _ ->
            val ft = parentFragmentManager.beginTransaction()
            ft.remove(this).add(ListFragment(),"hi")
            val db = CalendarDatabase.getInstance(requireContext())
            val dao = db.calendarDao()

            CoroutineScope(Dispatchers.Main).launch {

                val planEntity = CoroutineScope(Dispatchers.IO).async {
                    dao.getPlanByDay(date.year.toString() + "-" + date.month.toString() + "-" + date.day)
                }.await()

//                Toast.makeText(requireContext(),plan.planName + ", "+plan.count,Toast.LENGTH_SHORT).show()
                if(planEntity!=null){
                    val calendarDialogFragment = CalendarDialogFragment(date,planEntity)
                    calendarDialogFragment.show(childFragmentManager,"CalendarDialogFragment")
                }
            }
        }
        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_month)))
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        calendarView.setHeaderTextAppearance(com.prolificinteractive.materialcalendarview.R.style.CalendarWidgetHeader)
        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(com.prolificinteractive.materialcalendarview.R.style.CustomTextAppearance)
        calendarView.setTileHeightDp(40)
    }

    private fun callSetPlanActivity(){
        //TODO: plan을 생성하는 액티비티를 호출하고 플랜을 받아옴
        val intent = Intent(requireContext(),MakePlanActivity::class.java)
        startActivity(intent)
    }
}