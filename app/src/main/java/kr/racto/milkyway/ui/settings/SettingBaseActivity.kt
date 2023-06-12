package kr.racto.milkyway.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivitySettingBaseBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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
            finish()
        }

    }

    fun readFile(fileName:Int):String{
        val inputStream: InputStream = resources.openRawResource(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line+"\n")
            line = reader.readLine()
        }
        reader.close()
        return stringBuilder.toString()
    }
}