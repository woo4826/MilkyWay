package kr.racto.milkyway.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}