package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sofront.databinding.ActivityMakePlanBinding


class MakePlanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMakePlanBinding
    private lateinit var recyclerViewState: Parcelable
    val itemList = arrayListOf<HashTagData>()      // 아이템 배열
    val hashtagAdapter = HashtagAdapter(itemList)     // 어댑터
    val routineList = ArrayList<Routine>() //플랜 배열 _ 이게 Plan.kt의 Plan
    val plansAdapter = PlansAdapter(routineList) //플랜 어댑터
    lateinit var planName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakePlanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.title.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                planName = binding.title.text.toString()
            }

        })

        binding.title.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.title.windowToken, 0)
                handled = true
            }
            handled
        }

        var hashtags = ArrayList<String>() //클릭한 해쉬태그

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

        val initSet = ArrayList<Set>()
        initSet.add(Set(0,0))
        val initWorkout = ArrayList<Workout>()
        initWorkout.add(Workout("", 0, initSet))

        binding.planList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        plansAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.planList.adapter = plansAdapter
        routineList.add(Routine(true, initWorkout))
        plansAdapter.notifyDataSetChanged()

        binding.planAddBtn.setOnClickListener{
            val plusSet = ArrayList<Set>()
            plusSet.add(Set(0,0))
            val plusWorkout = ArrayList<Workout>()
            plusWorkout.add(Workout("", 0, plusSet))
            routineList.add(Routine(true, plusWorkout))
            plansAdapter.notifyDataSetChanged()
        }

        binding.planSaveBtn.setOnClickListener{
            val plan = Plan(planName, hashtags, routineList)
            Log.d("최종 데이터", "${plan}")
        }

        binding.planCancleBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("정말로 삭제하시겠습니까?")
                .setMessage("삭제된 플랜은 복구하실 수 없습니다.\n정말로 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        //확인클릭
                        onBackPressed()
                        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        //취소클릭
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }

}