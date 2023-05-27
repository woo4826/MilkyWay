package kr.racto.milkyway.review

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        ft.add(R.id.review_frame,ReviewFragment()).commit()

        binding.iconBack.setOnClickListener{
            //이전 Activity로 화면 전환
            val next= Intent(this, MainActivity::class.java)
            startActivity(next)
        }
    }

    fun changeFrag(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.review_frame,secondReviewFragment()).commit()
    }
}