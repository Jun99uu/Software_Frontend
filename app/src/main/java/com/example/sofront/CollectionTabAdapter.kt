package com.example.sofront

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionTabAdapter(private var hashList: ArrayList<String>, private var summaryList:ArrayList<ArrayList<summaryPlan>>): RecyclerView.Adapter<CollectionTabAdapter.MyViewHolder>() {
    lateinit var context:Context
    var position = 0
    var firstTitle = ""
    var secondTitle = ""

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tabTitle: TextView = itemView.findViewById(R.id.plan_tab_title)

        val firstPlanLayout: ConstraintLayout = itemView.findViewById(R.id.first_pc_layout)
        val secondPlanLayout: ConstraintLayout = itemView.findViewById(R.id.second_pc_layout)
        val noViewLayout: TextView = itemView.findViewById(R.id.no_view)

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
        val tmpCommentList = ArrayList<Comment>()
//        tmpCommentList.add(Comment("하이", "12345", "홍길동","2022-05-25 | 10:18", "ㅎㅇ", "하잉요오오ㅗ"))


//        TODO 아래주석 서버 통신 + 바인딩 구문

        if(summaryList[position].size > 2) {
            if (summaryList[position][0].planName.length > 7) firstTitle =
                summaryList[position][0].planName.substring(0, 6) + "…"
            else firstTitle = summaryList[position][0].planName
            if (summaryList[position][1].planName.length > 7) secondTitle =
                summaryList[position][1].planName.substring(0, 6) + "…"
            else secondTitle = summaryList[position][1].planName

            holder.firstPlanTitle.text = firstTitle
            holder.firstPlanDays.text = "${summaryList[position][0].planDay}days"
            holder.firstPlanDowns.text = summaryList[position][0].downLoadNum.toString()
            holder.firstPlanComment.text = summaryList[position][0].commentNum.toString()
            holder.firstplanLike.text = summaryList[position][0].likeNum.toString()

            holder.secondPlanTitle.text = secondTitle
            holder.secondPlanDays.text = "${summaryList[position][1].planDay}days"
            holder.secondPlanDowns.text = summaryList[position][1].downLoadNum.toString()
            holder.secondPlanComment.text = summaryList[position][1].commentNum.toString()
            holder.secondplanLike.text = summaryList[position][1].likeNum.toString()

            val intent = Intent(context, PlanDetailViewActivity::class.java)
            holder.firstPlanLayout.setOnClickListener {
                intent.putExtra("comments", tmpCommentList)
                _getPlanByPlanName(summaryList[position][0].planName, intent)
            }
            holder.secondPlanLayout.setOnClickListener {
                intent.putExtra("comments", tmpCommentList)
                _getPlanByPlanName(summaryList[position][1].planName, intent)
            }
        }else if(summaryList[position].size == 2){
            if (summaryList[position][0].planName.length > 7) firstTitle =
                summaryList[position][0].planName.substring(0, 6) + "…"
            else firstTitle = summaryList[position][0].planName
            if (summaryList[position][1].planName.length > 7) secondTitle =
                summaryList[position][1].planName.substring(0, 6) + "…"
            else secondTitle = summaryList[position][1].planName

            holder.firstPlanTitle.text = firstTitle
            holder.firstPlanDays.text = "${summaryList[position][0].planDay}days"
            holder.firstPlanDowns.text = summaryList[position][0].downLoadNum.toString()
            holder.firstPlanComment.text = summaryList[position][0].commentNum.toString()
            holder.firstplanLike.text = summaryList[position][0].likeNum.toString()

            holder.secondPlanTitle.text = secondTitle
            holder.secondPlanDays.text = "${summaryList[position][1].planDay}days"
            holder.secondPlanDowns.text = summaryList[position][1].downLoadNum.toString()
            holder.secondPlanComment.text = summaryList[position][1].commentNum.toString()
            holder.secondplanLike.text = summaryList[position][1].likeNum.toString()

            val intent = Intent(context, PlanDetailViewActivity::class.java)
            holder.firstPlanLayout.setOnClickListener {
                intent.putExtra("comments", tmpCommentList)
                _getPlanByPlanName(summaryList[position][0].planName, intent)
            }
            holder.secondPlanLayout.setOnClickListener {
                intent.putExtra("comments", tmpCommentList)
                _getPlanByPlanName(summaryList[position][1].planName, intent)
            }
            holder.detailBtn.visibility = View.INVISIBLE
        }else if(summaryList[position].size == 1){
            if (summaryList[position][0].planName.length > 7) firstTitle =
                summaryList[position][0].planName.substring(0, 6) + "…"
            else firstTitle = summaryList[position][0].planName
            holder.firstPlanTitle.text = firstTitle
            holder.firstPlanDays.text = "${summaryList[position][0].planDay}days"
            holder.firstPlanDowns.text = summaryList[position][0].downLoadNum.toString()
            holder.firstPlanComment.text = summaryList[position][0].commentNum.toString()
            holder.firstplanLike.text = summaryList[position][0].likeNum.toString()

            val intent = Intent(context, PlanDetailViewActivity::class.java)
            holder.firstPlanLayout.setOnClickListener {
                intent.putExtra("comments", tmpCommentList)
                _getPlanByPlanName(summaryList[position][0].planName, intent)
            }

            holder.secondPlanLayout.visibility = View.INVISIBLE
            holder.detailBtn.visibility = View.INVISIBLE
        }else if(summaryList[position].size == 0){
            holder.firstPlanLayout.visibility = View.INVISIBLE
            holder.secondPlanLayout.visibility = View.INVISIBLE
            holder.detailBtn.visibility = View.INVISIBLE
            holder.noViewLayout.visibility = View.VISIBLE
        }

        val intentToDetail = Intent(context, PlanCollectionDetailActivity::class.java)
        holder.detailBtn.setOnClickListener{
            intentToDetail.putExtra("hashtag",hashList[position])
            intentToDetail.putExtra("plans", summaryList[position])
            context.startActivity(intentToDetail)
        }
    }

    override fun getItemCount(): Int {
        return hashList.size
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