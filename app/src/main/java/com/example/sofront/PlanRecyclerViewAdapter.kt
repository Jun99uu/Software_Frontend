package com.example.sofront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.PlanRecyclerViewBinding

class PlanRecyclerViewAdapter() : RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder>() {
    var testList = mutableListOf<String>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanRecyclerViewAdapter.ViewHolder {
        val binding = PlanRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(testList[position])
    }

    override fun getItemCount(): Int {
        return testList.size
    }
    inner class ViewHolder(binding: PlanRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root){
        private val textView = binding.testTextView
        fun bind(item: String){
            textView.text = item
        }
    }

}