package com.example.sofront

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class DailyRoutineDetailAdapter() : RecyclerView.Adapter<DailyRoutineDetailAdapter.ViewHolder>() {
    private val setArrayList = ArrayList<Set>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val info : TextView = itemView.findViewById(R.id.workout_detail)
        private val nowSet : TextView = itemView.findViewById(R.id.workout_set)
        val stateBox:ConstraintLayout = itemView.findViewById(R.id.state_box)
        fun bind(item : Set, position: Int){
            val nowText = (position+1).toString()+"set"
            nowSet.text = nowText
            (item.weight.toString()+"kg으로 "+item.count+"회 할거에요\uD83D\uDCAA").also { info.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_routine_detail_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(setArrayList[position],position)
    }

    override fun getItemCount(): Int {
        return setArrayList.size
    }
    fun addItem(item : Set){
        setArrayList.add(item)
    }

    fun fillColor(holder:ViewHolder){
        holder.stateBox.backgroundTintMode
    }
}