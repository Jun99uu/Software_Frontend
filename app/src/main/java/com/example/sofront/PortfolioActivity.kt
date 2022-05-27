package com.example.sofront

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityPortfolioBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class PortfolioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val portfolio  = intent.getSerializableExtra("portfolio") as Portfolio
        initView(portfolio,binding)

        val commentArray = TestFactory.getSomeComment(portfolio.commentNum)
        val adapter = CommentAdapter(commentArray)
        val user = adapter.user
        val uid : String
        val userName : String
        if(user==null){
            uid = "tmpUid"
            userName = "tmpName"
        }
        else{
            uid = user.uid
            userName = user.displayName!!
        }

        binding.commentSaveBtn.setOnClickListener {
            val text = binding.commentInput.text.toString()
            val comment = Comment(portfolio.title,uid,userName, Date().toString(),"",text)
            RetrofitService._postPortfolioComment(portfolio.id.toString(), comment)
            //TODO("디비에 저장하고 불러오기")
            commentArray.add(comment)
            adapter.notifyDataSetChanged()
            CloseKeyboard()
            binding.commentInput.setText("")
        }

        val recyclerView = binding.commentRc

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalItemDecorator(30))
    }
    fun initView(portfolio: Portfolio, binding: ActivityPortfolioBinding){
        binding.portfolioTitle.text  = portfolio.title
        binding.portfolioContent.text = portfolio.content
        binding.portfolioCommentNum.text = portfolio.commentNum.toString()
        binding.portfolioLikeNum.text = portfolio.likeNum.toString()
        if(portfolio.liked){
            binding.like.setBackgroundColor(Color.RED)
        }
    }
    fun CloseKeyboard()
    {
        val view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}