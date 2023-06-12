package kr.racto.milkyway.review

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityMainReviewBinding


class MainReviewActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainReviewBinding
    var Ratingvalue=""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    fun init(){
        val ft = supportFragmentManager.beginTransaction()
        val roomName=intent.getStringExtra("roomName")
        val nextFragment=ReviewFragment()
        val bundle=Bundle()
        bundle.putString("roomName",roomName)
        nextFragment.arguments=bundle
        ft.add(R.id.review_frame,nextFragment).commit()

        binding.iconBack.setOnClickListener{
            //이전 Activity로 화면 전환
            finish()
        }
    }

    fun changeFrag(){
        val ft = supportFragmentManager.beginTransaction()

        val roomName=intent.getStringExtra("roomName")
        val roomId=intent.getIntExtra("roomId",-1)
        val nextFragment=secondReviewFragment()
        val bundle=Bundle()
        bundle.putString("roomName",roomName)
        bundle.putInt("roomId", roomId)
        nextFragment.arguments=bundle
        ft.replace(R.id.review_frame,nextFragment).commit()

    }
}