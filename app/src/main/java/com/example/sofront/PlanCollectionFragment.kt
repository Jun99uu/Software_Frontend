package com.example.sofront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentPlanCollectionBinding

class PlanCollectionFragment : Fragment() {
    lateinit var binding: FragmentPlanCollectionBinding
    lateinit var adapter:CollectionTabAdapter
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanCollectionBinding.inflate(layoutInflater)
        var itemList = ArrayList<String>()
        itemList.add("유산소")
        itemList.add("가슴")
        itemList.add("등")
        itemList.add("어깨")
        itemList.add("하체")
        itemList.add("팔")
        binding.plantabMenu.layoutManager = linearLayoutManager
        adapter = CollectionTabAdapter(itemList)
        binding.plantabMenu.adapter = adapter
        binding.plantabMenu.addItemDecoration(VerticalItemDecorator(40))
        return binding.root
    }
}