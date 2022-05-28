package com.example.sofront

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sofront.databinding.PlanRecyclerViewBinding
import com.example.sofront.databinding.ProfilePortfolioItemBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonDisposableHandle.parent

class ProfilePortfolioRecyclerViewAdapter :
    RecyclerView.Adapter<ProfilePortfolioRecyclerViewAdapter.ViewHolder>() {
    private val portfolioList = mutableListOf<Portfolio>()
    lateinit var context: Context
    val defaultImg = R.drawable.gymdori
    val defaultContent = R.drawable.womanrun
    lateinit var myProfile:String
    val user = Firebase.auth.currentUser

    inner class ViewHolder(private val binding: ProfilePortfolioItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.portfolioTitle
        private val content = binding.portfolioContent
        private val date = binding.portfolioDate
        private val commentNum = binding.portfolioCommentNum
        private val writer = binding.portfolioWriter
        val contentImg = binding.portfolioImg
        val profile = binding.portfolioProfile
        fun bind(item : Portfolio){
            title.text= item.title
            if(item.content.length>100){
                val tmpStr = item.content.substring(0,100) + "..."
                content.text = tmpStr
            }
            else {
                content.text = item.content
            }
            date.text = item.date
            commentNum.text = item.commentNum.toString()
            binding.root.setOnClickListener{
                Log.d("click",binding.portfolioTitle.text.toString())
                val intent = Intent(binding.root.context,PortfolioActivity::class.java)
                intent.putExtra("portfolio",item)
                binding.root.context.startActivity(intent)
            }
            writer.text = item.portfolioWriterName
            if(item.liked)
            binding.portfolioLikeIcon.setImageResource(R.drawable.ic_heart_fill)
            binding.portfolioLikeNum.text = item.likeNum.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfilePortfolioItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolioList[position])
        Glide.with(context)
            .load(portfolioList[position].contentImage) // 불러올 이미지 url
            .placeholder(defaultContent) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultContent) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultContent) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(holder.contentImg) // 이미지를 넣을 뷰
        Glide.with(context)
            .load(user?.photoUrl) // 불러올 이미지 url
            .placeholder(defaultImg) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImg) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImg) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(holder.profile) // 이미지를 넣을 뷰
    }

    fun addItem(portfolio : Portfolio){
        portfolioList.add(portfolio)
    }
    override fun getItemCount(): Int {
        return portfolioList.size
    }

    fun deleteFirstItem(){
        portfolioList.removeAt(0)
    }
}