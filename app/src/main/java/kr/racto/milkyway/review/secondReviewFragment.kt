package kr.racto.milkyway.review

import ApiService
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.naver.maps.map.style.layers.SymbolLayer
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.databinding.FragmentSecondReviewBinding
import kr.racto.milkyway.login.App.Companion.apiService
import kr.racto.milkyway.model.ReviewRoomDTO
import kr.racto.milkyway.ui.detail.RoomDetailActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import org.json.JSONObject
import org.json.JSONException

class secondReviewFragment : Fragment() {

    private var _binding: FragmentSecondReviewBinding?=null

    private val binding get()=_binding!!

    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reviewViewModel= ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding= FragmentSecondReviewBinding.inflate(inflater,container,false)
        val root:View=binding.root

        init()
        return root
    }

    private fun init() {

        val roomName=arguments?.getString("roomName")!!
        binding.name.text=roomName

        binding.root.setOnClickListener {
            // 키보드를 내리는 기능을 구현
            val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
        }

        val main=activity as MainReviewActivity
        binding.ratingbar.rating=main.Ratingvalue.toFloat()
        binding.ratingText.text=main.Ratingvalue


        binding.ratingbar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val value=rating.toString()
            binding.ratingText.text=value
        }

        binding.completion.setOnClickListener {
            val ratingNum = binding.ratingbar.rating.toDouble() // 별점 값
            val contents = binding.contents.text.toString() // 리뷰 텍스트
            val roomId = arguments?.getInt("roomId")!! // 수유실 id
            val userEmail = user?.email!!

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val dateString = year.toString()+"-"+month.toString()+"-"+day.toString()

            val json = """
                {
                    "title": "$dateString",
                    "description": "$contents",
                    "rating": $ratingNum,
                    "email": "$userEmail",
                    "roomId": $roomId,
                    "roomName": "$roomName",
                    "address": "no data"
                }
            """.trimIndent()
//            Log.i("inputReview", json)
//            try {
//                JSONObject(json)
//                Toast.makeText(activity, "유효한 JSON 데이터입니다.", Toast.LENGTH_SHORT).show()
//            } catch (e: JSONException) {
//                Toast.makeText(activity, "유효하지 않은 JSON 데이터입니다.", Toast.LENGTH_SHORT).show()
//            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = json.toRequestBody(mediaType)

            // 데이터 삽입
            val call = apiService.insertReview(requestBody)
            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        // 데이터 삽입 성공 처리
                        requireActivity().finish()
                    } else {
                        if (response.code() == 500) {
                            // 서버 내부 오류인 경우 처리
                            Toast.makeText(activity, "서버 내부 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            requireActivity().finish()
                        } else {
                            // 다른 상태 코드에 대한 처리
                            // 예: response.code() == 404 - 페이지를 찾을 수 없음
                            //     response.code() == 401 - 인증 실패
                            //     등등
                            Toast.makeText(activity, "요청에 실패했습니다."+response.code().toString(), Toast.LENGTH_SHORT).show()
                            requireActivity().finish()
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    requireActivity().finish()
                }
            })
        }
    }

}