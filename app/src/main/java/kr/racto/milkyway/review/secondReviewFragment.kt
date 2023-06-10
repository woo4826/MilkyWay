package kr.racto.milkyway.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
        val main=activity as MainReviewActivity
        binding.ratingbar.rating=main.Ratingvalue.toFloat()
        binding.ratingText.text=main.Ratingvalue

        val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode== Activity.RESULT_OK && it.data!=null){
                binding.showImg.visibility=View.VISIBLE
                val uri=it.data!!.data
                Glide.with(this)
                    .load(uri)
                    .into(binding.reviewPhoto)
            }
        }

        binding.photoInsert.setOnClickListener {
            val pickImageIntent = Intent(Intent.ACTION_PICK)
            pickImageIntent.type="image/*"
            activityResult.launch(pickImageIntent)
        }

        binding.ratingbar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val value=rating.toString()
            binding.ratingText.text=value
        }

        binding.completion.setOnClickListener {
            val newIntent= Intent(requireContext(), MainActivity::class.java)
            //review 작성한 내용 intent에 넘기기
            startActivity(newIntent)
        }
    }

}