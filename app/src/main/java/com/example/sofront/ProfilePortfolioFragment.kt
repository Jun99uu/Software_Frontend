package com.example.sofront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentProfilePortfolioBinding
import com.example.sofront.databinding.ProfilePortfolioRecyclerViewBinding
import java.sql.Blob

class ProfilePortfolioFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProfilePortfolioBinding.inflate(inflater,container,false)
        val recyclcerView= binding.profilePortfolioRv
        val adapter = ProfilePortfolioRecyclerViewAdapter()
        val hashTagList = ArrayList<String>()
        hashTagList.add("hashTag1")
        val commentList = ArrayList<Comment>()
        commentList.add(Comment("코멘트 작성자","2022-08-23","commentContent"))
        adapter.addItem(Portfolio("hi","hi","content","2022-08-22", hashTagList, commentList))
        recyclcerView.adapter = adapter
        recyclcerView.layoutManager = LinearLayoutManager(this.context)
        return binding.root
    }

}