package kr.racto.milkyway.review

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
import kr.racto.milkyway.MainActivity
import kr.racto.milkyway.databinding.FragmentSecondReviewBinding

class secondReviewFragment : Fragment() {

    private var _binding: FragmentSecondReviewBinding?=null

    private val binding get()=_binding!!
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

        val roomName=arguments?.getString("roomName")
        if(roomName!=null){
            binding.name.text=roomName
        }

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
            val ratingNum = binding.ratingbar.rating // 별점 값
            val contents = binding.contents.text.toString() // 리뷰 텍스트
            val roomId = arguments?.getInt("roomId") // 수유실 id

            // 데이터 삽입

            requireActivity().finish()
        }
    }

}