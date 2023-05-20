package kr.racto.milkyway.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityAgreementBinding

class AgreementActivity : AppCompatActivity() {
    lateinit var binding:ActivityAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener{
            val nextIntent=Intent(this, MainActivity::class.java)
            nextIntent.putExtra("settings",0)
            startActivity(nextIntent)
        }
    }

}