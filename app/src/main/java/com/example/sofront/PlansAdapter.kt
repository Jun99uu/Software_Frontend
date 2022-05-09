package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class PlansAdapter(
    private var routineList: ArrayList<Routine>
) : RecyclerView.Adapter<PlansAdapter.MyViewHolder>() {

    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planDay : TextView = itemView.findViewById<TextView>(R.id.plan_day_tv)
        val deleteBtn : Button = itemView.findViewById(R.id.plan_delete_btn)
        val planInnerList : ViewPager2 = itemView.findViewById(R.id.inner_plan_list)
        val prevList : ViewPager2 = itemView.findViewById(R.id.preview_viewpager)
        val prevIndicator : WormDotsIndicator = itemView.findViewById(R.id.preview_indicator)
        val planIndicator : WormDotsIndicator = itemView.findViewById(R.id.plan_indicator)
        val exerciseAddBtn : Button = itemView.findViewById(R.id.exercise_add_btn)
        val toggleBtn: Button = itemView.findViewById(R.id.plan_toggle)
        val planSetting = itemView.findViewById<ConstraintLayout>(R.id.plan_setting)
        fun bind(item: Routine) {
            planInnerList.adapter = PlansInnerVPAdapter(item.workoutList)
            planIndicator.setViewPager2(planInnerList)
            prevList.adapter = PlanSummaryAdapter(item.workoutList)
            prevIndicator.setViewPager2(prevList)
        }
    }

    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: ConstraintLayout): Boolean {
        // 2
        ToggleAnimation.toggleArrow(view, isExpanded)
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(routineList[position])
        holder.planDay.text = "Day ${position+1}"
        holder.deleteBtn.setOnClickListener{
            removeItem(position)
        }
        holder.exerciseAddBtn.setOnClickListener{
            val tmpSetList = ArrayList<Set>()
            tmpSetList.add(Set(0,0))
            val tmpWorkout = Workout("", 0, tmpSetList)
            routineList[position].workoutList.add(tmpWorkout)
            notifyDataSetChanged()
        }
        toggleLayout(routineList[position].isExpanded, holder.toggleBtn, holder.planSetting)
        holder.toggleBtn.setOnClickListener{
            val show = toggleLayout(!routineList[position].isExpanded, it, holder.planSetting)
            routineList[position].isExpanded = show
            if(show){
                notifyDataSetChanged()
            }else{
                notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int {
        return routineList.size
    }

    fun removeItem(position: Int){
        if(itemCount > 1){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("정말로 삭제하시겠습니까?")
                .setMessage("삭제된 루틴은 복구하실 수 없습니다.\n정말로 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        //확인클릭
                        routineList.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        //취소클릭
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }else{
            Toast.makeText(context, "플랜은 최소 1개의 루틴을 포함해야합니다😀", Toast.LENGTH_SHORT).show()
        }
    }
}
