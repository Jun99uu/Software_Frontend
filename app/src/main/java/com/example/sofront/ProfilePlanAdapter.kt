package com.example.sofront

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePlanAdapter(private var planList: ArrayList<Plan>): RecyclerView.Adapter<ProfilePlanAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0
    val tmpComment = Comment("하이", "1234", "김종국", "2022-05-24 17:23", "프사", "안녕하세요 댓글입니다.\n힘들다...")
    val tmpCommentList = ArrayList<Comment>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planImage : CircleImageView = itemView.findViewById(R.id.plan_profile_img)
        val planName : TextView = itemView.findViewById(R.id.profile_plan_name)
        val planHashTag: RecyclerView = itemView.findViewById(R.id.profile_plan_hashtag)
        val planDate : TextView = itemView.findViewById(R.id.profile_plan_date)
        val planDownload : TextView = itemView.findViewById(R.id.profile_plan_download)
        val planComment : TextView = itemView.findViewById(R.id.profile_plan_comment)
        val planLike : TextView = itemView.findViewById(R.id.profile_plan_like)
        val likeButton: ImageButton = itemView.findViewById(R.id.profile_plan_like_ic)
        fun bind(item: Plan){
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
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_plan_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planList[position])
        val planImgList = context.resources.obtainTypedArray(R.array.planimg_array)
        val range = (0..10)
        var drawable = planImgList.getResourceId(range.random(), -1)
        holder.planImage.setImageResource(drawable)
        holder.planHashTag.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.planHashTag.adapter = ProfilePlanHashtagAdapter(planList[position].hashTagList)
        holder.itemView.setOnClickListener{
            tmpCommentList.add(tmpComment)
            tmpCommentList.add(tmpComment)
            tmpCommentList.add(tmpComment)
            tmpCommentList.add(tmpComment)
            tmpCommentList.add(tmpComment)
            val intent = Intent(context, PlanDetailViewActivity::class.java)
            intent.putExtra("plan", planList[position])
            intent.putExtra("comments", tmpCommentList)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    fun deleteFirstItem(){
        planList.removeAt(0)
    }

}

class VerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }
}