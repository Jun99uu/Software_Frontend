package com.example.sofront

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.PlanRecyclerViewBinding
import com.example.sofront.databinding.ProfilePortfolioItemBinding

class ProfilePortfolioRecyclerViewAdapter :
    RecyclerView.Adapter<ProfilePortfolioRecyclerViewAdapter.ViewHolder>() {
    private val portfolioList = mutableListOf<Portfolio>()
    inner class ViewHolder(private val binding: ProfilePortfolioItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.portfolioTitle
        private val content = binding.portfolioContent
        private val date = binding.portfolioDate
        private val commentNum = binding.portfolioCommentNum
        private val writer = binding.portfolioWriter
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
            writer.text = item.portfolioWriter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfilePortfolioItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolioList[position])
    }

    fun addItem(portfolio : Portfolio){
        portfolioList.add(portfolio)
    }
    override fun getItemCount(): Int {
        return portfolioList.size
    }
}