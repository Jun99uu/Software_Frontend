package com.example.sofront

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.example.sofront.databinding.ActivityMakePortfolioBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MakePortfolioActivity : AppCompatActivity() {
    lateinit var sendPortfolio: SendPortfolio
    var title = ""
    var file = ""
    var content = ""
    val converter = BitmapConverter()

    lateinit var afterImageView: ImageView
    lateinit var beforeView: CardView
    lateinit var afterView: CardView

    val user = Firebase.auth.currentUser
    val myUid = user?.uid.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMakePortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        afterImageView = binding.afterContentImageView
        beforeView = binding.beforeContentImage
        afterView = binding.afterContentImage

        binding.beforeContentImage.setOnClickListener{
            val PIintent = Intent(Intent.ACTION_GET_CONTENT)
            PIintent.setType("image/*")
            Launcher.launch(PIintent)
        }

        binding.afterContentImage.setOnClickListener{
            val PIintent = Intent(Intent.ACTION_GET_CONTENT)
            PIintent.setType("image/*")
            Launcher.launch(PIintent)
        }

        binding.portfolioSaveBtn.setOnClickListener{
            if(binding.makePortfolioTitle.text.equals("")){
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else if(binding.makePortfolioContent.text.equals("")){
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else if(file.equals("")){
                Toast.makeText(this, "사진을 첨부해주세요", Toast.LENGTH_SHORT).show()
            }else{
                Log.d("profileText",binding.makePortfolioTitle.text.toString())
                title = binding.makePortfolioTitle.text.toString()
                content = binding.makePortfolioContent.text.toString()
                savePressed(title, file, content)
            }
        }

        binding.portfolioCancleBtn.setOnClickListener{
            canclePressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val Launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data !=null) {
                val currentImageUri = it.data?.data
                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            afterImageView.setImageBitmap(bitmap)
                            file = converter.bitmapToString(bitmap)
                            afterView.visibility = View.VISIBLE
                            beforeView.visibility = View.GONE
                        } else {
                            val bitmap = currentImageUri.uriToBitmap(this)
                            afterImageView.setImageBitmap(bitmap)
                            file = converter.bitmapToString(bitmap)
                            afterView.visibility = View.VISIBLE
                            beforeView.visibility = View.GONE
                        }
                    }

                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
            }else{
                Log.d("ActivityResult","something wrong")
            }
        }

    fun canclePressed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("정말로 취소하시겠습니까?")
            .setMessage("작성하던 내용은 복구하실 수 없습니다.\n정말로 수정을 취소하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                    ActivityCompat.finishAffinity(this)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun savePressed(title:String, file:String, content:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("포트폴리오를 저장하시겠습니까?")
            .setMessage("정말로 저장하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //확인클릭
                    val tmp = SendPortfolio(myUid, title, content, file)
                    sendPortfolio = tmp
                    _postPortfolio(sendPortfolio)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //취소클릭
                    Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun _postPortfolio(sendPortfolio: SendPortfolio){
        RetrofitService.retrofitService.postPortfolio(sendPortfolio).enqueue(object :
            Callback<SendPortfolio> {
            override fun onResponse(call: Call<SendPortfolio>, response: Response<SendPortfolio>) {
                if(response.isSuccessful){
                    Log.d("postPortfolio test success", response.body().toString())
                    finish()
                }else{
                    Log.e("postPortfolio test", "success but something error")
                    Log.e("postPortfolio error code",response.code().toString())
                }
            }
            override fun onFailure(call: Call<SendPortfolio>, t: Throwable) {
                Log.e("postPortfolio test", "fail")
                Log.e("postPortfolio error msg",t.message.toString())
            }
        })
    }
    @Throws(IOException::class)
    fun Uri.uriToBitmap(context: Context): Bitmap =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver, this)
            ) { decoder: ImageDecoder, _: ImageDecoder.ImageInfo?, _: ImageDecoder.Source? ->
                decoder.isMutableRequired = true
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            BitmapDrawable(
                context.resources,
                MediaStore.Images.Media.getBitmap(context.contentResolver, this)
            ).bitmap
        }
}