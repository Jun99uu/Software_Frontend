package com.example.sofront

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanCollectionDetailAdapter(
    private var planList: ArrayList<summaryPlan>
) : RecyclerView.Adapter<PlanCollectionDetailAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0
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
        fun bind(item: summaryPlan) {
            planName.text = item.planName
            planDate.text = "${item.planDay}day"
            planDownload.text = item.downLoadNum.toString()
            planComment.text = item.commentNum.toString()
            planLike.text = item.likeNum.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
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
            val intent = Intent(context, PlanDetailViewActivity::class.java)
            intent.putExtra("comments", tmpCommentList)
            _getPlanByPlanName(planList[position].planName, intent)
        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    fun _getPlanByPlanName(planName:String, intent:Intent) {
        var myPlan = Plan("", ArrayList(), ArrayList(), "", true, 0, 0, 0)
        RetrofitService.retrofitService.getPlanByPlanName(planName).enqueue(object :
            Callback<Plan> {
            override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                if (response.isSuccessful) {
                    Log.d("getPlan test", "success")
                    Log.d("getPlan test success", response.body().toString())
                    myPlan = response.body()!!
                    intent.putExtra("plan", myPlan) // 여기 자세한 정보로
                    Log.d("플랜", myPlan.toString())
                    context.startActivity(intent)
                } else {
                    Log.d("getPlan test", "success but something error")
                    Toast.makeText(context, "뭔가 오류가 발생했어요😱", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Plan>, t: Throwable) {
                Log.d("getPlan test", "fail")
                Toast.makeText(context, "예상치 못한 오류가 발생했어요😱", Toast.LENGTH_SHORT).show()
            }
        })
    }
}