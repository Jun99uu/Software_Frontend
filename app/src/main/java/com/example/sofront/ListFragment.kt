package com.example.sofront

import android.app.Instrumentation
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.*
import android.os.Bundle
import android.os.SystemClock
import android.text.style.LineBackgroundSpan
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
class view: ViewModel(){

}

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerview:RecyclerView
    lateinit var calendarView: MaterialCalendarView
    var planLength =0
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

        setRecyclerView()

        addPlanBtn.setOnClickListener {
            callSetPlanActivity()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    fun decorateDay(date:CalendarDay){
        val set:HashSet<CalendarDay> = HashSet()
        var d = date
        for(i in 1..planLength){
            set.add(d)
            Log.d("for",d.toString())
            d = addOnetoCalendarDay(d)
        }
        Log.d("Set",set.toString())
        calendarView.addDecorator(EventDecorator(set))
        planLength=0
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

    fun setRecyclerView(){
        //TODO: 서버에서 플랜을 가져와서 리사이클러뷰로 띄워줌
        val adapter = PlanRecyclerViewAdapter()
        initRecyclerViewList(adapter)
        setRecyclerViewAdapter(adapter)
    }
    fun initRecyclerViewList(adapter:PlanRecyclerViewAdapter){
        val auth = FirebaseAuth.getInstance()
        if(auth.uid == null){
            Log.d("Firebase uid","null")
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
    fun setRecyclerViewAdapter(adapter: PlanRecyclerViewAdapter){
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    fun callSetPlanActivity(){
        //TODO: plan을 생성하는 액티비티를 호출하고 플랜을 받아옴
//        addPlanList()
    }
}

class EventDecorator(dates: Collection<CalendarDay>): DayViewDecorator {

    var dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(LineSpan())
    }
}

//그림그리기
class LineSpan : LineBackgroundSpan{
    override fun drawBackground(
        canvas:Canvas,  paint:Paint,
        left:Int, right:Int, top:Int, baseline:Int, bottom:Int,
        charSequence: CharSequence,
        start:Int,end:Int,lineNum:Int)
        {
            val rect = Rect()
            rect.set(left, top-(top-bottom), right, bottom-(top-bottom))

            paint.color = Color.parseColor("#1D872A")
            canvas.drawRect(rect,paint)
    }

}