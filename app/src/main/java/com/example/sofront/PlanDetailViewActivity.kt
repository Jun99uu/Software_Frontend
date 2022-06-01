package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sofront.databinding.ActivityPlanDetailViewBinding
import com.example.sofront.databinding.FragmentPlanCollectionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.MonthView.context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlanDetailViewActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    var myUid = "" //내 uid
    var planUid = "" //해당 플랜의 uid
    lateinit var plan:Plan //전달받은 plan
    lateinit var commentAdapter: CommentAdapter
    var planName = ""
    var profileImg = ""
    var planWriter = ""
    var commentNum = 0
    var likeNum = 0
    var downNum = 0
    private var mBinding: ActivityPlanDetailViewBinding? = null
    private val binding get() = mBinding!!
    val defaultImg = R.drawable.gymdori
    var state = false
    var likedState = false
    var prevLikenum = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPlanDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myUid = user?.uid.toString()
        plan = intent.getSerializableExtra("plan") as Plan
        planUid = plan.makerUid
        planName = plan.planName
        commentNum = plan.commentNum
        likeNum = plan.likeNum
        prevLikenum = likeNum //이전좋아요 수
        downNum = plan.downLoadNum
        val planLike = PlanLike(myUid, planName)

        Log.d("썅", plan.toString())
        binding.heartNum.text = likeNum.toString()
        if(plan.liked) {
            binding.heartIc.setBackgroundResource(R.drawable.ic_heart_fill)
            likedState = true
        }

        _getCertainProfile(plan.makerUid)

        if(myUid.equals(planUid)){
            //내 플랜이면 삭제버튼 활성화
            binding.deleteBtn.visibility = View.VISIBLE
        }

        //레이아웃설정
        binding.planDetailTitle.text = planName
        binding.commentNum.text = commentNum.toString()
        binding.downNum.text = downNum.toString()
        binding.heartNum.text = likeNum.toString()
        //TODO 이미지, 플랜 작성자 바인딩해야됨

        val backIntent = Intent(this, HomeActivity::class.java)
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        binding.detailProfileImg.setOnClickListener{
            if(state){
                val intentToProfile = Intent(this, ProfileActivity::class.java)
                intentToProfile.putExtra("UID", plan.makerUid)
                startActivity(intentToProfile)
            }else{
                Toast.makeText(this,"잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.planDetailRc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.planDetailRc.adapter = PlanDetailFirstAdapter(plan.routineList)

        _getCommentByPlanName(plan.planName)

        binding.commentSaveBtn.setOnClickListener{
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
            val formatted = current.format(formatter)
            val content = binding.commentInput.text.toString()
            val comment = planComment(content, myUid, plan.planName)
            val commentItem = Comment(plan.planName, myUid, "나", "방금전", user?.photoUrl.toString(), content, "tmp")
            _postCommentInPlan(comment)
            binding.commentInput.setText("")
            Toast.makeText(this, "댓글이 입력되었습니다.",Toast.LENGTH_SHORT).show()
            CloseKeyboard()
            commentAdapter.addItem(commentItem)
            commentAdapter.notifyDataSetChanged()
        }

        binding.heartIc.setOnClickListener{
            Log.d("시발ㅅㅂ", planLike.toString())
            _postPlanLike(planLike)
        }

        binding.deleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("정말로 삭제하시겠습니까?")
                .setMessage("삭제된 플랜은 복구하실 수 없습니다.\n정말로 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        //확인클릭
                        _deletePlanByPlanName(planName, backIntent)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        //취소클릭
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }
    fun CloseKeyboard()
    {
        var view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun _getCommentByPlanName(planName:String){
        var commentList:ArrayList<Comment>
        RetrofitService.retrofitService.getCommentByPlanName(planName).enqueue(object : Callback<ArrayList<Comment>> {
            override fun onResponse(call: Call<ArrayList<Comment>>, response: Response<ArrayList<Comment>>) {
                if (response.isSuccessful) {
                    Log.d("getComment in Plan test", "success")
                    Log.d("getComment in Plan success", response.body().toString())
                    commentList = response.body()!!
                    binding.commentRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    commentAdapter = CommentAdapter(commentList)
                    binding.commentRc.adapter = commentAdapter
                    commentAdapter.notifyDataSetChanged()
                    binding.commentRc.addItemDecoration(VerticalItemDecorator(30))
                } else {
                    Log.d("getComment in Plan test", "success but something error")
                }
            }

            override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                Log.d("getComment in Plan test", "fail")
            }
        })
    }

    fun _getCertainProfile(UID:String){
        var certainProfile:certainProfile
        RetrofitService.retrofitService.getCertainProfile(UID).enqueue(object : Callback<certainProfile> {
            override fun onResponse(call: Call<certainProfile>, response: Response<certainProfile>) {
                if (response.isSuccessful) {
                    Log.d("getProfile in Plan test", "success")
                    Log.d("getProfile in Plan success", response.body().toString())
                    certainProfile = response.body()!!
                    Log.d("프로필", certainProfile.toString())
                    binding.planWriter.text = certainProfile.name
                    openImg(certainProfile.profileImg)
                } else {
                    Log.d("getProfile in Plan test", "success but something error")
                }
            }

            override fun onFailure(call: Call<certainProfile>, t: Throwable) {
                Log.d("getProfile in Plan test", "fail")
            }
        })
    }

    fun openImg(profileImg:String){
        Glide.with(this)
            .load(profileImg) // 불러올 이미지 url
            .placeholder(defaultImg) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImg) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImg) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.detailProfileImg) // 이미지를 넣을 뷰
        state = true
    }

    fun _postCommentInPlan(comment:planComment){
        RetrofitService.retrofitService.postCommentInPlan(comment).enqueue(object: Callback<planComment>{
            override fun onResponse(call: Call<planComment>, response: Response<planComment>) {
                if(response.isSuccessful){
                    Log.d("post comment in Plan test", "success")
                    Log.d("post comment in Plan success", response.body().toString())
                }else {
                    Log.d("post comment in Plan test", "success but something error")
                }
            }
            override fun onFailure(call: Call<planComment>, t: Throwable) {
                Log.d("post comment in Plan test", "fail")
            }
        })
    }

    fun _postPlanLike(planLike:PlanLike){
        RetrofitService.retrofitService.postLike(planLike).enqueue(object: Callback<PlanLike>{
            override fun onResponse(call: Call<PlanLike>, response: Response<PlanLike>) {
                if(response.isSuccessful){
                    Log.d("post like in Plan test", "success")
                    Log.d("post like in Plan success", response.body().toString())
                    if(likedState){//이미 좋아요를 누른 상태에서 좋아요 눌렀음
                        likedState = false
                        binding.heartIc.setBackgroundResource(R.drawable.ic_heart)
                        binding.heartNum.text = (prevLikenum-1).toString()
                    }else{//좋아요를 누르지 않은 사람이 좋아요를 눌렀음
                        likedState = true
                        binding.heartIc.setBackgroundResource(R.drawable.ic_heart_fill)
                        binding.heartNum.text = (prevLikenum+1).toString()
                    }
                }else {
                    Log.d("post like in Plan test", "success but something error")
                }
            }
            override fun onFailure(call: Call<PlanLike>, t: Throwable) {
                Log.d("post like in Plan test", "fail")
            }
        })
    }

    fun _deletePlanByPlanName(planName:String, intent:Intent){
        RetrofitService.retrofitService.deletePlanByPlanName(planName).enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    Log.d("plan delete", "success")
                    Toast.makeText(context, "플랜이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }else {
                    Log.d("plan delete", "success but something error")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("plan delete", "fail")
            }
        })
    }
}