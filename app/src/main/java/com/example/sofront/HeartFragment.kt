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
            val portfolioList = TestFactory.getSomePortfolio(10)
            for(portfolio in portfolioList){
                adapter.addItem(portfolio)
            }

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