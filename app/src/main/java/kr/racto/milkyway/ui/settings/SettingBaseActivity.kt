package kr.racto.milkyway.ui.settings

import android.content.Intent
import android.graphics.Path.Op
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivitySettingBaseBinding

class SettingBaseActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragNum= intent.getIntExtra("setting_base",-1)
        if(fragNum>=0){
            val ft = supportFragmentManager.beginTransaction()
            when(fragNum){
                0->ft.replace(R.id.settings_frame,Agreement()).commit()
                1->ft.replace(R.id.settings_frame,Privacy()).commit()
                2->ft.replace(R.id.settings_frame,Developer()).commit()
                3->ft.replace(R.id.settings_frame,Opensource()).commit()
                4->ft.replace(R.id.settings_frame,ReviewManagement()).commit()
            }
        }
        binding.iconBack.setOnClickListener{
            val nextIntent= Intent(this, MainActivity::class.java)
            nextIntent.putExtra("settings",0)
            startActivity(nextIntent)
        }

    }
}