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
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MakePlanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMakePlanBinding
    private var recyclerViewState: Parcelable? = null
    val itemList = arrayListOf<String>()      // 아이템 배열
    val hashtagAdapter = HashtagAdapter(itemList)     // 어댑터
    val routineList = ArrayList<Routine>() //플랜 배열 _ 이게 Plan.kt의 Plan
    val plansAdapter = PlansAdapter(routineList) //플랜 어댑터
    var planNameCheck = false //중복검사 했는지 확인하는 변수
    lateinit var planName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakePlanBinding.inflate(layoutInflater)
        val view = binding.root
        val auth = FirebaseAuth.getInstance()

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

        binding.planNameCheckBtn.setOnClickListener{
            if(!planNameCheck){
                val planName = binding.title.text.toString()
                //여기서 플랜명 중복검사
                if(true && planName != ""){
                    //만약 겹치는 플랜명이 없다면??
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("플랜명을 사용하시겠습니까?")
                        .setMessage("해당 플랜명 사용에 동의하면,\n플랜 작성 중 변경할 수 없습니다.\n해당 플랜명을 사용하시겠습니까?")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener { dialog, id ->
                                //확인클릭
                                binding.title.isEnabled = false
                                planNameCheck = true
                                binding.planNameWarning.visibility = View.GONE //경고문구 gone
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, id ->
                                //취소클릭
                            })
                    // 다이얼로그를 띄워주기
                    builder.show()
                }else if(planName == ""){
                    Toast.makeText(this, "플랜명을 입력해주세요.", Toast.LENGTH_LONG).show()
                }else{
                    binding.planNameWarning.visibility = View.VISIBLE //경고문구 visible
                }
            }
        }

        val hashtags = ArrayList<String>() //클릭한 해쉬태그

        binding.hashtagList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hashtagList.adapter = hashtagAdapter

        hashtagAdapter.setItemClickListener(object: HashtagAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int){
                if(hashtags.size < 5){
                    if(hashtags.contains(itemList[position])){
                        hashtags.remove(itemList[position])
                        v.setBackgroundResource(R.drawable.home_no_select_btn)
                    }else{
                        hashtags.add(itemList[position])
                        v.setBackgroundResource(R.drawable.home_yes_select_btn)
                    }
                }else{
                    if(hashtags.contains(itemList[position])){
                        hashtags.remove(itemList[position])
                        v.setBackgroundResource(R.drawable.home_no_select_btn)
                    }else {
                        Toast.makeText(view.context, "태그는 5개까지만 선택할 수 있어요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                binding.hashtagNum.text = "(${hashtags.size.toString()}/5)"
            }
        })

        itemList.add("유산소")
        itemList.add("가슴")
        itemList.add("등")
        itemList.add("어깨")
        itemList.add("하체")
        itemList.add("팔")

        hashtagAdapter.notifyDataSetChanged()

        val initSet = ArrayList<Set>()
        initSet.add(Set(0,0))
        val initWorkout = ArrayList<Workout>()
        initWorkout.add(Workout("", 0, initSet))

        binding.planList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        plansAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.planList.adapter = plansAdapter
        routineList.add(Routine(false, initWorkout))
        plansAdapter.notifyDataSetChanged()

        binding.planAddBtn.setOnClickListener{
            saveRecyclerViewState()
            val plusSet = ArrayList<Set>()
            plusSet.add(Set(0,0))
            val plusWorkout = ArrayList<Workout>()
            plusWorkout.add(Workout("", 0, plusSet))
            routineList.add(Routine(false, plusWorkout))
            plansAdapter.notifyDataSetChanged()
            binding.planList.scrollToPosition(plansAdapter.itemCount-1)
        }

        binding.planSaveBtn.setOnClickListener{
            if(!planNameCheck){
                Toast.makeText(this, "플랜명 중복검사를 진행해주세요.", Toast.LENGTH_LONG).show()
            }else{
                val plan = Plan(planName, hashtags, routineList, auth.uid.toString(), false,0,0,0)
                Log.d("최종 데이터", "${plan}")
//                RetrofitService._setPlan(plan)
                RetrofitService.retrofitService.setPlan(plan).enqueue(object : Callback<Plan> {
                    override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                        if(response.isSuccessful){
                            Log.d("setPlan","success")
                            finish()
                        }
                        else{
                            if(response.code() == NetworkErrorCode.PLAN_NAME_OVERLAP){
                                Toast.makeText(applicationContext,"중복된 이름입니다.",Toast.LENGTH_LONG).show()
                            }
                            else {
                                Log.e("setPlan", "success but something error")
                                Log.e("setPlan error code", response.code().toString())
                                Log.e("setPlan error message", response.message())
                            }
                        }
                    }

                    override fun onFailure(call: Call<Plan>, t: Throwable) {
                        Log.e("setPlan","fail")
                        Log.e("setPlan error message",t.message.toString())
                    }
                })
            }
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

    //스크롤 새로고침 막는 부분이긴하나.. 작동안되는듯
    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            setSavedRecyclerViewState()
        }
    }

    private fun saveRecyclerViewState(){
        recyclerViewState = binding.planList.layoutManager!!.onSaveInstanceState()
    }

    private fun setSavedRecyclerViewState(){
        binding.planList.layoutManager!!.onRestoreInstanceState(recyclerViewState)
    }

}