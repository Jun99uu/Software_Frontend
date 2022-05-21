package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class PlanDetailSecondAdapter(private var planinnerList: ArrayList<Workout>): RecyclerView.Adapter<PlanDetailSecondAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectExerciseBtn : TextView = itemView.findViewById(R.id.select_exercise_btn)
        val selectSetBtn : TextView = itemView.findViewById(R.id.select_set_num)
        val setList : ViewPager2 = itemView.findViewById(R.id.set_list)
        val setIndicator : WormDotsIndicator = itemView.findViewById(R.id.set_indicator)
        fun bind(item: Workout) {
            setList.adapter = PlansInnerSetAdapter(item.setList)
            selectExerciseBtn.text = item.workoutName
            if(item.setNum != 0){
                selectSetBtn.text = item.setNum.toString()
            }
            setIndicator.setViewPager2(setList)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_detail_pager_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planinnerList[position])
        holder.selectExerciseBtn.text = planinnerList[position].workoutName
        holder.selectSetBtn.text = planinnerList[position].setNum.toString()
    }

    override fun getItemCount(): Int {
        return planinnerList.size
    }
}