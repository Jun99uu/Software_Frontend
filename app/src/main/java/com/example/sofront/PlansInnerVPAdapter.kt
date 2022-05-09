package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class PlansInnerVPAdapter(private var planinnerList: ArrayList<Workout>): RecyclerView.Adapter<PlansInnerVPAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectExerciseBtn : Button = itemView.findViewById(R.id.select_exercise_btn)
        val deleteExerciseBtn : Button = itemView.findViewById(R.id.exercise_delete_btn)
        val selectSetBtn : Button = itemView.findViewById(R.id.select_set_num)
        val setList : ViewPager2 = itemView.findViewById(R.id.set_list)
        val setIndicator : WormDotsIndicator = itemView.findViewById(R.id.set_indicator)
        fun bind(item: Workout) {
            setList.adapter = PlansInnerSetAdapter(item.setList)
            selectExerciseBtn.text = item.workoutName
            if(item.setNum != 0){
                selectSetBtn.text = item.setNum.toString()
            }
            setIndicator.setViewPager2(setList)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_pager_basic, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planinnerList[position])
        var exerciseArray: Array<String> = arrayOf("예시1", "예시2", "예시3", "예시4")
        var setArray: Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        holder.selectExerciseBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("운동 선택")
                .setItems(exerciseArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // 여기서 인자 'which'는 배열의 position을 나타냅니다.
                        holder.selectExerciseBtn.text = exerciseArray[which]
                        planinnerList[position].workoutName = exerciseArray[which]
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
        holder.selectSetBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("운동 선택")
                .setItems(setArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // 여기서 인자 'which'는 배열의 position을 나타냅니다.
                        val prevNum:Int = planinnerList[position].setNum
                        val inputNum:Int = setArray[which].toInt()
                        if(prevNum > inputNum){
                            //이미 지정한 수보다 작은 세트 수를 다시 선택했을 때
                            holder.selectSetBtn.text = setArray[which]
                            planinnerList[position].setNum = inputNum
                            for(i in 0 until prevNum - inputNum){
                                planinnerList[position].setList.removeAt(planinnerList[position].setList.size-1)
                            }
                        }else if(prevNum < inputNum){
                            //이미 지정한 수보다 큰 세트 수를 다시 선택했을 때
                            holder.selectSetBtn.text = setArray[which]
                            planinnerList[position].setNum = inputNum
                            if(prevNum == 0){
                                for(i in 1 until inputNum - prevNum){
                                    planinnerList[position].setList.add(Set(0,0))
                                }
                            }else{
                                for(i in 0 until inputNum - prevNum){
                                    planinnerList[position].setList.add(Set(0,0))
                                }
                            }
                            Log.d("데이터", planinnerList[position].toString())
                        }
                        notifyDataSetChanged()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
        holder.deleteExerciseBtn.setOnClickListener{
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return planinnerList.size
    }

    fun removeItem(position: Int){
        val builder = AlertDialog.Builder(context)
        if(itemCount > 1){
            builder.setTitle("정말로 삭제하시겠습니까?")
                .setMessage("삭제된 운동정보는 복구하실 수 없습니다.\n정말로 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        //확인클릭
                        planinnerList.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        //취소클릭
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }else{
            Toast.makeText(context, "루틴은 최소 1개의 운동을 포함해야합니다😀", Toast.LENGTH_SHORT).show()
        }
    }
}