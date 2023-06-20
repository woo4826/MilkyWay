package kr.racto.milkyway.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kr.racto.milkyway.ProfileDialog
import kr.racto.milkyway.R
import kr.racto.milkyway.databinding.ActivityRoomDetailBinding
import kr.racto.milkyway.login.App.Companion.apiService
import kr.racto.milkyway.model.RoomData
import kr.racto.milkyway.review.MainReviewActivity
import kr.racto.milkyway.ui.settings.SettingsReview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.round

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

    override fun onRestart() {
        super.onRestart()
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



        val call = apiService.getRoomReviewData(roomId)

        call.enqueue(object : Callback<RoomData> {
            override fun onResponse(call: Call<RoomData>, response: Response<RoomData>) {
                if (response.isSuccessful) {


                    val roomData = response.body()

                    val reviewList = roomData?.reviewList
                    val roomName = roomData?.roomName!!
                    val address = roomData?.address!!
                    val ratingAvg = roomData?.ratingAvg!!
                    val reviewCount = roomData?.reviewCount!!

                    binding.reviewAvg.text = (round(ratingAvg*10)/10).toString()+" ("+reviewCount.toString()+")"
                    binding.ratingAvg.rating = (round(ratingAvg*2)/2).toFloat()

                    if(reviewList != null && reviewList.size != 0){

                        reviewlist.clear()
                        for(i in 0 until reviewList.size){
                            reviewlist.add(DetailReview(reviewList[i].email!!,reviewList[i].rating!!, reviewList[i].title!!,reviewList[i].description!!))
                        }
                        adapter.notifyDataSetChanged()
                        binding.noReview.visibility = View.GONE
                    }else{
                        binding.noReview.visibility = View.VISIBLE
                    }

                } else {
                    if (response.code() == 500) {
                        // 서버 내부 오류인 경우 처리
                        Toast.makeText(this@RoomDetailActivity, "서버 내부 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 다른 상태 코드에 대한 처리
                        // 예: response.code() == 404 - 페이지를 찾을 수 없음
                        //     response.code() == 401 - 인증 실패
                        //     등등
                        // 400이면 리뷰가 없다고 처리
                        // Toast.makeText(this@RoomDetailActivity, "요청에 실패했습니다."+response.code().toString(), Toast.LENGTH_SHORT).show()
                        binding.noReview.visibility = View.VISIBLE
                    }
                }
            }
            override fun onFailure(call: Call<RoomData>, t: Throwable) {
                // 네트워크 요청이 실패한 경우
            }
        })

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
                intent.putExtra("roomId", roomId)
                startActivity(intent)
            }
        }
        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}