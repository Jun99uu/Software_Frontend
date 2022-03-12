package com.example.sofront

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.SelectItemRecyclerBinding

class SelectItemAdapter() : RecyclerView.Adapter<SelectItemAdapter.MyViewHolder>() {

    fun newInstance() : SelectItemAdapter{
        val j = SelectItemAdapter()
        j.count = count
        j.userInfo = userInfo
        j.textList = textList
        j.toggleList = toggleList

        return j
    }
    var count : Int = 0
    var userInfo = UserInfo()
    var textList = mutableListOf<String>()
    lateinit var toggleList : ArrayList<ToggleButton>
    var buttonList = mutableListOf<Button>()
    inner class MyViewHolder(private val binding: SelectItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(text: String){
            binding.rvBt.text = text
        }
        fun setOnClick(position: Int){

//            DetailInfoActivity::expand(position)
            binding.rvBt.setOnClickListener {
                expand(count)
                when(count) {
                    0 -> userInfo.user_level = binding.rvBt.text.toString()
                    1 -> userInfo.user_purpose = binding.rvBt.text.toString()
                    2 -> userInfo.user_type = binding.rvBt.text.toString()
                    3 -> userInfo.user_time = binding.rvBt.text.toString()
                    4 -> userInfo.user_number = binding.rvBt.text.toString()
                }
                for(i in 0 until buttonList.size){
                    if(i == position){
                        buttonList[i].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.binding.root.context,R.color.item_selected))
                    }
                    else{
                        buttonList[i].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.binding.root.context,R.color.item_nselected))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SelectItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        buttonList.add(binding.rvBt)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(textList[position])
        holder.setOnClick(position)
    }

    override fun getItemCount(): Int {
        return textList.size
    }
    fun expand(x: Int){
        for (i in 0 until toggleList.size) {
            val toggleButton = toggleList[i]
            if(i==x && toggleButton.isChecked) {
                toggleButton.toggle() }
            else if(i==x+1 && x < toggleList.size ){
                if(!toggleButton.isChecked) toggleButton.toggle()
            }
            else{
                if(toggleButton.isChecked) {
                    toggleButton.toggle()
                }
            }
        }
    }
}