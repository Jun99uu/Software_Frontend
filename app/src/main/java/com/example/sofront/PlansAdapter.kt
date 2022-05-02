package com.example.sofront

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class PlansAdapter(
    private var planList: ArrayList<PlanData>
) : RecyclerView.Adapter<PlansAdapter.MyViewHolder>() {

    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planDay : TextView = itemView.findViewById<TextView>(R.id.plan_day_tv)
        val deleteBtn : Button = itemView.findViewById(R.id.plan_delete_btn)
        val planInnerList : ViewPager2 = itemView.findViewById(R.id.inner_plan_list)
        fun bind(plan: PlanData) {
            val planSetting = itemView.findViewById<ConstraintLayout>(R.id.plan_setting)
            val toggleBtn: Button = itemView.findViewById(R.id.plan_toggle)
            planInnerList.adapter = PlansInnerVPAdapter(plan.planInfoList)
            toggleLayout(plan.isExpanded, toggleBtn, planSetting)
            toggleBtn.setOnClickListener{
                val show = toggleLayout(!plan.isExpanded, it, planSetting)
                plan.isExpanded = show
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
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planList[position])
        holder.planDay.text = "Day ${position+1}"
        holder.deleteBtn.setOnClickListener{
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    fun removeItem(position: Int){
        if(position > 0){
            planList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}
