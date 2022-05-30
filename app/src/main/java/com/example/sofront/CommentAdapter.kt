package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(private var commentList: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {
    lateinit var context: Context
    var position = 0
    val user = Firebase.auth.currentUser

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg:CircleImageView = itemView.findViewById(R.id.comment_profile) //댓글 작성자 프사 url
        val commentName: TextView = itemView.findViewById(R.id.comment_name)
        val commentDate: TextView = itemView.findViewById(R.id.comment_date)
        val commentContent: TextView = itemView.findViewById(R.id.comment_content)
        val deleteBtn: Button = itemView.findViewById(R.id.comment_delete_btn)
        fun bind(item: Comment) {
            commentName.text = item.writerName
            commentDate.text = item.commentDate
            commentContent.text = item.commentContent
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(commentList[position])
        if(user?.uid.equals(commentList[position].writerUid)){
            holder.deleteBtn.visibility = View.VISIBLE
        }
        holder.deleteBtn.setOnClickListener{
            deleteComment(position,commentList[position].commentN)
        }
        holder.profileImg.setOnClickListener{
            onClickProfileImg(commentList[position].writerUid)
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun deleteComment(position:Int, commentID: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("정말로 삭제하시겠습니까?")
            .setMessage("삭제된 댓글은 복구하실 수 없습니다.\n정말로 삭제하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    //통신을 통해 댓글삭제.
                    //TODO 서버에서 댓글 삭제가 완료되면 리스트에서도 삭제.
                    RetrofitService.retrofitService.deleteComment(commentID).enqueue(object : Callback<Comment> {
                        override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                            if(response.isSuccessful){
                                Log.d("delete comment","success")
                                commentList.removeAt(position)
                                notifyDataSetChanged()
                                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Log.e("delete comment", "success but error")
                                Log.e("delete comment error code",response.code().toString())
                            }
                        }

                        override fun onFailure(call: Call<Comment>, t: Throwable){
                            Log.e("delete comment","fail")
                            Log.e("delete comment error msg",t.message.toString())
                        }

                    })


                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                })
        // 다이얼로그를 띄워주기
        builder.show()

        Log.d("지울 댓글", commentList[position].toString())
    }

    private fun onClickProfileImg(uid:String){
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("UID", uid)
        context.startActivity(intent)
    }
}