package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DailyRoutineAdater(private var workoutList:ArrayList<Workout>) : RecyclerView.Adapter<DailyRoutineAdater.ViewHolder>() {
    lateinit var context: Context
    var position = 0

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Workout){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyRoutineAdater.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_routine_item, parent, false)
        context = parent.context
        return DailyRoutineAdater.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyRoutineAdater.ViewHolder, position: Int) {
        holder.bind(workoutList[position])
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }
}