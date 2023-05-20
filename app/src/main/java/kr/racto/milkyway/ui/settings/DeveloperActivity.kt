package kr.racto.milkyway.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.databinding.ActivityDeveloperBinding

class DeveloperActivity : AppCompatActivity() {
    lateinit var binding:ActivityDeveloperBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            val nextIntent= Intent(this, MainActivity::class.java)
            nextIntent.putExtra("settings",0)
            startActivity(nextIntent)
        }
    }
}