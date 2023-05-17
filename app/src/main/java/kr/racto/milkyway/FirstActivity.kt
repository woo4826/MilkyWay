package kr.racto.milkyway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.databinding.ActivityFirstBinding
import kr.racto.milkyway.databinding.ActivityLoginBinding
import kr.racto.milkyway.login.App
import kr.racto.milkyway.login.App.Companion.prefs
import kr.racto.milkyway.login.JoinActivity
import kr.racto.milkyway.login.LoginActivity
import kr.racto.milkyway.login.PreferenceUtil

class FirstActivity : AppCompatActivity() {
    lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.nonmemberbtn.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        binding.loginbtn.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
        binding.joinbtn.setOnClickListener {
            val i = Intent(this, JoinActivity::class.java)
            startActivity(i)
        }
    }
}