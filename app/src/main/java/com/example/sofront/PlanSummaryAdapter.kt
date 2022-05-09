package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class PlanSummaryAdapter(private var planinnerList: ArrayList<Workout>): RecyclerView.Adapter<PlanSummaryAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val prevPlan : TextView = itemView.findViewById(R.id.preview_text)
        fun bind(item: Workout){
            if(!(item.workoutName.equals("") || item.setNum == 0)){
                prevPlan.text = "${item.workoutName} ${item.setNum}세트"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_summary_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planinnerList[position])

    }

    override fun getItemCount(): Int {
        return planinnerList.size
    }
}