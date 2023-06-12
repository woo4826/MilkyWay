package kr.racto.milkyway.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kr.racto.milkyway.ProfileDialog
import kr.racto.milkyway.databinding.ActivityRoomDetailBinding
import kr.racto.milkyway.login.App.Companion.apiService
import kr.racto.milkyway.model.RoomData
import kr.racto.milkyway.review.MainReviewActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRoomDetailBinding
    lateinit var adapter: DetailAdapter
    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
    var reviewlist = ArrayList<DetailReview>()
    var roomId: Int = -1
    lateinit var dictionary: HashMap<String, String>
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
            dictionary = (bundle.getSerializable("dictionary") as HashMap<String, String>?)!!
            if (dictionary != null) {
                roomId = dictionary["roomId"]!!.toInt()
                val name = dictionary["roomName"]
                val address = dictionary["address"]
                val callnumber = dictionary["managerTelNo"]
                binding.roomName.text = name
                binding.roomAddress.text = address
                binding.roomCallnumber.text = callnumber
            }
        }



        val call = apiService.getRoomData(roomId)

        call.enqueue(object : Callback<RoomData> {
            override fun onResponse(call: Call<RoomData>, response: Response<RoomData>) {
                if (response.isSuccessful) {
                    val roomData = response.body()
                    // JSON 데이터 사용하기
                    // 예시: val reviewList = roomData?.reviewList
                    // ...
                } else {
                    // 요청이 실패한 경우
                    Toast.makeText(this@RoomDetailActivity,"s",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<RoomData>, t: Throwable) {
                // 네트워크 요청이 실패한 경우
            }
        })

        reviewlist.add(DetailReview("qwer@naver.com",3.0, "2023-05-13","aaaaaaa"))
        reviewlist.add(DetailReview("1234@naver.com",3.0, "2023-05-13","sssssss"))

        adapter = DetailAdapter(reviewlist)
        binding.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = adapter

        binding.reviewAdd.setOnClickListener {
            if(user == null){
                ProfileDialog(this).show()
            }
            else{
                val intent = Intent(this, MainReviewActivity::class.java)
                val name = dictionary["roomName"]
                intent.putExtra("roomName",name)
                startActivity(intent)
                finish()
            }
        }

    }
}