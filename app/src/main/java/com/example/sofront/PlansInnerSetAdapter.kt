package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlansInnerSetAdapter(private var setList: ArrayList<PlanSet>): RecyclerView.Adapter<PlansInnerSetAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setTitle : TextView = itemView.findViewById(R.id.set_title)
        fun bind(item: PlanSet){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlansInnerSetAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.num_list_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlansInnerSetAdapter.MyViewHolder, position: Int) {
        holder.bind(setList[position])
        holder.setTitle.text = "${position+1}세트"
    }

    override fun getItemCount(): Int {
        return setList.size
    }
}