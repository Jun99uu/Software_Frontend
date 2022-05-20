package com.example.sofront

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.PlanRecyclerViewBinding
import com.example.sofront.databinding.ProfilePortfolioRecyclerViewBinding

class ProfilePortfolioRecyclerViewAdapter :
    RecyclerView.Adapter<ProfilePortfolioRecyclerViewAdapter.ViewHolder>() {
    private val portfolioList = mutableListOf<Portfolio>()
    inner class ViewHolder(binding: ProfilePortfolioRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.portfolioTitle
        private val content = binding.portfolioContent
        private val date = binding.portfolioDate
        fun bind(item : Portfolio){
            title.text= item.title
            content.text = item.content
            date.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ProfilePortfolioRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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