package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class PlansInnerSetAdapter(private var setList: ArrayList<PlanSet>): RecyclerView.Adapter<PlansInnerSetAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setTitle : TextView = itemView.findViewById(R.id.set_title)
        val editWeight : EditText = itemView.findViewById(R.id.et_weight)
        val editTimes : EditText = itemView.findViewById(R.id.et_times)
        fun bind(item: PlanSet){
            editWeight.setText(item.weight.toString())
            editTimes.setText(item.count.toString())
            editWeight.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //텍스트 입력전에
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //텍스트 변할때
                }

                override fun afterTextChanged(p0: Editable?) {
                    if(!(editWeight.text.toString().equals("") || editWeight.text.toString() == null)){
                        item.weight = Integer.parseInt(editWeight.text.toString())
                    }
                }
            })
            editTimes.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //텍스트 입력전에
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //텍스트 변할때
                }

                override fun afterTextChanged(p0: Editable?) {
                    if(!(editTimes.text.toString().equals("") || editTimes.text.toString() == null)){
                        item.count = Integer.parseInt(editTimes.text.toString())
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlansInnerSetAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.num_list_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlansInnerSetAdapter.MyViewHolder, position: Int) {
        holder.bind(setList[position])
        holder.setTitle.text = "${position+1}세트"
    }

    override fun getItemCount(): Int {
        return setList.size
    }
}