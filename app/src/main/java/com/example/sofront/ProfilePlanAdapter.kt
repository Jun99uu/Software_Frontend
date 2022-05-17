package com.example.sofront

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePlanAdapter(private var planList: ArrayList<Plan>): RecyclerView.Adapter<ProfilePlanAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planImage : CircleImageView = itemView.findViewById(R.id.plan_profile_img)
        val planName : TextView = itemView.findViewById(R.id.profile_plan_name)
        val planHashTag: RecyclerView = itemView.findViewWithTag(R.id.profile_plan_hashtag)
        val planDate : TextView = itemView.findViewById(R.id.profile_plan_date)
        val planDownload : TextView = itemView.findViewById(R.id.profile_plan_download)
        val planComment : TextView = itemView.findViewById(R.id.profile_plan_comment)
        val planLike : TextView = itemView.findViewById(R.id.profile_plan_like)
        fun bind(item: Plan){
            planHashTag.adapter = HashtagAdapter(item.hashTagList)
            planName.text = item.planName
            planDate.text = "${item.routineList.size.toString()}day"
            planDownload.text = item.downLoadNum.toString()
            planComment.text = item.commentNum.toString()
            planLike.text = item.likeNum.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfilePlanAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_plan_item, parent, false)
        Log.d("제발", "들어오긴함")
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planList[position])
    }

    override fun getItemCount(): Int {
        return planList.size
    }
}