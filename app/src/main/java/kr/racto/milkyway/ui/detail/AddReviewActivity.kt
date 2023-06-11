package kr.racto.milkyway.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide.init
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityAddReviewBinding

class AddReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        binding.rating.setOnRatingBarChangeListener { _, rating, _ ->

        }
        val intent = intent
        val name = intent.getStringExtra("roomName")
        binding.roomName.text = name

        binding.btnAdd.setOnClickListener {
            val rating = binding.rating.rating
            val contents = binding.contents.text.toString()
            Toast.makeText(this, rating.toString(), Toast.LENGTH_SHORT).show()

            // 리뷰 데이터 삽입 수행

            finish()
        }
    }
}