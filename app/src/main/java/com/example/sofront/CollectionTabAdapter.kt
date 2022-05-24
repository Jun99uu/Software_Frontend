package com.example.sofront

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CollectionTabAdapter(private var hashList: ArrayList<String>): RecyclerView.Adapter<CollectionTabAdapter.MyViewHolder>() {
    lateinit var context:Context
    lateinit var plans:ArrayList<Plan>
    var position = 0
    var firstTitle = ""
    var secondTitle = ""

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tabTitle: TextView = itemView.findViewById(R.id.plan_tab_title)

        val firstPlanTitle: TextView = itemView.findViewById(R.id.first_pc_title)
        val firstPlanDays: TextView = itemView.findViewById(R.id.first_pc_days)
        val firstPlanComment: TextView = itemView.findViewById(R.id.first_pc_comments)
        val firstPlanDowns: TextView = itemView.findViewById(R.id.first_pc_downs)
        val firstplanLike: TextView = itemView.findViewById(R.id.first_pc_likess)

        val secondPlanTitle: TextView = itemView.findViewById(R.id.second_pc_title)
        val secondPlanDays: TextView = itemView.findViewById(R.id.second_pc_days)
        val secondPlanComment: TextView = itemView.findViewById(R.id.second_pc_comments)
        val secondPlanDowns: TextView = itemView.findViewById(R.id.second_pc_downs)
        val secondplanLike: TextView = itemView.findViewById(R.id.second_pc_likess)

        val detailBtn: Button = itemView.findViewById(R.id.detail_collection_btn)
        fun bind(item:String){
            tabTitle.text = "#${item}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionTabAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_collection_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionTabAdapter.MyViewHolder, position: Int) {
        holder.bind(hashList[position])
//        TODO 아래주석 서버 통신 + 바인딩 구문
//        plans = RetrofitService._getPlanByHashTag(hashList[position])
//
//        if(plans[0].planName.length > 7) firstTitle = plans[0].planName.substring(0,6) + "…"
//        else firstTitle = plans[0].planName
//        if(plans[1].planName.length > 7) secondTitle = plans[1].planName.substring(0,6) + "…"
//        else secondTitle = plans[1].planName
//
//        holder.firstPlanTitle.text = firstTitle
//        holder.firstPlanDays.text = "${plans[0].routineList.size}days"
//        holder.firstPlanDowns.text = plans[0].downLoadNum.toString()
//        holder.firstPlanComment.text = plans[0].commentNum.toString()
//        holder.firstplanLike.text = plans[0].likeNum.toString()
//
//        holder.secondPlanTitle.text = secondTitle
//        holder.secondPlanDays.text = "${plans[1].routineList.size}days"
//        holder.secondPlanDowns.text = plans[1].downLoadNum.toString()
//        holder.secondPlanComment.text = plans[1].commentNum.toString()
//        holder.secondplanLike.text = plans[1].likeNum.toString()
    }

    override fun getItemCount(): Int {
        return hashList.size
    }
}