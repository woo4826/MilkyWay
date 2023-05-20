package kr.racto.milkyway.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityOpensourceBinding

class OpensourceActivity : AppCompatActivity() {
    lateinit var binding:ActivityOpensourceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOpensourceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.iconBack.setOnClickListener {
            val nextIntent= Intent(this, MainActivity::class.java)
            nextIntent.putExtra("settings",0)
            startActivity(nextIntent)
        }
    }
}