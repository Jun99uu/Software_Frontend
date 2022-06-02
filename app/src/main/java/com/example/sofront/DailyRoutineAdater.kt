package com.example.sofront

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DailyRoutineAdater(val context: Context) : RecyclerView.Adapter<DailyRoutineAdater.ViewHolder>() {
//    lateinit var context: Context
//    var position = 0
    private val workoutList = ArrayList<Workout>()

    class ViewHolder(itemView : View,private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.routine_item_title)
        private val progressBar : ProgressBar = itemView.findViewById(R.id.routine_item_progress_bar)
        fun bind(item:Workout){
            title.text = item.workoutName
            progressBar.max = item.setNum
            itemView.setOnClickListener{
                val intent = Intent(context, DailyRoutineDetailActivity::class.java)
                intent.putExtra("workout",item)
//                intent.putExtra("workoutName",item.workoutName)
//                intent.putExtra("totalSet",item.setNum)
                val nowSet = 0// 0 대신 여태까지 한 세트수 (내부디비에 있는거) 저장
                intent.putExtra("nowSet",nowSet)
//                intent.putExtra("set",item.setList[nowSet])
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_routine_item, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workoutList[position])
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    fun addItem(item: Workout){
        workoutList.add(item)
    }
}