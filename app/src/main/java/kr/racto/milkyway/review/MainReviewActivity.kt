package kr.racto.milkyway.review

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityMainReviewBinding
import kr.racto.milkyway.ui.settings.Agreement
import kr.racto.milkyway.ui.settings.Developer
import kr.racto.milkyway.ui.settings.Opensource
import kr.racto.milkyway.ui.settings.Privacy
import kr.racto.milkyway.ui.settings.ReviewManagement

class MainReviewActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fragNum= intent.getIntExtra("review_base",-1)
        if(fragNum>=0){
            val ft = supportFragmentManager.beginTransaction()
            when(fragNum){
                0->ft.replace(R.id.review_frame, ReviewFragment()).commit()
                1->ft.replace(R.id.review_frame, secondReviewFragment()).commit()
            }
        }
        binding.iconBack.setOnClickListener{
            when(fragNum){
                0->{
                    val next=Intent(this, MainActivity::class.java)
                    startActivity(next)
                    fragNum=-1
                }
                1->{
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.review_frame, ReviewFragment()).commit()
                    fragNum=0
                }
            }
        }
    }
}