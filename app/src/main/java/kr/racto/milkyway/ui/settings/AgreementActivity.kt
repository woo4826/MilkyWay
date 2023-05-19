package kr.racto.milkyway.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityAgreementBinding

class AgreementActivity : AppCompatActivity() {
    lateinit var binding:ActivityAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {

        }
    }
}