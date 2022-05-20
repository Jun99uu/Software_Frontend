package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sofront.databinding.ActivityMainBinding
import com.example.sofront.databinding.ActivityPortfolioBinding
import com.example.sofront.databinding.ActivityProfileBinding

class PortfolioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val portfolio  = intent.getSerializableExtra("portfolio") as Portfolio
        Log.d("PortfolioAcitivy","good")
        binding.portfolioTitle.text  = portfolio.title
        binding.portfolioContent.text = portfolio.content
        binding.portfolioCommentNum.text = portfolio.commentList.size.toString()
//        binding.portfolioLikeNum.text = portfolio.like
    }
}