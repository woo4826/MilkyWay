package kr.racto.milkyway.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide.init
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityRoomDetailBinding

class RoomDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRoomDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){

    }
}