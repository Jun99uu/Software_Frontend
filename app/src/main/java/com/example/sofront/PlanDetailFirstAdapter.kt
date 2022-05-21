package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class PlanDetailFirstAdapter(
    private var routineList: ArrayList<Routine>
) : RecyclerView.Adapter<PlanDetailFirstAdapter.MyViewHolder>() {

    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planDay : TextView = itemView.findViewById(R.id.plan_day_tv)
        val planInnerList : ViewPager2 = itemView.findViewById(R.id.inner_plan_list)
        val prevList : ViewPager2 = itemView.findViewById(R.id.preview_viewpager)
        val prevIndicator : WormDotsIndicator = itemView.findViewById(R.id.preview_indicator)
        val planIndicator : WormDotsIndicator = itemView.findViewById(R.id.plan_indicator)
        val toggleBtn: Button = itemView.findViewById(R.id.plan_toggle)
        val planSetting = itemView.findViewById<ConstraintLayout>(R.id.plan_setting)
        val preview = itemView.findViewById<ConstraintLayout>(R.id.plan_preview)
        fun bind(item: Routine) {
            planInnerList.adapter = PlanDetailSecondAdapter(item.workoutList)
            planIndicator.setViewPager2(planInnerList)
            prevList.adapter = PlanSummaryAdapter(item.workoutList)
            prevIndicator.setViewPager2(prevList)
        }
    }

    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: ConstraintLayout, layoutCollapse: ConstraintLayout): Boolean {
        // 2
        ToggleAnimation.toggleArrow(view, isExpanded)
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
            ToggleAnimation.collapse(layoutCollapse)
        } else {
            ToggleAnimation.collapse(layoutExpand)
            ToggleAnimation.expand(layoutCollapse)
        }
        return isExpanded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_detail_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(routineList[position])
        holder.planDay.text = "Day ${position+1}"
        toggleLayout(routineList[position].isExpanded, holder.toggleBtn, holder.planSetting, holder.preview)
        holder.toggleBtn.setOnClickListener{
            val show = toggleLayout(!routineList[position].isExpanded, it, holder.planSetting, holder.preview)
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
}