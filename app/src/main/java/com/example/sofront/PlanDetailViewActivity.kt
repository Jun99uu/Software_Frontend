package com.example.sofront

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofront.databinding.ActivityPlanDetailViewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlanDetailViewActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    var myUid = "" //내 uid
    var planUid = "" //해당 플랜의 uid
    val myName = user?.displayName
    lateinit var plan:Plan //전달받은 plan
    lateinit var comments:ArrayList<Comment> //전달받은 comment
    lateinit var commentAdapter: CommentAdapter
    var planName = ""
    var profileImg = ""
    var planWriter = ""
    var commentNum = 0
    var likeNum = 0
    var downNum = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlanDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myUid = user?.displayName.toString()
        plan = intent.getSerializableExtra("plan") as Plan
        comments = intent.getSerializableExtra("comments") as ArrayList<Comment>
        planUid = plan.makerUid
        planName = plan.planName
        commentNum = plan.commentNum
        likeNum = plan.likeNum
        downNum = plan.downLoadNum
        commentAdapter = CommentAdapter(comments)

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

        binding.planDetailRc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.planDetailRc.adapter = PlanDetailFirstAdapter(plan.routineList)
        binding.commentRc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.commentRc.adapter = commentAdapter
        binding.commentRc.addItemDecoration(VerticalItemDecorator(30))

        binding.commentSaveBtn.setOnClickListener{
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
            val formatted = current.format(formatter)
            val content = binding.commentInput.text
            //TODO comment 서버로 전송
            //TODO commentList 다시 받아옴
            binding.commentInput.setText("")
            Toast.makeText(this, "댓글이 입력되었습니다.",Toast.LENGTH_SHORT).show()
            CloseKeyboard()
//            comments.add(Comment("하이", "uid", "${myName}", formatted, "프사", "${content}"))
            commentAdapter.notifyDataSetChanged()
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
}