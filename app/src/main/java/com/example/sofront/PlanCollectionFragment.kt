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

    fun _getPlanByHashTag(hashTag: String) : ArrayList<Plan>{
        var myPlan = ArrayList<Plan>()
        RetrofitService.retrofitService.getPlanByHashTag(hashTag).enqueue(object :
            Callback<ArrayList<Plan>> {
            override fun onResponse(call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>) {
                if (response.isSuccessful) {
                    Log.d("getPlan test success", response.body().toString())
                    myPlan = response.body()!!
                } else {
                    Log.d("getPlan test", "success but something error")
                }
            }

            override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                Log.d("getPlan test", "fail")
            }
        })
        return myPlan
    }
}