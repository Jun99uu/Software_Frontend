package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityMakePlanBinding

class MakePlanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMakePlanBinding
    val itemList = arrayListOf<HashTagData>()      // 아이템 배열
    val hashtagAdapter = HashtagAdapter(itemList)     // 어댑터
    val planList = arrayListOf<PlanData>() //플랜 배열
    val plansAdapter = PlansAdapter(planList) //플랜 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakePlanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var hashtags = mutableListOf<String>() //클릭한 해쉬태그

        binding.hashtagList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hashtagList.adapter = hashtagAdapter

        hashtagAdapter.setItemClickListener(object: HashtagAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int){
                if(hashtags.size < 5){
                    if(hashtags.contains(itemList[position].tag)){
                        hashtags.remove(itemList[position].tag)
                        v.setBackgroundResource(R.drawable.home_no_select_btn)
                    }else{
                        hashtags.add(itemList[position].tag)
                        v.setBackgroundResource(R.drawable.home_yes_select_btn)
                    }
                }else{
                    if(hashtags.contains(itemList[position].tag)){
                        hashtags.remove(itemList[position].tag)
                        v.setBackgroundResource(R.drawable.home_no_select_btn)
                    }else {
                        Toast.makeText(view.context, "태그는 5개까지만 선택할 수 있어요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                binding.hashtagNum.text = "(${hashtags.size.toString()}/5)"
            }
        })

        itemList.add(HashTagData("유산소"))
        itemList.add(HashTagData("가슴"))
        itemList.add(HashTagData("등"))
        itemList.add(HashTagData("어깨"))
        itemList.add(HashTagData("하체"))
        itemList.add(HashTagData("팔"))

        hashtagAdapter.notifyDataSetChanged()

        binding.planList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.planList.adapter = plansAdapter
        planList.add(PlanData(true))
        plansAdapter.notifyDataSetChanged()

        binding.planAddBtn.setOnClickListener{
            planList.add(PlanData(true))
            plansAdapter.notifyDataSetChanged()
        }


    }

}