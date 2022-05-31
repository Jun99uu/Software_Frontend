package com.example.sofront

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.FragmentPlanCollectionBinding
import com.example.sofront.databinding.FragmentProfilePlanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanCollectionFragment : Fragment() {
    private var mBinding: FragmentPlanCollectionBinding? = null
    private val binding get() = mBinding!!
    lateinit var adapter:CollectionTabAdapter
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPlanCollectionBinding.inflate(layoutInflater)
        _getPlanByHashTag()
        return binding.root
    }

    fun _getPlanByHashTag(){
        var myPlan:ArrayList<ArrayList<summaryPlan>>
        var itemList = ArrayList<String>()
        itemList.add("가슴")
        itemList.add("등")
        itemList.add("하체")
        itemList.add("어깨")
        itemList.add("팔")
        itemList.add("유산소")
        RetrofitService.retrofitService.getPlanByHashTag().enqueue(object : Callback<ArrayList<ArrayList<summaryPlan>>> {
            override fun onResponse(call: Call<ArrayList<ArrayList<summaryPlan>>>, response: Response<ArrayList<ArrayList<summaryPlan>>>) {
                if (response.isSuccessful) {
                    Log.d("getPlan by hashtag test success", response.body().toString())
                    myPlan = response.body()!!
                    binding.plantabMenu.layoutManager = linearLayoutManager
                    adapter = CollectionTabAdapter(itemList, myPlan)
                    binding.plantabMenu.adapter = adapter
                    binding.plantabMenu.addItemDecoration(VerticalItemDecorator(40))
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("getPlan by hashtag test", "success but something error")
                }
            }

            override fun onFailure(call: Call<ArrayList<ArrayList<summaryPlan>>>, t: Throwable) {
                Log.d("getPlan by hashtag test", "fail")
            }
        })
    }
}