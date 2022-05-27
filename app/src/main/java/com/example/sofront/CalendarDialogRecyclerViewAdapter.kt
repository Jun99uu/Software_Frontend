package com.example.sofront

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentCalendarDialogItemBinding

class CalendarDialogRecyclerViewAdapter :
    RecyclerView.Adapter<CalendarDialogRecyclerViewAdapter.ViewHolder>() {
    private val routine = ArrayList<Workout>()
    inner class ViewHolder(val binding: FragmentCalendarDialogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val routineDayTv = binding.routineDayTv
        fun bind(item : Workout){
            val tmpStr = item.workoutName + " : "+ item.setNum + "세트"
            routineDayTv.text = tmpStr
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentCalendarDialogItemBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(routine[position])
    }

    override fun getItemCount(): Int {
        return routine.size
    }

    fun addItem(workout: Workout) {
        routine.add(workout)
    }
}