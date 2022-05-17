package com.example.sofront

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.*
import android.os.Bundle
import android.text.style.LineBackgroundSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.sofront.databinding.FragmentListBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlin.collections.HashSet
import kotlin.collections.Set

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

        setRecyclerView()
        setOnDragAndDrop()

        addPlanBtn.setOnClickListener {
            callSetPlanActivity()
        }
        calendarView.setOnDateChangedListener { widget, date, selected ->
            decorateDay(date)
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
    fun setOnDragAndDrop(){
        calendarView.setOnDragListener { v: View, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // Determines if this View can accept the dragged data.
                    if (e.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // As an example of what your application might do, applies a blue color tint
                        // to the View to indicate that it can accept data.
                        (v as? ImageView)?.setColorFilter(Color.BLUE)

                        // Invalidate the view to force a redraw in the new tint.
                        v.invalidate()

                        // Returns true to indicate that the View can accept the dragged data.
                        true
                    } else {
                        // Returns false to indicate that, during the current drag and drop operation,
                        // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Applies a green tint to the View.
                    (v as? ImageView)?.setColorFilter(Color.GREEN)

                    // Invalidates the view to force a redraw in the new tint.
                    v.invalidate()

                    // Returns true; the value is ignored.
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    // Ignore the event.
                    true
                DragEvent.ACTION_DRAG_EXITED -> {
                    // Resets the color tint to blue.
                    (v as? ImageView)?.setColorFilter(Color.BLUE)

                    // Invalidates the view to force a redraw in the new tint.
                    v.invalidate()

                    // Returns true; the value is ignored.
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Gets the item containing the dragged data.
                    val item: ClipData.Item = e.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.
                    Toast.makeText(requireContext(), "Dragged data is $dragData", Toast.LENGTH_SHORT)
                        .show()
                    planLength = item.text.toString().toInt()
                    // Turns off any color tints.
                    (v as? ImageView)?.clearColorFilter()

                    // Invalidates the view to force a redraw.
                    v.invalidate()

                    // Returns true. DragEvent.getResult() will return true.
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    // Turns off any color tinting.
                    (v as? ImageView)?.clearColorFilter()

                    // Invalidates the view to force a redraw.
                    v.invalidate()

                    // Does a getResult(), and displays what happened.
                    when (e.result) {
                        true ->
                            Toast.makeText(
                                requireContext(),
                                "The drop was handled.",
                                Toast.LENGTH_SHORT
                            )
                        else ->
                            Toast.makeText(
                                requireContext(),
                                "The drop didn't work.",
                                Toast.LENGTH_SHORT
                            )
                    }.show()

                    // Returns true; the value is ignored.
                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e(
                        "DragDrop Example",
                        "Unknown action type received by View.OnDragListener."
                    )
                    false
                }
            }

            true
        }
    }
    fun initRecyclerViewList(adapter:PlanRecyclerViewAdapter){
        val a = ArrayList<Routine>()
        a.add(Routine(true,ArrayList()))
        a.add(Routine(true,ArrayList()))
        a.add(Routine(true,ArrayList()))
        adapter.addItem(Plan("3일짜리",ArrayList(),a,"test",true))
        val b = ArrayList<Routine>()
        b.add(Routine(true,ArrayList()))
        b.add(Routine(true,ArrayList()))
        b.add(Routine(true,ArrayList()))
        b.add(Routine(true,ArrayList()))
        adapter.addItem(Plan("4일짜리",ArrayList(),b,"test",true))
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