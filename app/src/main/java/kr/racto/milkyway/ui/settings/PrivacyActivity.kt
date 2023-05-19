package kr.racto.milkyway.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityPrivacyBinding

class PrivacyActivity : AppCompatActivity() {
    lateinit var binding:ActivityPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {

        }
    }
}