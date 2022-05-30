package com.example.sofront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityPlanCollectionDetailBinding

class PlanCollectionDetailActivity : AppCompatActivity() {
    lateinit var hashTag:String
    lateinit var plans:ArrayList<summaryPlan>
    lateinit var adapter:PlanCollectionDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlanCollectionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hashTag = "#${intent.getStringExtra("hashtag").toString()}"
        plans = intent.getSerializableExtra("plans") as ArrayList<summaryPlan>
        adapter = PlanCollectionDetailAdapter(plans)

        binding.hashTitle.text = hashTag
        binding.hashDetailRc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.hashDetailRc.adapter = adapter
        binding.hashDetailRc.addItemDecoration(VerticalItemDecorator(30))
    }
}