package kr.racto.milkyway.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}