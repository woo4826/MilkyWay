package kr.racto.milkyway.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide.init
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityRoomDetailBinding

class RoomDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRoomDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        val i = intent
        val bundle = i.extras
        if (bundle != null) {
            val dictionary = bundle.getSerializable("dictionary") as HashMap<String, String>?
            if (dictionary != null) {
                val name = dictionary["roomName"]
                val address = dictionary["address"]
                val callnumber = dictionary["managerTelNo"]
                binding.roomName.text = name
                binding.roomAddress.text = address
                binding.roomCallnumber.text = callnumber
            }
        }
    }
}