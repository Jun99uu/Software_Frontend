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
                if(hashtags.contains(itemList[position].tag)){
                    hashtags.remove(itemList[position].tag)
                }else{
                    hashtags.add(itemList[position].tag)
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
    }

}