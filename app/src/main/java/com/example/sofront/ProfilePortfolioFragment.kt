package com.example.sofront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.FragmentProfilePortfolioBinding
import com.google.firebase.auth.FirebaseAuth

class ProfilePortfolioFragment : Fragment() {
    lateinit var binding: FragmentProfilePortfolioBinding
    lateinit var recyclerView : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfilePortfolioBinding.inflate(inflater,container,false)
        recyclerView = binding.profilePortfolioRv

        setRecyclerView()
        return binding.root
    }
    fun setRecyclerView(){
        val adapter = ProfilePortfolioRecyclerViewAdapter()
        setRecyclerView(adapter)
        setRecyclerViewAdapter(adapter)
    }
    fun setRecyclerView(adapter:ProfilePortfolioRecyclerViewAdapter){
        val auth = FirebaseAuth.getInstance()
        if(auth.uid==null){
            val hashTagList = ArrayList<String>()
            hashTagList.add("hashTag1")
            val commentList = ArrayList<Comment>()
            commentList.add(TestFactory.getSomeComment())
            commentList.add(TestFactory.getSomeComment())
            adapter.addItem(TestFactory.getSomePortfolio())
            adapter.addItem(TestFactory.getSomePortfolio())
            adapter.addItem(TestFactory.getSomePortfolio())
        }
        else{
            val portfolioList = RetrofitService._getPortfolio(auth.uid!!)
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