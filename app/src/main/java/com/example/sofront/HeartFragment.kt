package com.example.sofront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentHeartBinding
import com.google.firebase.auth.FirebaseAuth

class HeartFragment : Fragment() {
    lateinit var binding : FragmentHeartBinding
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeartBinding.inflate(inflater,container,false)
        setRecyclerView()

        return binding.root
    }
    fun setRecyclerView(){
        recyclerView = binding.heartPortfolioRv
        val adapter = ProfilePortfolioRecyclerViewAdapter()
        setRecyclerView(adapter)
        setRecyclerViewAdapter(adapter)
    }
    fun setRecyclerView(adapter:ProfilePortfolioRecyclerViewAdapter){
        val auth = FirebaseAuth.getInstance()
        if(auth.uid==null){
            val hashTagList = ArrayList<String>()
            hashTagList.add("hashTag1")
            val commentNum = 10
            val commentList = ArrayList<portfolioComment>()
            commentList.add(portfolioComment("코멘트 작성자","uid","프사","2022-08-23","commentContent"))
            commentList.add(portfolioComment("댓글작성자 박재현","uid","프사","2022-08-31","댓글내용 : 날이 꾸리꾸리"))
            adapter.addItem(Portfolio("제목1","이준규","날이 좋아 긴 문자열 짜르기 테스트 ㅁㅁㅁㅁㅁㅁㅁ\n\n\n\n\n\n\n\nㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ","2022-08-22", hashTagList, commentNum))
            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList, commentNum ))
            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList, commentNum))
            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList, commentNum))
            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList, commentNum))
            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList, commentNum))
            adapter.addItem(Portfolio("제목2","박성규","날이 안좋아","2022-08-17",hashTagList, commentNum))

        }
        else{
            val portfolioList = RetrofitService._getSubscribingPortfolio(auth.uid!!)
            for(portfolio in portfolioList){
                adapter.addItem(portfolio)
            }
        }
    }
    fun setRecyclerViewAdapter(adapter: ProfilePortfolioRecyclerViewAdapter){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }
}